package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOException;
import by.epam.bigdatalab.dao.DAOHolder;
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

    public void processCrimesToDB(LocalDate startDate, LocalDate endDate, String path) throws ServiceException {
        processCrimes(startDate, endDate, path);
    }

//    public void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) throws ServiceException {
//        String jsonOutput = JSON.toJSONString(processCrimes(startDate, endDate, pathToPoints));
//        try {
//            DAOHolder.getInstance().getFileDAO().saveCrimes(jsonOutput, pathToSaveFile);
//        } catch (DAOException e) {
//            logger.error("DAOException in PoliceAPIServiceImp method saveCrimesInFile()");
//            throw new ServiceException("Can't save crimes in file", e);
//        }
//    }


    private void processCrimes(LocalDate startDate, LocalDate endDate, String path) throws ServiceException {
        List<Point> points = getPointsFromFile(path);
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
            ses.schedule(new CrimePointThread(urls.get(i)), startingDelaySeconds, TimeUnit.SECONDS);
        }

        awaitTerminationAfterShutdown(ses);

        LocalDateTime end = LocalDateTime.now();
        System.out.println(now);
        System.out.println(end);


    }

    private List<Point> getPointsFromFile(String path) throws ServiceException {
        try {
            return DAOHolder.getInstance().getFileDAO().getPoints(path);
        } catch (DAOException e) {
            logger.error("DAOException in PoliceAPIServiceImp method processCrimes()");
            throw new ServiceException("DAOException in getPointsFromFile", e);
        }
    }


}
