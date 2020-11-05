package by.epam.bigdatalab.dao.factory;

import by.epam.bigdatalab.dao.DataBaseDAO;
import by.epam.bigdatalab.dao.FileDAO;
import by.epam.bigdatalab.dao.impl.FileDAOImpl;
import by.epam.bigdatalab.dao.impl.PostgreSQLDAOImpl;


public final class DAOFactory {
    private static volatile DAOFactory instance;

    private final FileDAO fileDAO = new FileDAOImpl();
    private final DataBaseDAO dataBaseDAO = new PostgreSQLDAOImpl();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {

        DAOFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (DAOFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DAOFactory();
                }
            }
        }
        return localInstance;
    }


    public FileDAO getFileDAO() {
        return fileDAO;
    }

    public DataBaseDAO getDataBaseDAO() {
        return dataBaseDAO;
    }
}