package by.epam.bigdatalab.service.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.Request;
import by.epam.bigdatalab.service.ServiceException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class PoliceAPIServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIServiceTest.class);

    private static final String PATH = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\LondonStations.csv";
    private static final String SAVE_PATH = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\Crimes.txt";

    LocalDate startDate = LocalDate.of(2019, 5, 1);
    LocalDate endDate = LocalDate.of(2019, 5, 1);

    @BeforeClass
    public static void initializeConnectionPool() {
        ConnectionPoolHolder.getInstance().getConnectionPool().init();
    }

    @AfterClass
    public static void disposeConnectionPool() {
        ConnectionPoolHolder.getInstance().getConnectionPool().dispose();
    }

    @Test
    public void processCrimesToDB() throws ServiceException {
        PoliceAPIService policeAPIService = new PoliceAPIService();
        policeAPIService.processCrimesToDB(startDate,endDate,PATH);
    }

    @Test
    public void onePoint(){
        String str = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2018-01";


        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException in PoliceAPIServiceImp method buildURL()", e);
        }

        List<Crime> crimes = Request.doRequest(url, Crime.class);
        System.out.println(crimes);
        
        
    }

    @Test
    public void processCrimesToFile() {

    }
}