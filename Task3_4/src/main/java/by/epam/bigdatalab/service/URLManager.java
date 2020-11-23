package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Force;
import by.epam.bigdatalab.bean.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class URLManager {
    private static final String POLICE_API = "https://data.police.uk/api";
    private static final String CRIMES_URI = "/crimes-street/all-crime";
    private static final String STOP_AND_SEARCH_URI = "/stops-force";

    private static final String PARAMETER_LATITUDE = "lat";
    private static final String PARAMETER_LONGITUDE = "lng";
    private static final String PARAMETER_DATE = "date";
    private static final String PARAMETER_FORCE = "force";


    private static final Logger logger = LoggerFactory.getLogger(URLManager.class);


    public static List<String> buildCrimesURLs(LocalDate startDate, LocalDate endDate, List<Point> points) {
        List<LocalDate> localDateList = DateUtil.buildDateRange(startDate, endDate);
        List<String> urls = new LinkedList<>();
        for (LocalDate localDate : localDateList) {
            for (Point point : points) {
                Map<String, Object> parameters = new LinkedHashMap<>();
                parameters.put(PARAMETER_LATITUDE, point.getLatitude());
                parameters.put(PARAMETER_LONGITUDE, point.getLongitude());
                parameters.put(PARAMETER_DATE, DateUtil.formatDate(localDate));
                urls.add(buildURL(CRIMES_URI, parameters));
            }
        }
        return urls;
    }

    public static List<String> buildStopsAndSearchesURLs(LocalDate startDate, LocalDate endDate, List<Force> forces) {
        List<LocalDate> localDateList = DateUtil.buildDateRange(startDate, endDate);

        List<String> urls = new LinkedList<>();
        for (LocalDate localDate : localDateList) {
            for (Force force : forces) {
                Map<String, Object> parameters = new LinkedHashMap<>();
                parameters.put(PARAMETER_FORCE, force.getId());
                parameters.put(PARAMETER_DATE, DateUtil.formatDate(localDate));
                urls.add(buildURL(STOP_AND_SEARCH_URI, parameters));
            }
        }
        return urls;
    }

    public static String buildURL(String uri, Map<String, Object> parameters) {
        return POLICE_API + uri + buildURLParameters(parameters);
    }


    private static String buildURLParameters(Map<String, Object> parameters) {

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
}
