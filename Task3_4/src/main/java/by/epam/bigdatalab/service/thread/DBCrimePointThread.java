package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class DBCrimePointThread implements Runnable {
    private final URL url;

    public DBCrimePointThread(URL url) {
        this.url = url;
    }

    @Override
    public void run() {
        List<Crime> crimeList = new LinkedList<>(Request.doRequest(url, Crime.class));
        DAOHolder.getInstance().getDataBaseDAO().saveCrimes(crimeList);
    }
}



