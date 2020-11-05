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

        Properties props = new Properties();

        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        props.setProperty(DBParameter.DATA_SOURCE_CLASS_NAME, dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
        props.setProperty(DBParameter.DATA_SOURCE_USER, dbResourceManager.getValue(DBParameter.DB_USER));
        props.setProperty(DBParameter.DATA_SOURCE_PASSWORD, dbResourceManager.getValue(DBParameter.DB_PASSWORD));
        props.setProperty(DBParameter.DATA_SOURCE_DATABASE_NAME, dbResourceManager.getValue(DBParameter.DB_NAME));

        HikariConfig config = new HikariConfig(props);
        hikariDataSource = new HikariDataSource(config);

    }

    @Override
    public HikariDataSource getSource() {
        return hikariDataSource;
    }

    @Override
    public void dispose() {
        hikariDataSource.close();
    }

//    public Connection getConnection(){
//        Connection connection = null;
//        try {
//            connection = hikariDataSource.getConnection();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return connection;
//    }
//
//    public void closeConnection(Connection connection){
//        hikariDataSource.evictConnection(connection);
//    }


}
