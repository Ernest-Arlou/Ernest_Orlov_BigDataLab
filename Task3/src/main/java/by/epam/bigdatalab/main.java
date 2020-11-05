package by.epam.bigdatalab;


import by.epam.bigdatalab.bean.CrimeLocation;
import by.epam.bigdatalab.bean.CrimeLocationStreet;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPool;
import by.epam.bigdatalab.dao.connectionpool.factory.ConnectionPoolFactory;
import by.epam.bigdatalab.dao.factory.DAOFactory;
import by.epam.bigdatalab.service.factory.ServiceFactory;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;

public class main {


    public static void main(String[] args) throws Exception {



        ConnectionPoolFactory.getInstance().getConnectionPool().init();

        ServiceFactory.getInstance().getPoliceAPIService().test();

//        DAOFactory.getInstance().getDataBaseDAO().saveCrimes(null);
//


//        DataSource dataSource = ConnectionPoolFactory.getInstance().getConnectionPool().getSource();
//        FluentJdbc fluentJdbc = new FluentJdbcBuilder()
//                .connectionProvider(dataSource)
//                .build();
//        Query query = fluentJdbc.query();


//
//        List<CrimeLocationStreet> customers = query.select("SELECT * FROM \"Street\"")
//                .listResult(resultSet -> new CrimeLocationStreet(resultSet.getInt("id"),resultSet.getString("name")));
//
//        System.out.println(customers);


//        query.update("INSERT INTO \"Street\"(id, name) values(?,?)")
//                .params(5,"name5")
//        .run();


        ConnectionPoolFactory.getInstance().getConnectionPool().dispose();








//        Properties props = new Properties();
//        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
//        props.setProperty("dataSourceClassName",dbResourceManager.getValue(DBParameter.DB_DATA_SOURCE_CLASS_NAME));
//        props.setProperty("dataSource.user", dbResourceManager.getValue(DBParameter.DB_USER));
//        props.setProperty("dataSource.password", dbResourceManager.getValue(DBParameter.DB_PASSWORD));
//        props.setProperty("dataSource.databaseName", dbResourceManager.getValue(DBParameter.DB_NAME));
////        props.put("dataSource.logWriter", new PrintWriter(System.out));
//
//        HikariConfig config = new HikariConfig(props);
//        HikariDataSource ds = new HikariDataSource(config);
//
//
////        HikariDataSource ds = new HikariDataSource();
////        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/crimes");
////        ds.setUsername("postgres");
////        ds.setPassword("1234");
////
//        try {
//            Connection connection = ds.getConnection();
//
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.test");
//
//            while (resultSet.next()) {
//
//                System.out.println(resultSet.getInt("id"));
//                System.out.println(resultSet.getString("name"));
//
//            }
//
//
//
//            System.out.println(connection.getClientInfo());
//
//
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }


//        ServiceFactory.getInstance().getPoliceAPIService().test("E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv");




        //        String urlStr = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2019-01";


//        List<Crime> crimes = Arrays.asList(doRequest("/crimes-street/all-crime", Crime[].class));


//        System.out.println(buildParameters());
    }







}
