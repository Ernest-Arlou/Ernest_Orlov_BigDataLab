package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.Point;

import java.util.List;

public interface FileDAO {
    List<Point> getPoints(String path);

    void startWritingIn(String path);

    void endWritingIn(String path);

    void write(String path, List<Crime> crimes);
}
