package by.epam.bigdatalab.service;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class URLManagerTest {

    @Test
    public void createURL() throws MalformedURLException {
        String str = "https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2018-01";
        URL url = new URL(str);

        Assert.assertEquals(url,URLManager.createURL(str));
    }
}