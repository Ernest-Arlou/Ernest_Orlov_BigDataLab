package by.epam.bigdatalab;


import static by.epam.bigdatalab.StringUtils.isPositiveNumber;

public final class Utils {
    public static boolean isAllPositiveNumbers(String... strings) {
        if (strings == null) {
            return false;
        }
        for (String str : strings) {
            if (!isPositiveNumber(str)) {
                return false;
            }
        }
        return true;
    }
}
