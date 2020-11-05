package by.epam.bigdatalab.dao.connectionpool;

import com.zaxxer.hikari.HikariDataSource;

public interface ConnectionPool {
    void init();

    HikariDataSource getSource();

    void dispose();
}
