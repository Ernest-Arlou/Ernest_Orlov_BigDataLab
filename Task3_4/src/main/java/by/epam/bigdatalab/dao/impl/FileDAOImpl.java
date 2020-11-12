package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOException;
import by.epam.bigdatalab.dao.FileDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl implements FileDAO {
    private static final String CSV_SPLITTER = ",";
    private static final String CSV_HEADER = "name,longitude,latitude";

    private static final Logger logger = LoggerFactory.getLogger(FileDAOImpl.class);


    @Override
    public List<Point> getPoints(String path) throws DAOException {

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
            logger.error("IOException in FileDAOImpl method getPoints()", e);
            throw new DAOException("Problem with file", e);
        }

        return points;
    }


    @Override
    public void saveCrimes(String crimes, String path) throws DAOException {

        logger.debug("File path = {}", path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(crimes);

        } catch (IOException e) {
            logger.error("IOException in FileDAOImpl method saveCrimes()", e);
            throw new DAOException("Problem with file", e);
        }


    }


}
