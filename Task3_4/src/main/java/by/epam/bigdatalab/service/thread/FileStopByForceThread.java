package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FileStopByForceThread implements Runnable {
    private final URL url;
    private final String path;

    public FileStopByForceThread(URL url, String path) {
        this.url = url;
        this.path = path;
    }

    @Override
    public void run() {
        List<StopAndSearch> stopAndSearches = new LinkedList<>(Request.doRequest(url, StopAndSearch.class));
        DAOHolder.getInstance().getFileDAO().write(path, stopAndSearches);
    }
}