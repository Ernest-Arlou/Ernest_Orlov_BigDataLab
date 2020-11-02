package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.dao.DBParameter;
import by.epam.bigdatalab.dao.DBResourceManager;
import by.epam.bigdatalab.dao.PostgreSQLDAO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PostgreSQLDAOImpl implements PostgreSQLDAO {

    private void connectionPool(){
        Properties props = new Properties();
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        props.setProperty("dataSourceClassName",dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
        props.setProperty("dataSource.user", dbResourceManager.getValue(DBParameter.DB_USER));
        props.setProperty("dataSource.password", dbResourceManager.getValue(DBParameter.DB_PASSWORD));
        props.setProperty("dataSource.databaseName", dbResourceManager.getValue(DBParameter.DB_NAME));
//        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        HikariDataSource ds = new HikariDataSource(config);

        try {
            Connection connection = ds.getConnection();





        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
