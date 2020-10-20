package by.epam.bigdatalab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest
{
    private final String POSITIVE_NUMBER = "12";
    private final String POSITIVE_DOUBLE = "12.0";
    private final String NEGATIVE_NUMBER = "-12";
    private final String NEGATIVE_DOUBLE = "-12.0";
    private final String NOT_NUMBER = "-12asw";



    @Test
    public void PositiveNumber() {
        assertTrue(StringUtils.isPositiveNumber(POSITIVE_NUMBER));
        assertTrue(StringUtils.isPositiveNumber(POSITIVE_DOUBLE));

    }


    @Test
    public void NegativeNumber() {
        assertFalse(StringUtils.isPositiveNumber(NEGATIVE_NUMBER));
        assertFalse(StringUtils.isPositiveNumber(NEGATIVE_DOUBLE));
        assertFalse(StringUtils.isPositiveNumber(NOT_NUMBER));
    }

    @Test
    public void NotNumber() {
        assertFalse(StringUtils.isPositiveNumber(NOT_NUMBER));
        assertFalse(StringUtils.isPositiveNumber(null));
    }
}
