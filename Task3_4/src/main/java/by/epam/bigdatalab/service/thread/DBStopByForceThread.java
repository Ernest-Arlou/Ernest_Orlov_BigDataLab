package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DBStopByForceThread implements Runnable {
    private final Queue<String> urls;

    public DBStopByForceThread(Queue<String> urls) {
        this.urls = urls;
    }

    @Override
    public void run() {
        List<StopAndSearch> stopAndSearches = new LinkedList<>(Request.doRequest(urls, StopAndSearch.class));
        DAOHolder.getInstance().getDataBaseDAO().saveStopAndSearches(stopAndSearches);
    }
}