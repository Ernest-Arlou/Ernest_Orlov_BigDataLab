package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileCrimePointThread implements Runnable {
    private final String path;
    private final Queue<String> urls;


    public FileCrimePointThread(Queue<String> urls, String path) {
        this.urls = urls;
        this.path = path;
    }

    @Override
    public void run() {
        List<Crime> crimeList = new LinkedList<>(Request.doRequest(urls, Crime.class));
        DAOHolder.getInstance().getFileDAO().write(path, crimeList);
    }
}