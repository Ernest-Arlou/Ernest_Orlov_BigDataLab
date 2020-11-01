package by.epam.bigdatalab.service.impl;

import by.epam.bigdatalab.FileException;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOException;
import by.epam.bigdatalab.dao.factory.DAOFactory;
import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.ServiceException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PoliceAPIServiceImp implements PoliceAPIService {

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String DARE_PATTERN = "yyyy-MM";
    private static final String POLICE_API = "https://data.police.uk/api";

    @Override
    public void test(String str) throws ServiceException, FileException {
        System.out.println(getPointsFromFile(str));
    }

    private List<Point> getPointsFromFile(String path) throws ServiceException, FileException {
        try {
            return DAOFactory.getInstance().getFileDAO().getPoints(path);
        } catch (DAOException e) {
            throw new ServiceException("DAOException in getPointsFromFile", e);
        }
    }

    private String buildParameters(Map<String, Object> parameters) {
//        Map<String, Object> parameters = new LinkedHashMap<>();

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
            throw new ServiceException("MalformedURLException in buildURL", e);
        }
        return url;
    }


    private <T> T doRequest(URL url, Class<T> type) throws ServiceException {


        HttpURLConnection connection = null;

        T objects = null;

        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD_GET);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);

            checkResponseStatus(connection);


            ObjectMapper JSON_MAPPER = new ObjectMapper();

            JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JSON_MAPPER.setDateFormat(new SimpleDateFormat(DARE_PATTERN));
            JSON_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);


            InputStream inputStream = connection.getInputStream();

            objects = JSON_MAPPER.readValue(inputStream, type);


        } catch (ProtocolException e) {
            throw new ServiceException("ProtocolException in doRequest", e);
        } catch (JsonParseException e) {
            throw new ServiceException("JsonParseException in doRequest", e);
        } catch (JsonMappingException e) {
            throw new ServiceException("JsonMappingException in doRequest", e);
        } catch (IOException e) {
            throw new ServiceException("IOException in doRequest", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return objects;
    }


    private void checkResponseStatus(HttpURLConnection connection) throws ServiceException {
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new ServiceException("IOException in checkResponseStatus", e);
        }

        if (responseCode != 200) {
            throw new ServiceException("Error retreiving resource " +
                    "(" + connection.getURL() + ") " +
                    "(" + responseCode + ")"
            );
        }

    }


}