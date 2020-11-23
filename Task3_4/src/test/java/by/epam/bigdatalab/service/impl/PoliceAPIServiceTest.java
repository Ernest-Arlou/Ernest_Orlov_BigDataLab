package by.epam.bigdatalab.service.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import by.epam.bigdatalab.service.PoliceAPIService;
import by.epam.bigdatalab.service.Request;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PoliceAPIServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PoliceAPIServiceTest.class);

    private static final String PATH_TO_POINTS = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\LondonStations.csv";
    private static final String PATH_TO_SAVE_FILE = "E:\\University_and_Work\\Java_Training\\BigData\\Remote\\Task3_4\\src\\main\\resources\\Crimes.txt";
    private final PoliceAPIService policeAPIService = new PoliceAPIService();
    LocalDate startDate = LocalDate.of(2018, 1, 1);
    LocalDate endDate = LocalDate.of(2018, 1, 1);

    //
    @BeforeClass
    public static void initializeConnectionPool() {
        ConnectionPoolHolder.getInstance().getConnectionPool().init();
    }

    @AfterClass
    public static void disposeConnectionPool() {
        ConnectionPoolHolder.getInstance().getConnectionPool().dispose();
    }

        @Test
    public void processCrimesToDB() {
        Assert.assertTrue(true);
    }
//
    @Test
    public void processCrimesToFile() throws IOException {
//        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
    }

    //
//    @Test
//    public void processCrimesToDB() {
//        LocalDateTime start = LocalDateTime.now();
//        policeAPIService.processCrimesToDB(startDate, endDate, PATH_TO_POINTS);
//        System.out.println(start);
//        System.out.println(LocalDateTime.now());
//    }

    //
//    @Test
//    public void processCrimesToFile() {
//        policeAPIService.processCrimesToFile(startDate, endDate, PATH_TO_POINTS, PATH_TO_SAVE_FILE);
//    }
//
//    @Test
//    public void onePoint() {
//        String str = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2018-01";
//
//        Queue<String> strings = new LinkedList<>();
//        strings.add(str);
//        List<Crime> crimes = Request.doRequest(strings, Crime.class);
//        Assert.assertNotNull(crimes);
//        DAOHolder.getInstance().getDataBaseDAO().saveCrimes(crimes);
//
//    }

    ////
//    @Test
//    public void oneStopAndSearch() {
//        String str = "https://data.police.uk/api/stops-force?force=avon-and-somerset&date=2018-01";
//
//        List<StopAndSearch> stopAndSearches =  Request.doRequest(URLManager.createURL(str), StopAndSearch.class);
//        Assert.assertNotNull(stopAndSearches);
//
//        DAOHolder.getInstance().getDataBaseDAO().saveStopAndSearches(stopAndSearches);
//
//    }
//
//    @Test
//    public void getForces() {
//        List<Force> forces = policeAPIService.getForces();
//    }
//
//
//    @Test
//    public void processStopsAndSearchesToDB() {
//        policeAPIService.processStopsAndSearchesToDB(startDate, endDate);
//    }
//
//    @Test
//    public void processStopsAndSearchesToFile() {
//        policeAPIService.processStopsAndSearchesToFile(startDate, endDate, PATH_TO_SAVE_FILE);
//    }

}