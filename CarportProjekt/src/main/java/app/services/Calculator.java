package app.services;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.entities.Order;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator
{

    private static final int Posts = 1;
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

    public void calcCarport(Order order) throws DatabaseException
    {
        calcPosts(order);
        calcBeams(order);
        calcRafters(order);
    }


    // stolper
    private void calcPosts(Order order) throws DatabaseException
    {
        //antal stolper
        int quantity = calcPostQuantity();

        //længde på stolper "variant"
         List <MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(0, 1,connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);
        OrderItem orderItem = new OrderItem(0,order,materialVariant, quantity, "Stolper nedgraves 90 cm. i jord");
        orderItems.add(orderItem);

        // tester
        for (OrderItem orderItem1 : orderItems)
        {
            System.out.println(orderItem1.getMaterialVariant());
        }

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
