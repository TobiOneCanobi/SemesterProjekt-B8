package app.services;

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

    public void calcCarport(Order order) throws DatabaseException, InterruptedException
    {

        System.out.println("Starting calcPosts");
        calcPosts(order);
        System.out.println("Finished calcPosts");

        System.out.println("Starting calcBeams");
        calcBeams(order);
        System.out.println("Finished calcBeams");

        System.out.println("Starting calcRafter");
        calcRafter(order);
        System.out.println("Finished calcRafter");

    }

    // stolper
    private void calcPosts(Order order) throws DatabaseException
    {

        //antal stolper
        int quantity = calcPostQuantity();

        //længde på stolper "variant"
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(300, 1, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, materialVariant, quantity, "Stolper nedgraves 90 cm. i jord");
        orderItems.add(orderItem);

        // tester
        System.out.println("stolper:");
        System.out.println(quantity);
        for (MaterialVariant materialVariant1 : materialVariants)
        {
            System.out.println(materialVariant1.toString());
        }
    }

    public int calcPostQuantity()
    {
        return 2 * (2 + (length - 130) / 340);
    }


    //remme
    private void calcBeams(Order order) throws DatabaseException
    {
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);


        int quantity = calcBeamQuantity();

        OrderItem orderItem = new OrderItem(0, order, materialVariant, quantity, "Remme i sider, sadles ned i stolper");
        orderItems.add(orderItem);

        //test
        System.out.println("remme:");
        System.out.println(quantity);
        for (MaterialVariant materialVariant1 : materialVariants)
        {
            System.out.println(materialVariant1.toString());
        }
    }

    public int calcBeamQuantity()
    {
        return 2;
    }


    //spær
    private void calcRafter(Order order) throws DatabaseException
    {
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);

        String description = materialVariant.getMaterial().getName();
        width = extractPartWidth(description);

        int quantity = calcRafterQuantity(length, width);

        OrderItem orderItem = new OrderItem(0, order, materialVariant, quantity, "Spær, monteres på rem");
        orderItems.add(orderItem);


        //test
        System.out.println("spær:");
        System.out.println(quantity);
        for (MaterialVariant materialVariant1 : materialVariants)
        {
            System.out.println(materialVariant1.toString());
        }
        System.out.println("Rafter Width: " + width);
    }

    public int calcRafterQuantity(int length, int width)
    {
        double widthInCm = width / 10.0; // Convert width from mm to cm
        double result = length / widthInCm;
        return (int) Math.ceil(result);
    }

    private int extractPartWidth(String description)
    {
        String regex = "\\d+x(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(description);
        if (matcher.find())
        {
            return Integer.parseInt(matcher.group(1));
        } else
        {
            throw new IllegalArgumentException("Invalid material description format: " + description);
        }
    }

    public List<OrderItem> getOrderItems()
    {
        return orderItems;
    }
}
