package by.epam.bigdatalab.dao.connectionpool.impl;

import by.epam.bigdatalab.dao.DBParameter;
import by.epam.bigdatalab.dao.DBResourceManager;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolImpl implements ConnectionPool {

    private void connectionPool(){
        Properties props = new Properties();
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        props.setProperty("dataSourceClassName",dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
        props.setProperty("dataSource.user", dbResourceManager.getValue(DBParameter.DB_USER));
        props.setProperty("dataSource.password", dbResourceManager.getValue(DBParameter.DB_PASSWORD));
        props.setProperty("dataSource.databaseName", dbResourceManager.getValue(DBParameter.DB_NAME));


        HikariConfig config = new HikariConfig(props);
        HikariDataSource ds = new HikariDataSource(config);

        try {
            Connection connection = ds.getConnection();






        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

}
