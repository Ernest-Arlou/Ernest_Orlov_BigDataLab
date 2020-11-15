package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class URLManager {
    private static final String POLICE_API = "https://data.police.uk/api";
    private static final String CRIMES_URI = "/crimes-street/all-crime";

    private static final String PARAMETER_LATITUDE = "lat";
    private static final String PARAMETER_LONGITUDE = "lng";
    private static final String PARAMETER_DATE = "date";

    private static final Logger logger = LoggerFactory.getLogger(URLManager.class);


    public static List<URL> buildCrimesURLs(List<LocalDate> dates, List<Point> points) {
        List<URL> urls = new ArrayList<>();
        for (LocalDate localDate : dates) {
            for (Point point : points) {
                Map<String, Object> parameters = new LinkedHashMap<>();
                parameters.put(PARAMETER_LATITUDE, point.getLatitude());
                parameters.put(PARAMETER_LONGITUDE, point.getLongitude());
                parameters.put(PARAMETER_DATE, DateUtil.formatDate(localDate));
                URL url = buildURL(CRIMES_URI, parameters);
                if (url != null) {
                    urls.add(url);
                }
            }
        }
        return urls;
    }

    public static URL createURL(String urlStr){
        URL url = null;
        try {
            url = new URL(urlStr);

        } catch (MalformedURLException e) {
            logger.error("MalformedURLException in PoliceAPIServiceImp method buildURL()", e);
        }
        return url;
    }

    private static URL buildURL(String uri, Map<String, Object> parameters) {
        String urlStr = POLICE_API + uri + buildURLParameters(parameters);
       return createURL(urlStr);
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
