package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DBStopByForceThread implements Runnable {
    private final URL url;

    public DBStopByForceThread(URL url) {
        this.url = url;
    }

    @Override
    public void run() {
        List<StopAndSearch> stopAndSearches = new LinkedList<>(Request.doRequest(url, StopAndSearch.class));
        DAOHolder.getInstance().getDataBaseDAO().saveStopAndSearches(stopAndSearches);
    }
}