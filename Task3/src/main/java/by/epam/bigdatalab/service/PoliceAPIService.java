package by.epam.bigdatalab.service;

import by.epam.bigdatalab.FileException;

public interface PoliceAPIService {
    void test(String str) throws ServiceException, FileException;
}
