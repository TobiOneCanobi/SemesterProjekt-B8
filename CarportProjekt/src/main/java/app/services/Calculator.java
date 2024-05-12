package app.services;

import app.entities.Material;
import app.entities.Order;
import app.entities.OrderItem;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator
{

    private static final int POSTs = 1;
    private static final int Rafters = 2;
    private static final int beams = 2;

    private List<OrderItem> orderItems = new ArrayList<>();
    private int width;
    private int length;
    private ConnectionPool connectionPool;


    public Calculator(int width, int length, ConnectionPool connectionPool)
    {
        this.width = width;
        this.length = length;
        this.connectionPool = connectionPool;
    }

    public void calcCarport(Order order)
    {
        calcPosts(order);
        calcBeams(order);
        calcRafters(order);
    }


    // stolper
    private void calcPosts(Order order)
    {
        //antal stolper
        int quantity = calcPostQuantity();

        //længde på stolper "variant"
        //List <MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(0, Posts,connectionPool);
        //MaterialVariant materialVariant = materialVariants.get(0);
        //OrderItem orderItem = new OrderItem(0,order,materialVariant, quantity, Stolper nedgraves 90 cm. i jord);
        //orderItems.add(orderItems);

    }

    public int calcPostQuantity()
    {
        return 2 * (2 + (length -130)/ 340);
    }

    //remme
    private void calcBeams(Order order)
    {

    }

    //spær
    private void calcRafters(Order order)
    {

    }

    public List<OrderItem> getOrderItems()
    {
        return orderItems;
    }
}
