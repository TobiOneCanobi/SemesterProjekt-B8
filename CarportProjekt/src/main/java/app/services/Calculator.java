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
        calcPosts(order);
        calcBeams(order);
        calcRafter(order);

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

    }

    public int calcPostQuantity()
    {
        int maxDist;
        int firstPost;
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
    }

    public int calcBeamQuantity()
    {
        return 2;
    }


    //spær
    private void calcRafter(Order order) throws DatabaseException
    {
        double rafterWidth;
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);

        String description = materialVariant.getMaterial().getName();
        // extract width from description
        rafterWidth = extractPartWidth(description);

        int optimalNumberOfRafters = (int) calcOptimalSpaceWidthAndQuantity(length);
        //int quantity = calcRafterQuantity(length, optimalSpaceWidth);

        OrderItem orderItem = new OrderItem(0, order, materialVariant, optimalNumberOfRafters, "Spær, monteres på rem");
        orderItems.add(orderItem);

    }

    public double calcOptimalSpaceWidthAndQuantity(int totalLength)
    {
        double rafterWidth = 4.5;
        int minSpacing = 45;
        int maxSpacing = 60;

        // beregner en min og max antal spær
        int maxRafters = (int) ((totalLength + minSpacing) / (rafterWidth + minSpacing));
        int minRafters = (int) ((totalLength + maxSpacing) / (rafterWidth + maxSpacing));

        double optimalSpaceWidth = 0;
        int optimalNumberOfRafters = 0;

        // loop igennem for at finde en løsning
        for (int n = minRafters; n <= maxRafters; n++)
        {
            double totalRafterWidth = n * rafterWidth;
            int numberOfSpaces = n - 1;
            double spaceWidth = (totalLength - totalRafterWidth) / numberOfSpaces;

            //tjek om den beregende afstand overholder min og max afstand
            if (spaceWidth >= minSpacing && spaceWidth <= maxSpacing)
            {
                optimalSpaceWidth = spaceWidth;
                optimalNumberOfRafters = n;
                break;
            }
        }

        return optimalNumberOfRafters;
    }

    public int extractPartWidth(String description)
    {
        String regex = "(\\d+)x\\d+";
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
