package by.epam.bigdatalab.service.thread;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.DAOHolder;
import by.epam.bigdatalab.service.Request;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FileCrimePointThread implements Runnable {
    private final URL url;
    private final String path;

    public FileCrimePointThread(URL url, String path) {
        this.url = url;
        this.path = path;
    }

    @Override
    public void run() {
        List<Crime> crimeList = new LinkedList<>(Request.doRequest(url, Crime.class));
//        System.out.println(Thread.currentThread().getName() + "STARTED");
        DAOHolder.getInstance().getFileDAO().write(path, crimeList);
//        System.out.println(Thread.currentThread().getName() + "ENDED");
    }
}