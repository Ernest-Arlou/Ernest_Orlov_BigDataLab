package by.epam.bigdatalab.service;

import by.epam.bigdatalab.bean.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PointsReader {
    private static final String CSV_SPLITTER = ",";
    private static final String CSV_HEADER = "name,longitude,latitude";

    private static final Logger logger = LoggerFactory.getLogger(PointsReader.class);

    public List<Point> getPoints(String path) {

        List<Point> points = new ArrayList<>();

        String line;

        logger.debug("File path = {}", path);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            while ((line = br.readLine()) != null) {

                if (!line.equals(CSV_HEADER)) {
                    String[] pointMas = line.split(CSV_SPLITTER);
                    logger.debug("longitude = {}", pointMas[1]);
                    logger.debug("latitude = {}", pointMas[2]);
                    double longitude = Double.parseDouble(pointMas[1]);
                    double latitude = Double.parseDouble(pointMas[2]);

                    points.add(new Point(longitude, latitude));
                }
            }
        } catch (IOException | NumberFormatException e) {
            logger.error(e.toString());

        }

        return points;
    }
}
