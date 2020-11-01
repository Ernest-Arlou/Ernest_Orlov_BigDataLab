package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.FileException;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.DAOException;
import by.epam.bigdatalab.dao.FileDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl implements FileDAO {
    private static final String CSV_SPLITTER =  ",";
    private static final String CSV_HEADER =  "name,longitude,latitude";


    @Override
    public List<Point> getPoints(String path) throws DAOException, FileException {

        List<Point> points = new ArrayList<>();

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            while ((line = br.readLine()) != null) {

                if (!line.equals(CSV_HEADER)) {
                    String[] pointMas = line.split(CSV_SPLITTER);
                    double longitude = Double.parseDouble(pointMas[1]);
                    double latitude = Double.parseDouble(pointMas[2]);

                    points.add(new Point(longitude, latitude));
                }
            }
        } catch (IOException e) {
           throw new DAOException("Problem with file",e);
        }

        return points;
    }


}
