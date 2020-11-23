package by.epam.bigdatalab.dao;

import java.util.List;

public interface FileDAO {

    void startWritingIn(String path);

    void endWritingIn(String path);

    <T> void write(String path, List<T> objects);

}
