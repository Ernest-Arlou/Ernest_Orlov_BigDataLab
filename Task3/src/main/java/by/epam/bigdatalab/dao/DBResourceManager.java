package by.epam.bigdatalab.dao;


import java.util.ResourceBundle;

public class DBResourceManager {
    private static final String DB_PROPERTIES = "database";
    private final static DBResourceManager instance = new DBResourceManager();
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_PROPERTIES);

    public static DBResourceManager getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return resourceBundle.getString(key);
    }
}