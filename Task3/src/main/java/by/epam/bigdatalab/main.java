package by.epam.bigdatalab;


import by.epam.bigdatalab.bean.Crime;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        doRequest();
    }


    private static final String REQUEST_METHOD_GET = "GET";

//    private Map<String, Object> parameters = new LinkedHashMap<>();
//
//    private void setParameters(){
//        parameters.
//    }
//
//    protected String buildParameters(){
//        Set<String> keys = this.parameters.keySet();
//        if(keys.isEmpty()){
//            return "";
//        }
//        StringBuilder sb = new StringBuilder();
//        boolean isFirst = true;
//        for(String s : keys){
//            if(isFirst){
//                sb.append("?");
//                isFirst = false;
//            }else{
//                sb.append("&");
//            }
//            sb.append(s).append("=").append(parameters.get(s));
//        }
//
//        System.out.println(sb);
//        return sb.toString();
//    }


        public static  <T> T doRequest() throws Exception {
//        String url = uri + this.buildParameters();

            String urlStr = "https://data.police.uk/api/crimes-street/all-crime?poly=52.268,0.543:52.794,0.238:52.130,0.478&date=2018-01";
            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }



            HttpURLConnection connection = null;
            try {

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD_GET);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);

            checkResponseStatus(connection);



            T response = parseResponse(connection);
            return response;
        } catch (IOException io) {
                System.out.println(io.getMessage());
        }
            finally {
               connection.disconnect();
            }
            return null;
    }

        protected static  <T> T parseResponse(HttpURLConnection connection) throws IOException{



        try {


           ObjectMapper JSON_MAPPER = new ObjectMapper();



                JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JSON_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM"));
                JSON_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);


                InputStream inputStream = connection.getInputStream();
            List<Crime> crimes = Arrays.asList( JSON_MAPPER.readValue(inputStream, Crime[].class));
            System.out.println(crimes);





//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) !=null ){
//                stringBuilder.append(line);
//            }
//            reader.close();
//
//            System.out.println(stringBuilder.toString());

//            if (existing != null) {
//                return PoliceData.getJsonMapper().readerForUpdating(existing).readValue(response);
//            }
//            if (type != null) {
//                return PoliceData.getJsonMapper().readValue(response, type);
//            }
            return null;
        } finally {

        }
    }

        private static void checkResponseStatus(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        /**
         * As per documentation
         * If a request succeeds, the API will return a 200 status code.
         * If a request fails, the API will return a non-200 status code.
         */
        if(responseCode != 200){
            throw new Exception(
                    "Error retreiving resource " +
                            "(" + connection.getURL() + ") " +
                            "(" + responseCode + ")"
            );
        }

    }





    }
