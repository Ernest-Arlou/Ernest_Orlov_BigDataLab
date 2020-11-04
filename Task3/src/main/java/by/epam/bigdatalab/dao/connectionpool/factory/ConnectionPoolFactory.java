package by.epam.bigdatalab.dao.connectionpool.factory;

import by.epam.bigdatalab.dao.connectionpool.ConnectionPool;
import by.epam.bigdatalab.dao.connectionpool.impl.HikariConnectionPoolImpl;


public final class ConnectionPoolFactory {
    private static volatile ConnectionPoolFactory instance;

    private final ConnectionPool connectionPool = new HikariConnectionPoolImpl();

    private ConnectionPoolFactory() {
    }

    public static ConnectionPoolFactory getInstance() {

        ConnectionPoolFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (ConnectionPoolFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConnectionPoolFactory();
                }
            }
        }
        return localInstance;
    }


    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
