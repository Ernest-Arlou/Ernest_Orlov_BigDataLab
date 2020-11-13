package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Crime;

import java.net.URL;
import java.util.List;
import java.util.Set;


public class CrimePointRequest implements Runnable {

    private final Set<Crime> set;
    private final URL url;


    public CrimePointRequest(URL url, Set<Crime> set) {
        this.url = url;
        this.set = set;

    }

    @Override
    public void run() {
        List<Crime> crimeList = Request.doRequest(url, Crime.class);
        System.out.println(crimeList.size());
        if (crimeList != null) {
            set.addAll(crimeList);
        }
    }
}



