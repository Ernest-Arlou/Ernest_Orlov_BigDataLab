package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Force;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.thread.DBCrimePointThread;
import by.epam.bigdatalab.service.thread.DBStopByForceThread;
import by.epam.bigdatalab.service.thread.FileCrimePointThread;
import by.epam.bigdatalab.service.thread.FileStopByForceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class PoliceAPIService {
    private static final int CORE_POOL_SIZE = 16;
    private static final int CONNECTIONS_LIMIT = 14;
    private static final int CONNECTIONS_LIMIT_PER_TIME_SECONDS = 1;
    private static final int FORCED_THREAD_TERMINATION_SECONDS = 12000;
    private static final String FORCES_URL = "https://data.police.uk/api/forces";
    private static final PointsReader POINTS_READER = new PointsReader();

    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIService.class);


    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(FORCED_THREAD_TERMINATION_SECONDS, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error(e.toString());
            throw new RuntimeException();
        }
    }

    public List<Force> getForces() {
        Queue<String> strings = new LinkedList<>();
        strings.add(FORCES_URL);
        return Request.doRequest(strings, Force.class);
    }

    public void processStopsAndSearchesToDB(LocalDate startDate, LocalDate endDate) {
        Queue<String> urls = new ConcurrentLinkedQueue<>(URLManager.buildStopsAndSearchesURLs(startDate, endDate, getForces()));

        do {
            List<Runnable> threads = new LinkedList<>();

            for (int i = 0; i < urls.size(); i++) {
                threads.add(new DBStopByForceThread(urls));
            }
            executeThreads(threads);
        } while (!urls.isEmpty());
    }

    public void processCrimesToDB(LocalDate startDate, LocalDate endDate, String pathToPoints) {
        Queue<String> urls = new ConcurrentLinkedQueue<>(URLManager.buildCrimesURLs(startDate, endDate, getPointsFromFile(pathToPoints)));

        do {
            List<Runnable> threads = new LinkedList<>();
            for (int i = 0; i < urls.size(); i++) {
                threads.add(new DBCrimePointThread(urls));
            }
            executeThreads(threads);
        } while (!urls.isEmpty());
    }

    public void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) {
        DAOHolder.getInstance().getFileDAO().startWritingIn(pathToSaveFile);

        Queue<String> urls = new ConcurrentLinkedQueue<>(URLManager.buildCrimesURLs(startDate, endDate, getPointsFromFile(pathToPoints)));
        do {
            List<Runnable> threads = new LinkedList<>();
            for (int i = 0; i < urls.size(); i++) {
                threads.add(new FileCrimePointThread(urls, pathToSaveFile));
            }
            executeThreads(threads);
        } while (!urls.isEmpty());

        DAOHolder.getInstance().getFileDAO().endWritingIn(pathToSaveFile);
    }

    public void processStopsAndSearchesToFile(LocalDate startDate, LocalDate endDate, String pathToSaveFile) {
        DAOHolder.getInstance().getFileDAO().startWritingIn(pathToSaveFile);

        Queue<String> urls = new ConcurrentLinkedQueue<>(URLManager.buildStopsAndSearchesURLs(startDate, endDate, getForces()));

        do {
            List<Runnable> threads = new LinkedList<>();
            for (int i = 0; i < urls.size(); i++) {
                threads.add(new FileStopByForceThread(urls, pathToSaveFile));
            }
            executeThreads(threads);
        } while (!urls.isEmpty());

        DAOHolder.getInstance().getFileDAO().endWritingIn(pathToSaveFile);
    }

    private List<Point> getPointsFromFile(String path) {
        List<Point> points = POINTS_READER.getPoints(path);
        if (points == null || points.isEmpty()) {
            logger.error("No points in file in PoliceAPIServiceImp method processCrimes()");
            throw new RuntimeException();
        }
        return points;
    }

    private void executeThreads(List<Runnable> threads) {
        ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
        System.out.println(threads.size());

        int startingDelaySeconds = 0;
        for (int i = 0; i < threads.size(); i++) {
            if ((i != 0) && (i % CONNECTIONS_LIMIT == 0)) {
                startingDelaySeconds += CONNECTIONS_LIMIT_PER_TIME_SECONDS;
            }
            scheduledThreadPoolExecutor.schedule(threads.get(i), startingDelaySeconds, TimeUnit.SECONDS);
        }
        awaitTerminationAfterShutdown(scheduledThreadPoolExecutor);
    }

}
