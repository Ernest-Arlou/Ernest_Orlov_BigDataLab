package by.epam.bigdatalab;

import org.junit.Test;

import static by.epam.bigdatalab.Utils.isAllPositiveNumbers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UtilsTest
{
    private final String POSITIVE_NUMBER = "12";
    private final String POSITIVE_DOUBLE = "12.0";
    private final String NEGATIVE_NUMBER = "-12";
    private final String NEGATIVE_DOUBLE = "-12.0";
    private final String NOT_NUMBER = "-12asw";

    @Test
    public void positiveNumbers()
    {
        assertTrue( isAllPositiveNumbers(POSITIVE_NUMBER, POSITIVE_DOUBLE) );
    }

    @Test
    public void negativeNumbers()
    {
        assertFalse( isAllPositiveNumbers(NEGATIVE_NUMBER, NEGATIVE_DOUBLE) );
    }

    @Test
    public void notNumbers()
    {
        assertFalse( isAllPositiveNumbers(POSITIVE_NUMBER, NOT_NUMBER) );
        assertFalse( isAllPositiveNumbers(null) );
    }
}
