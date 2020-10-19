package by.epam.bigdatalab;

import static by.epam.bigdatalab.Utils.isAllPositiveNumbers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class UtilsTest
{

    @Test
    public void positiveNumbers()
    {
        assertTrue( isAllPositiveNumbers("1", "2") );
    }

    @Test
    public void negativeNumbers()
    {
        assertFalse( isAllPositiveNumbers("-1", "-2") );
    }

    @Test
    public void notNumbers()
    {
        assertFalse( isAllPositiveNumbers("-1as", "-2") );
    }
}