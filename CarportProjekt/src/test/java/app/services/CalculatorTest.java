package app.services;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest
{
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:postgresql://159.65.120.138:5432/%s?currentSchema=public";
    private static final String DB = "carport";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeAll
    static void setup()
    {

    }
    @Test
    void calcPostQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600,600, connectionPool);
        //expected
        int expected = 6;
        int unexpected = 4;
        //actual
        int actual = calculator.calcPostQuantity();
        //assert
        assertEquals(expected,actual);
        assertNotEquals(unexpected,actual);
    }

    @Test
    void calcBeamsQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600,600, connectionPool);
        //expected
        int expected = 2;
        int unexpected = 4;
        //actual
        int actual = calculator.calcBeamQuantity();
        //assert
        assertEquals(expected,actual);
        assertNotEquals(unexpected,actual);
    }
    @Test
    void calcRafterQuantity()
    {
        //setup
        Calculator calculator = new Calculator(600,600, connectionPool);
        //expected
        int expected = 11;
        int unexpected = 12;
        //actual
        int actual = (int) calculator.calcOptimalSpaceWidthAndQuantity(600);
        //assert
        assertEquals(expected,actual);
        assertNotEquals(unexpected,actual);
    }
    @Test
    void extractPartWidth()
    {
        //setup
        Calculator calculator = new Calculator(600,600, connectionPool);
        //expected
        int expected = 45;
        int unexpected = 40;
        //actual
        int actual = calculator.extractPartWidth("45x195 mm. spærtræ ubh.");
        //assert
        assertEquals(expected,actual);
        assertNotEquals(unexpected,actual);
    }

}