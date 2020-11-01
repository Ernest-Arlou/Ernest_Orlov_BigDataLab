package by.epam.bigdatalab.dao.factory;

import by.epam.bigdatalab.dao.FileDAO;
import by.epam.bigdatalab.dao.impl.FileDAOImpl;


public class DAOFactory {
    private static volatile DAOFactory instance;

    private final FileDAO fileDAO = new FileDAOImpl();

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
}