package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.bean.Point;

import java.util.List;

public interface FileDAO {
    List<Point> getPoints(String path) throws DAOException;

    void saveCrimes(String crimes, String path) throws DAOException;
}
