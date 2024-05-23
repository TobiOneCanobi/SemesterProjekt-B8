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
        return 2 * (2 + (length - 130) / 340);
    }

    //remme
    private void calcBeams(Order order) throws DatabaseException
    {
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);

        int quantity = calcBeamQuantity(length);

        OrderItem orderItem = new OrderItem(0, order, materialVariant, quantity, "Remme i sider, sadles ned i stolper");
        orderItems.add(orderItem);
    }

    public int calcBeamQuantity(int length)
    {
        if (length <= 600)
        {
            return 2;
        } else
        {
            return 4;
        }
    }

    //spær
    private void calcRafter(Order order) throws DatabaseException
    {
        double rafterWidth;
        List<MaterialVariant> materialVariants = MaterialMapper.getVariantsByProductIdAndMinLength(length, 2, connectionPool);
        MaterialVariant materialVariant = materialVariants.get(0);

        String description = materialVariant.getMaterial().getName();
        //extract width of material from description and convert it from mm to cm
        rafterWidth = (double) extractPartWidth(description) / 10;
        // get the same distance between all the rafters
        double widthBetweenRafters = calculateOptimalRafterSpaceWidth(length, rafterWidth);
        // calc the quantity of rafters
        int rafterQuantity = calculateNumberOfRafters(length, widthBetweenRafters, rafterWidth);

        OrderItem orderItem = new OrderItem(0, order, materialVariant, rafterQuantity, "Spær, monteres på rem");
        orderItems.add(orderItem);
    }

    public double calculateOptimalRafterSpaceWidth(int totalLength, double rafterWidth)
    {
        int minSpacing = 45;
        int maxSpacing = 60;

        int maxRafters = (int) ((totalLength + minSpacing) / (rafterWidth + minSpacing));
        int minRafters = (int) ((totalLength + maxSpacing) / (rafterWidth + maxSpacing));

        for (int n = minRafters; n <= maxRafters; n++)
        {
            double totalRafterWidth = n * rafterWidth;
            int numberOfSpaces = n - 1;
            double spaceWidth = (totalLength - totalRafterWidth) / numberOfSpaces;

            if (spaceWidth >= minSpacing && spaceWidth <= maxSpacing)
            {
                return spaceWidth;
            }
        }
        // Return an invalid value if no solution is found
        return -1;
    }

    public int calculateNumberOfRafters(int totalLength, double optimalSpaceWidth, double rafterWidth)
    {
        if (optimalSpaceWidth == -1)
        {
            // Return 0 if the space width is invalid
            return 0;
        }
        return (int) ((totalLength + optimalSpaceWidth) / (rafterWidth + optimalSpaceWidth));
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
