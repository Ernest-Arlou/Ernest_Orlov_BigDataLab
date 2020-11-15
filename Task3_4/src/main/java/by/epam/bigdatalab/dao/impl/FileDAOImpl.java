package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.Point;
import by.epam.bigdatalab.dao.FileDAO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl implements FileDAO {
    private static final String CSV_SPLITTER = ",";
    private static final String CSV_HEADER = "name,longitude,latitude";

    private static final Logger logger = LoggerFactory.getLogger(FileDAOImpl.class);

    private static volatile boolean firstFlag = true;


    @Override
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

    @Override
    public void startWritingIn(String path) {
        logger.debug("File path = {}", path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("[");

        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void endWritingIn(String path) {
        logger.debug("File path = {}", path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write("]");

        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public synchronized void write(String path, List<Crime> crimes) {
        String out = JSON.toJSONString(crimes);

//        String out = "[{\"category\":\"anti-social-behaviour\",\"location_type\":\"Force\",\"location\":{\"latitude\":\"52.635184\",\"street\":{\"id\":883410,\"name\":\"On or near Shopping Area\"},\"longitude\":\"-1.135455\"},\"context\":\"\",\"outcome_status\":null,\"persistent_id\":\"\",\"id\":62407437,\"location_subtype\":\"\",\"month\":\"2018-01\"}]";

        out = out.substring(1, out.length() - 1);

        logger.debug("File path = {}", path);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            if (firstFlag) {
                writer.write(out);
                firstFlag = false;
            } else {
                writer.write("," + out);
            }


        } catch (IOException e) {
            logger.error(e.toString());
        }

    }


}
