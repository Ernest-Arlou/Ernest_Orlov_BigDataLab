package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class DBCrimePointThread implements Runnable {
    private final Queue<String> urls;

    public DBCrimePointThread(Queue<String> urls) {
        this.urls = urls;
    }

    @Override
    public void run() {
        List<Crime> crimeList = new LinkedList<>(Request.doRequest(urls, Crime.class));
        DAOHolder.getInstance().getDataBaseDAO().saveCrimes(crimeList);
    }
}



