package by.epam.bigdatalab;


public final class StringUtils {

    public static boolean isPositiveNumber(String str) {
        if (org.apache.commons.lang3.math.NumberUtils.isParsable(str)) {
            double number = org.apache.commons.lang3.math.NumberUtils.createDouble(str);
            return number >= 0;
        }
        return false;
    }
}
