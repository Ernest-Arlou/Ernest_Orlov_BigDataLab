package by.epam.bigdatalab;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private Util() {
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
