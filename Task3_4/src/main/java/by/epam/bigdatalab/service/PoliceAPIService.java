package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Force;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.thread.DBCrimePointThread;
import by.epam.bigdatalab.service.thread.FileCrimePointThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private static final String FORCES_URL ="https://data.police.uk/api/forces";


    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIService.class);

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) throws ServiceException {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(FORCED_THREAD_TERMINATION_SECONDS, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            logger.error("InterruptedException in PoliceAPIServiceImp method awaitTerminationAfterShutdown()");
            throw new ServiceException("DAOException in getPointsFromFile");
        }
    }

    public void processCrimesToDB(LocalDate startDate, LocalDate endDate, String pathToPoints) throws ServiceException {
        process(startDate, endDate, pathToPoints, null);
    }

    public void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) throws ServiceException {

        DAOHolder.getInstance().getFileDAO().startWritingIn(pathToSaveFile);

        process(startDate, endDate, pathToPoints, pathToSaveFile);

        DAOHolder.getInstance().getFileDAO().endWritingIn(pathToSaveFile);

    }

    private void process(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) throws ServiceException {

        DAOHolder.getInstance().getFileDAO().startWritingIn(pathToSaveFile);

        List<Point> points = getPointsFromFile(pathToPoints);
        if (points == null) {
            logger.error("No points in file in PoliceAPIServiceImp method processCrimes()");
            throw new ServiceException("No points in file");
        }

        List<LocalDate> localDates = DateUtil.buildDateRange(startDate, endDate);
        List<URL> urls = URLManager.buildCrimesURLs(localDates, points);

        ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);

        System.out.println(urls.size());

        LocalDateTime now = LocalDateTime.now();
        int startingDelaySeconds = 0;
        for (int i = 0; i < urls.size(); i++) {
            if ((i != 0) && (i % CONNECTIONS_LIMIT == 0)) {
                startingDelaySeconds += CONNECTIONS_LIMIT_PER_TIME_SECONDS;
            }
            if (pathToSaveFile == null) {
                ses.schedule(new DBCrimePointThread(urls.get(i)), startingDelaySeconds, TimeUnit.SECONDS);
            } else {
                ses.schedule(new FileCrimePointThread(urls.get(i), pathToSaveFile), startingDelaySeconds, TimeUnit.SECONDS);
            }
        }

        awaitTerminationAfterShutdown(ses);

        LocalDateTime end = LocalDateTime.now();
        System.out.println(now);
        System.out.println(end);

    }

    private List<Point> getPointsFromFile(String path) throws ServiceException {
        return DAOHolder.getInstance().getFileDAO().getPoints(path);
    }

    public List<Force> getForces(){
        return Request.doRequest(URLManager.createURL(FORCES_URL),Force.class);
    }


    public List<StopAndSearch> getStopsAnsSearches(){
        return Request.doRequest(URLManager.createURL("https://data.police.uk/api/stops-force?force=avon-and-somerset&date=2018-01"),StopAndSearch.class);
    }

}
