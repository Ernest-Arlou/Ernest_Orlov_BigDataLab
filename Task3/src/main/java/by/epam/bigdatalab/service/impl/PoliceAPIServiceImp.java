package by.epam.bigdatalab.service.impl;

import by.epam.bigdatalab.FileException;
import by.epam.bigdatalab.Util;
import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOException;
import by.epam.bigdatalab.dao.factory.DAOFactory;
import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.ServiceException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class PoliceAPIServiceImp implements PoliceAPIService {

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String DATE_PATTERN = "yyyy-MM";
    private static final String POLICE_API = "https://data.police.uk/api";
    private static final String CRIMES_URI = "/crimes-street/all-crime";
    private static final int CORE_POOL_SIZE = 30;
    private static final int CONNECTIONS_LIMIT = 15;
    private static final int CONNECTIONS_LIMIT_PER_TIME_SECONDS = 1;
    private static final int FORCED_THREAD_TERMINATION_SECONDS = 120;
    private static final int CONNECTION_TIMEOUT = 6000;
    private static final int CONNECTION_READ_TIMEOUT = 6000;
    private static final String PARAMETER_LATITUDE = "lat";
    private static final String PARAMETER_LONGITUDE = "lng";
    private static final String PARAMETER_DATE = "date";

    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIServiceImp.class);

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

    @Override
    public void processCrimesToDB(LocalDate startDate, LocalDate endDate, String path) throws ServiceException {
        DAOFactory.getInstance().getDataBaseDAO().saveCrimesToDB(processCrimes(startDate, endDate, path));
    }

    @Override
    public void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) throws ServiceException {
        saveCrimesInFile(processCrimes(startDate, endDate, pathToPoints), pathToSaveFile);
    }


    private Set<Crime> processCrimes(LocalDate startDate, LocalDate endDate, String path) throws ServiceException {
        List<Point> points = null;
        Set<Crime> crimesSet = null;

        points = getPointsFromFile(path);
        if (points == null) {
            logger.error("No points in file in PoliceAPIServiceImp method processCrimes()");
            throw new ServiceException("No points in file");
        }

        List<LocalDate> localDates = buildDateRange(startDate, endDate);
        List<URL> urls = buildURLs(localDates, points);

        System.out.println(urls.size());
        crimesSet = new CopyOnWriteArraySet<>();
        ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);

        LocalDateTime start = LocalDateTime.now();

        int startingDelaySeconds = 0;
        for (int i = 0; i < urls.size(); i++) {
            if (i % CONNECTIONS_LIMIT == 0) {
                startingDelaySeconds += CONNECTIONS_LIMIT_PER_TIME_SECONDS;
            }
            ses.schedule(new CrimeRequest(urls.get(i), crimesSet), startingDelaySeconds, TimeUnit.SECONDS);
        }


        awaitTerminationAfterShutdown(ses);
        LocalDateTime end = LocalDateTime.now();

        System.out.println("Crimes set size: " + crimesSet.size());
        System.out.println(start);
        System.out.println(end);


        return crimesSet;
    }

    private void saveCrimesInFile(Set<Crime> crimes, String path) throws ServiceException {
        String jsonOutput = JSON.toJSONString(crimes);
        try {
            DAOFactory.getInstance().getFileDAO().saveCrimes(jsonOutput, path);
        } catch (DAOException e) {
            logger.error("DAOException in PoliceAPIServiceImp method saveCrimesInFile()");
            throw new ServiceException("Can't save crimes in file", e);
        }
    }

    private List<LocalDate> buildDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> localDates = new ArrayList<>();
        if (startDate.compareTo(endDate) < 1) {
            do {
                localDates.add(startDate);
                startDate = startDate.plusMonths(1);
            } while (startDate.compareTo(endDate) < 1);
        }
        return localDates;
    }

    private List<URL> buildURLs(List<LocalDate> dates, List<Point> points) throws ServiceException {
        List<URL> urls = new ArrayList<>();
        for (LocalDate localDate : dates) {
            for (Point point : points) {
                Map<String, Object> parameters = new LinkedHashMap<>();
                parameters.put(PARAMETER_LATITUDE, point.getLatitude());
                parameters.put(PARAMETER_LONGITUDE, point.getLongitude());
                parameters.put(PARAMETER_DATE, Util.formatDate(localDate));
                urls.add(buildURL(CRIMES_URI, parameters));
            }
        }
        return urls;
    }

    private List<Point> getPointsFromFile(String path) throws ServiceException {
        try {
            return DAOFactory.getInstance().getFileDAO().getPoints(path);
        } catch (DAOException e) {
            logger.error("DAOException in PoliceAPIServiceImp method processCrimes()");
            throw new ServiceException("DAOException in getPointsFromFile", e);
        }
    }

    private String buildParameters(Map<String, Object> parameters) {

        Set<String> keys = parameters.keySet();
        if (keys.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : keys) {
            if (isFirst) {
                sb.append("?");
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(s).append("=").append(parameters.get(s));
        }
        return sb.toString();
    }

    private URL buildURL(String uri, Map<String, Object> parameters) throws ServiceException {
        String urlStr = POLICE_API + uri + buildParameters(parameters);
        URL url = null;
        try {
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            logger.error("MalformedURLException in PoliceAPIServiceImp method buildURL()");
            throw new ServiceException("MalformedURLException in buildURL", e);
        }
        return url;
    }

    private <T> List<T> doRequest(URL url, Class<T> type) throws ServiceException {
        HttpURLConnection connection = null;

        List<T> objects = null;

        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_READ_TIMEOUT);

            if (gotConnection(connection)) {

                InputStream inputStream = connection.getInputStream();

                objects = JSON.parseArray(Util.getString(inputStream), type);

            }


        } catch (ProtocolException e) {
            logger.error("ProtocolException in PoliceAPIServiceImp method doRequest()");
            throw new ServiceException("ProtocolException in doRequest", e);
        } catch (IOException e) {
            logger.error("IOException in PoliceAPIServiceImp method doRequest()");
            throw new ServiceException("IOException in doRequest", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return objects;
    }

    private boolean gotConnection(HttpURLConnection connection) throws ServiceException {
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            logger.error("IOException in PoliceAPIServiceImp method gotConnection()");
            throw new ServiceException("IOException in gotConnection", e);
        }

        if (responseCode == 200) {
            return true;
        }

        if (responseCode == 404) {
            return false;
        }

        if (responseCode >= 500) {
            return false;
        }

        String errorStr = "Error retreiving resource " +
                "(" + connection.getURL() + ") " +
                "(" + responseCode + ")";

        logger.error("Error in PoliceAPIServiceImp method gotConnection() - " + errorStr);
        throw new ServiceException(errorStr);

    }

    private class CrimeRequest implements Callable {

        private Set<Crime> set;
        private URL url;


        public CrimeRequest(URL url, Set<Crime> set) {
            this.url = url;
            this.set = set;

        }

        @Override
        public Object call() throws ServiceException {
            List<Crime> crimeList = doRequest(url, Crime.class);
            if (crimeList != null) {
                set.addAll(crimeList);
            }
            return null;
        }
    }


}
