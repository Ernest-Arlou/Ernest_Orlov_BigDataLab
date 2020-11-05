package by.epam.bigdatalab.service;

import by.epam.bigdatalab.FileException;

import java.time.LocalDate;

public interface PoliceAPIService {


    void test() throws ServiceException, FileException;

    void processCrimes(LocalDate startDate, LocalDate endDate, String path) throws ServiceException;
}
