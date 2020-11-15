package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.dao.impl.FileDAOImpl;
import by.epam.bigdatalab.dao.impl.PostgreSQLDAOImpl;


public final class DAOHolder {

    private static final DataBaseDAO dataBaseDAO = new PostgreSQLDAOImpl();
    private static final FileDAO fileDAO = new FileDAOImpl();

    private DAOHolder() {
    }

    public static DAOHolder getInstance() {
        return DAOHolder.SingletonHolder.HOLDER_INSTANCE;
    }


    public DataBaseDAO getDataBaseDAO() {
        return dataBaseDAO;
    }

    public FileDAO getFileDAO() {
        return fileDAO;
    }


    public static class SingletonHolder {
        public static DAOHolder HOLDER_INSTANCE = new DAOHolder();
    }

}