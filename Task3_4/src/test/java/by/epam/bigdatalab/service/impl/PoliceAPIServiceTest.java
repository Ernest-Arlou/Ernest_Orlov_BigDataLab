package by.epam.bigdatalab.service.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.Force;
import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.Request;
import by.epam.bigdatalab.service.ServiceException;
import by.epam.bigdatalab.service.URLManager;
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

    private static final String PATH_TO_POINTS = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\LondonStations.csv";
    private static final String SAVE_PATH = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\Crimes.txt";
    private final PoliceAPIService policeAPIService = new PoliceAPIService();
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
        policeAPIService.processCrimesToDB(startDate, endDate, PATH_TO_POINTS);
    }

    @Test
    public void processCrimesToFile() throws ServiceException {
        policeAPIService.processCrimesToFile(startDate, endDate, PATH_TO_POINTS, SAVE_PATH);
    }

    @Test
    public void onePoint() {
        String str = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2018-01";

        DAOHolder.getInstance().getDataBaseDAO().saveCrimesToDB(Request.doRequest(URLManager.createURL(str), Crime.class));


    }

    @Test
    public void getForces() {
        List<Force> forces = policeAPIService.getForces();
        System.out.println(forces.size());
        System.out.println(forces);
    }

    @Test
    public void getStops() {
        List<StopAndSearch> stopAndSearches = policeAPIService.getStopsAnsSearches();
        System.out.println(stopAndSearches.size());
        System.out.println(stopAndSearches);
    }

}