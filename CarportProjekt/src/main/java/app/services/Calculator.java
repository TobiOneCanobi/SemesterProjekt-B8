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

        int optimalNumberOfRafters = (int) calculateOptimalSpaceWidth(length);
        //int quantity = calcRafterQuantity(length, optimalSpaceWidth);

        OrderItem orderItem = new OrderItem(0, order, materialVariant, optimalNumberOfRafters, "Spær, monteres på rem");
        orderItems.add(orderItem);

    }

    public double calculateOptimalSpaceWidth(int totalLength)
    {
        double rafterWidth = 4.5; // Width of each rafter in cm
        int minSpacing = 45; // Minimum space between rafters in cm
        int maxSpacing = 60; // Maximum space between rafters in cm

        // Determine maximum and minimum number of rafters possible
        int maxRafters = (int) ((totalLength + minSpacing) / (rafterWidth + minSpacing));
        int minRafters = (int) ((totalLength + maxSpacing) / (rafterWidth + maxSpacing));

        double optimalSpaceWidth = 0;
        int optimalNumberOfRafters = 0;

        // Iterate through possible number of rafters to find the optimal configuration
        for (int n = minRafters; n <= maxRafters; n++)
        {
            double totalRafterWidth = n * rafterWidth;
            int numberOfSpaces = n - 1;
            double spaceWidth = (totalLength - totalRafterWidth) / numberOfSpaces;

            // Check if the calculated space width is within the allowed range
            if (spaceWidth >= minSpacing && spaceWidth <= maxSpacing)
            {
                optimalSpaceWidth = spaceWidth;
                optimalNumberOfRafters = n;
                break; // Exit loop if a valid configuration is found
            }
        }

        return optimalNumberOfRafters;
    }

    /*public int calcRafterQuantity(int length, double optimalSpaceWidth) {
        int rafterQuantity = 0;

        double rafterWidthInCm = 45 / 10.0; // Convert width from mm to cm
        double availableLength = length - (2 * rafterWidthInCm); // Subtract the width of two end rafters
        double distanceBetweenRafters;

         Calculate the number of gaps between rafters
        int numberOfGaps = (int) Math.floor(availableLength / (55 + rafterWidthInCm));

         Calculate the dynamic distance between rafters


         Ensure we start and end with a rafter
        for (double currentLength = rafterWidthInCm + optimalSpaceWidth;
             currentLength + rafterWidthInCm <= length - rafterWidthInCm;
             currentLength += optimalSpaceWidth + rafterWidthInCm) {
            rafterQuantity++;
        }

        double lastRafterPosition = rafterWidthInCm + (rafterQuantity - 1) * (optimalSpaceWidth + rafterWidthInCm);
        if (length - lastRafterPosition >= rafterWidthInCm) {
            rafterQuantity++;
        }

        for (double i = 0; i < length; i += optimalSpaceWidth)
        {
            ++rafterQuantity;
            i+= rafterWidthInCm;

        }

        System.out.println("dist: " + optimalSpaceWidth);
        System.out.println("quantity: " + rafterQuantity);
        return rafterQuantity + 1; // Add 1 to include the final rafter at the end
    }*/

    private int extractPartWidth(String description)
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
