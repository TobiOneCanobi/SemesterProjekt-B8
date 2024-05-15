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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
       // calcPosts(order);
        calcBeams(order);
        calcRafters(order);
    }


    // stolper
    private void calcPosts(Order order) throws DatabaseException
    {
        //antal stolper
        int quantity = calcPostQuantity();

        //længde på stolper "variant"
        List <MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(300, 1,connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);
        OrderItem orderItem = new OrderItem(0,order,materialVariant, quantity, "Stolper nedgraves 90 cm. i jord");
        orderItems.add(orderItem);

        // tester
        for (OrderItem orderItem1 : orderItems)
        {
            System.out.println(orderItem1.getMaterialVariant());
            System.out.println(orderItem1.getMaterialVariant().getMaterial());

        }
    }

    public int calcPostQuantity()
    {
        return 2 * (2 + (length -130)/ 340);
    }

    //remme
    private void calcBeams(Order order) throws DatabaseException
    {
        int quantity = calcBeamsQuantity();


        List <MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2,connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);

        String description = materialVariant.getMaterial().getName();
        width = extractWidth(description);

        OrderItem orderItem = new OrderItem(0,order,materialVariant, quantity, "Remme i sider, sadles ned i stolper");
        orderItems.add(orderItem);


        //test
        for (OrderItem orderItem1 : orderItems)
        {
            System.out.println(orderItem1.getMaterialVariant());
            System.out.println(orderItem1.getMaterialVariant().getMaterial());

        }
        System.out.println("Beams Width: " + width);  // Print the extracted beams width
    }

    public int calcBeamsQuantity()
    {
        return length/(width/10);
    }

    //spær
    private void calcRafters(Order order)
    {

    }

    private int extractWidth(String description) {
        // Use a regular expression to match the format "<number>x<number>"
        String regex = "\\d+x(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid material description format: " + description);
        }
    }

    public List<OrderItem> getOrderItems()
    {
        return orderItems;
    }
}
