package app.services;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest
{
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:postgresql://159.65.120.138:5432/%s?currentSchema=public";
    private static final String DB = "carport";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @Test
    void calcPostQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600, 600, connectionPool);
        //expected
        int expected = 6;
        int unexpected = 4;
        //actual
        int actual = calculator.calcPostQuantity();
        //assert
        assertEquals(expected, actual);
        assertNotEquals(unexpected, actual);
    }

    @Test
    void calcBeamsQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600, 600, connectionPool);
        //expected
        int expected = 2;
        int expected2 = 4;
        int unexpected = 4;
        //actual
        int actual = calculator.calcBeamQuantity(600);
        int actual2 = calculator.calcBeamQuantity(800);
        //assert
        assertEquals(expected, actual);
        assertNotEquals(unexpected, actual);
        assertEquals(expected2, actual2);
    }

    @Test
    void calculateOptimalRafterSpaceWidth()
    {
        //setup
        Calculator calculator = new Calculator(600, 600, connectionPool);
        //expected
        double expected = 55.05;
        double unexpected = 55;
        //actual
        double actual = calculator.calculateOptimalRafterSpaceWidth(600, 4.5);
        //assert
        assertEquals(expected, actual);
        assertNotEquals(unexpected, actual);
    }

    @Test
    void calcRafterQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600, 600, connectionPool);
        //expected
        int expected = 11;
        int unexpected = 12;
        //actual
        int actual = (int) calculator.calculateNumberOfRafters(600, 55.05, 4.5);
        //assert
        assertEquals(expected, actual);
        assertNotEquals(unexpected, actual);
    }

    @Test
    void extractPartWidth()
    {
        //setup
        Calculator calculator = new Calculator(600, 600, connectionPool);
        //expected
        int expected = 45;
        int unexpected = 40;
        //actual
        int actual = calculator.extractPartWidth("45x195 mm. spærtræ ubh.");
        //assert
        assertEquals(expected, actual);
        assertNotEquals(unexpected, actual);
    }
}