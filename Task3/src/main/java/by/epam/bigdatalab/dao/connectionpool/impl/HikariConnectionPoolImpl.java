package by.epam.bigdatalab.dao.connectionpool.impl;

import by.epam.bigdatalab.dao.connectionpool.ConnectionPool;
import by.epam.bigdatalab.dao.connectionpool.DBParameter;
import by.epam.bigdatalab.dao.connectionpool.DBResourceManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

public final class HikariConnectionPoolImpl implements ConnectionPool {

    private HikariDataSource hikariDataSource;

    @Override
    public void init() {

//        Properties props = new Properties();
//
//        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
//        props.setProperty(DBParameter.DATA_SOURCE_CLASS_NAME, dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
//        props.setProperty(DBParameter.DATA_SOURCE_USER, dbResourceManager.getValue(DBParameter.DB_USER));
//        props.setProperty(DBParameter.DATA_SOURCE_PASSWORD, dbResourceManager.getValue(DBParameter.DB_PASSWORD));
//        props.setProperty(DBParameter.DATA_SOURCE_DATABASE_NAME, dbResourceManager.getValue(DBParameter.DB_NAME));
//        props.setProperty("dataSource.portNumber", "5432");
//        props.setProperty("dataSource.serverName", "localhost");
//
//
//        HikariConfig config = new HikariConfig(props);
//        hikariDataSource = new HikariDataSource(config);

//
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/crimes");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setPassword("1234");



    }

    @Override
    public HikariDataSource getSource() {
        return hikariDataSource;
    }

    @Override
    public void dispose() {
        hikariDataSource.close();
    }



}
