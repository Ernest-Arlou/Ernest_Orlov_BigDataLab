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
import java.util.*;

public class PoliceAPIServiceImp implements PoliceAPIService {

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String DATE_PATTERN = "yyyy-MM";
    private static final String POLICE_API = "https://data.police.uk/api";
    private static final String CRIMES_URI = "/crimes-street/all-crime";
    private static final int CONNECTION_TIMEOUT = 6000;
    private static final int CONNECTION_READ_TIMEOUT = 6000;
    private static final String PARAMETER_LATITUDE = "lat";
    private static final String PARAMETER_LONGITUDE = "lng";
    private static final String PARAMETER_DATE = "date";

    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIServiceImp.class);


    @Override
    public void test() throws ServiceException, FileException {


        testOnePoint();
//
//        LocalDate start = LocalDate.of(2018, 1, 1);
//        LocalDate end = LocalDate.of(2018, 1, 1);
//
//        String path = "E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv";
//
//
//        processCrimes(start, end, path);


    }

    private void testOnePoint() throws ServiceException {

        Date date = new GregorianCalendar(2019, 4, 1).getTime();

        double latitude = 52.629729;
        double longitude = -1.131592;


        Map<String, Object> stringObjectMap1 = new LinkedHashMap<>();
        stringObjectMap1.put("lat", latitude);
        stringObjectMap1.put("lng", longitude);
        stringObjectMap1.put("date", Util.formatDate(date));


        List<Crime> crimes = doRequest(buildURL(CRIMES_URI, stringObjectMap1), Crime.class);

        saveCrimesInDB(crimes);

    }


    @Override
    public void processCrimes(LocalDate startDate, LocalDate endDate, String path) throws ServiceException {
        List<Point> points = null;
        try {
            points = DAOFactory.getInstance().getFileDAO().getPoints(path);
            if (points == null) {
                logger.error("No points in file in PoliceAPIServiceImp method processCrimes()");
                throw new ServiceException("No points in file");
            }

            List<LocalDate> localDates = buildDateRange(startDate, endDate);
            List<URL> urls = buildURLs(localDates, points);

            System.out.println(urls.size());

            int i = 0;
            for (URL url : urls) {
                List<Crime> crimes = doRequest(url, Crime.class);
                System.out.println(i);
                i++;
                saveCrimesInDB(crimes);
                if (i == 40)
                    return;

            }


        } catch (DAOException e) {
            logger.error("DAOException in PoliceAPIServiceImp method processCrimes()");
            throw new ServiceException("DAOException in getPointsFromFile", e);
        }

    }

    private void saveCrimesInDB(List<Crime> crimes) {
        DAOFactory.getInstance().getDataBaseDAO().saveCrimesToDB(crimes);

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


    private List<Point> getPointsFromFile(String path) throws ServiceException, FileException {
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


}
