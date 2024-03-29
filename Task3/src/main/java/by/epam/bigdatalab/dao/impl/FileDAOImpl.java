package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.dao.FileDAO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileDAOImpl implements FileDAO {
    private static final Logger logger = LoggerFactory.getLogger(FileDAOImpl.class);
    private static volatile boolean firstFlag = true;

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
    public synchronized <T> void write(String path, List<T> objects) {
        String out = JSON.toJSONString(objects);

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
