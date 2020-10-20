package by.epam.bigdatalab;


import org.junit.Test;

import static by.epam.bigdatalab.Utils.isAllPositiveNumbers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AppTest {
    private final String POSITIVE_NUMBER = "12";
    private final String POSITIVE_DOUBLE = "12.0";
    private final String NEGATIVE_NUMBER = "-12";
    private final String NEGATIVE_DOUBLE = "-12.0";
    private final String NOT_NUMBER = "-12asw";

    @Test
    public void PositiveNumber() {
        assertTrue(isAllPositiveNumbers(POSITIVE_NUMBER));
        assertTrue(isAllPositiveNumbers(POSITIVE_DOUBLE));

    }

    @Test
    public void NegativeNumber() {
        assertFalse(isAllPositiveNumbers(NEGATIVE_NUMBER));
        assertFalse(isAllPositiveNumbers(NEGATIVE_DOUBLE));
        assertFalse(isAllPositiveNumbers(NOT_NUMBER));
    }

    @Test
    public void NotNumber() {
        assertFalse(isAllPositiveNumbers(NOT_NUMBER));
        assertFalse(isAllPositiveNumbers( null));
    }

}
