package by.epam.bigdatalab.dao.connectionpool;

import javax.sql.DataSource;

public interface ConnectionPool {
    void init();

    DataSource getSource();

    void dispose();
}
