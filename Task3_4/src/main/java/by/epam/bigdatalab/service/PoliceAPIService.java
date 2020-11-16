package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Force;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.thread.DBCrimePointThread;
import by.epam.bigdatalab.service.thread.DBStopByForceThread;
import by.epam.bigdatalab.service.thread.FileCrimePointThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PoliceAPIService {
    private static final int CORE_POOL_SIZE = 30;
    private static final int CONNECTIONS_LIMIT = 15;
    private static final int CONNECTIONS_LIMIT_PER_TIME_SECONDS = 1;
    private static final int FORCED_THREAD_TERMINATION_SECONDS = 120;
    private static final String FORCES_URL = "https://data.police.uk/api/forces";


    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIService.class);

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(FORCED_THREAD_TERMINATION_SECONDS, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error("InterruptedException in PoliceAPIServiceImp method awaitTerminationAfterShutdown()");
            throw new RuntimeException();
        }
    }

    public List<Force> getForces() {
        return Request.doRequest(URLManager.createURL(FORCES_URL), Force.class);
    }

    public List<StopAndSearch> getStopsAnsSearches() {
        return Request.doRequest(URLManager.createURL("https://data.police.uk/api/stops-force?force=avon-and-somerset&date=2018-01"), StopAndSearch.class);
    }

    public void processStopsAndSearchesToDB(LocalDate startDate, LocalDate endDate){
        List<Runnable> threads = new LinkedList<>();

        for (URL url : URLManager.buildStopsAndSearchesURLs(startDate,endDate,getForces())) {
         threads.add(new DBStopByForceThread(url));
        }

        executeThreads(threads);
    }

    public void processCrimesToDB(LocalDate startDate, LocalDate endDate, String pathToPoints)   {
        List<Runnable> threads = new LinkedList<>();

        for (URL url : URLManager.buildCrimesURLs(startDate, endDate, getPointsFromFile(pathToPoints))) {
            threads.add(new DBCrimePointThread(url));
        }
        executeThreads(threads);
    }

    public void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile)   {
        DAOHolder.getInstance().getFileDAO().startWritingIn(pathToSaveFile);

        List<Runnable> threads = new LinkedList<>();

        for (URL url : URLManager.buildCrimesURLs(startDate, endDate, getPointsFromFile(pathToPoints))) {
            threads.add(new FileCrimePointThread(url, pathToSaveFile));
        }
        executeThreads(threads);

        DAOHolder.getInstance().getFileDAO().endWritingIn(pathToSaveFile);
    }

    private List<Point> getPointsFromFile(String path) {
        List<Point> points = DAOHolder.getInstance().getFileDAO().getPoints(path);
        if (points == null || points.isEmpty()) {
            logger.error("No points in file in PoliceAPIServiceImp method processCrimes()");
            throw new RuntimeException();
        }
        return points;
    }

    private void executeThreads(List<Runnable> threads) {
        ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
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
