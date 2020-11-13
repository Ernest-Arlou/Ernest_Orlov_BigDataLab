package by.epam.bigdatalab.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Request {
    private static final int CONNECTION_TIMEOUT = 6000;
    private static final int CONNECTION_READ_TIMEOUT = 6000;

    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    private static final int BUFFER = 1024;
    private static final String ENCODING = "UTF-8";

    public static <T> List<T> doRequest(URL url, Class<T> type) {
        HttpURLConnection connection = null;

        List<T> objects = null;

        try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(CONNECTION_READ_TIMEOUT);

            if (gotConnection(connection)) {

                InputStream inputStream = connection.getInputStream();

                objects = JSON.parseArray(getStringFromStream(inputStream), type);

            }


        } catch (ProtocolException e) {
            logger.error("ProtocolException in PoliceAPIServiceImp method doRequest()", e);
        } catch (IOException e) {
            logger.error("IOException in PoliceAPIServiceImp method doRequest()", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return objects;
    }

    private static boolean gotConnection(HttpURLConnection connection) {
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            logger.error("IOException in PoliceAPIServiceImp method gotConnection()", e);

        }

        return responseCode == 200;

    }

    public static String getStringFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(ENCODING);

    }
}
