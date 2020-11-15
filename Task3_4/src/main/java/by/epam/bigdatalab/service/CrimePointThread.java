package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Crime;

import java.net.URL;
import java.util.List;
import java.util.Set;


public class CrimePointThread implements Runnable {

    private final Set<Crime> set;
    private final URL url;


    public CrimePointThread(URL url, Set<Crime> set) {
        this.url = url;
        this.set = set;

    }

    @Override
    public void run() {
        List<Crime> crimeList = Request.doRequest(url, Crime.class);
        if (crimeList != null) {
            set.addAll(crimeList);
        }
    }
}



