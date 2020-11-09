package by.epam.bigdatalab.service;

import java.time.LocalDate;

public interface PoliceAPIService {

    void processCrimesToDB(LocalDate startDate, LocalDate endDate, String path) throws ServiceException;

    void processCrimesToFile(LocalDate startDate, LocalDate endDate, String pathToPoints, String pathToSaveFile) throws ServiceException;
}
