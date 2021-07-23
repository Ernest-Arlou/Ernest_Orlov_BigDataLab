package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileStopByForceThread implements Runnable {
    private final String path;
    private final Queue<String> urls;

    public FileStopByForceThread(Queue<String> urls, String path) {
        this.urls = urls;
        this.path = path;
    }

    @Override
    public void run() {
        List<StopAndSearch> stopAndSearches = new LinkedList<>(Request.doRequest(urls, StopAndSearch.class));
        DAOHolder.getInstance().getFileDAO().write(path, stopAndSearches);
    }
}