package app.services;

public class CarportSvg
{
    private int width;
    private int length;
    private Svg innerSvg;
    private Svg outerSvg;

    // Variabler for de første og sidste stolpers positioner
    private double firstTopPostX;
    private double firstTopPostY;
    private double lastBottomPostX;
    private double lastBottomPostY;

    private double firstBottomPostX;
    private double firstBottomPostY;
    private double lastTopPostX;
    private double lastTopPostY;

    public CarportSvg(int width, int length)
    {
        this.width = width;
        this.length = length;
        outerSvg = new Svg(0, 0, "0 0 700 700", "100%");
        innerSvg = new Svg(0, 0, "0 0" + (width + 200) + " " + (length + 200), "100%");
        innerSvg.addRectangle(0, 0, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPosts();
        addDashLineTop();
        addDashLineBottom();
        addArrow();
        addText();
        outerSvg.addSvg(innerSvg);
    }

    //Bjælker
    private void addBeams()
    {
        innerSvg.addRectangle(0, 35, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        innerSvg.addRectangle(0, width - 35, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    //Spær
    private void addRafters()
    {
        for (double i = 0; i < length; i += 55.0)
        {
            innerSvg.addRectangle(i, 0.0, width, 4.5, "stroke:#000000; fill: #ffffff");
            i += 4.5;
        }
    }

    //Stolper
    private void addPosts()
    {
        boolean isFirstTopPost = true;
        boolean isFirstBottomPost = true;
        for (double i = 100; i <= length; i += 200)
        {
            double x1 = i;
            double y1 = width - 37.5;
            innerSvg.addRectangle(x1, y1, 10, 10, "stroke:#000000; fill: #000000");

            double x2 = i;
            double y2 = 32.5;
            innerSvg.addRectangle(x2, y2, 10, 10, "stroke:#000000; fill: #000000");

            // Gemmer koordinaterne for den første topstolpe
            if (isFirstTopPost)
            {
                firstTopPostX = x2;
                firstTopPostY = y2;
                isFirstTopPost = false;
            }

            //Gemmer koordinaterne for den første bundstolpe
            if (isFirstBottomPost)
            {
                firstBottomPostX = x1;
                firstBottomPostY = y1;
                isFirstBottomPost = false;
            }

            // Opdater koordinaterne for den sidste bundstolpe
            lastBottomPostX = x1;
            lastBottomPostY = y1;

            // Opdater koordinaterne for den sidste topstolpe
            lastTopPostX = x2;
            lastTopPostY = y2;
        }
    }

    public void addArrow()
    {
        //Pil til højre
        outerSvg.addArrow(length + 20, 0, length + 20, width, "stroke:#000000; fill: #000000");

        //Pil ned
        outerSvg.addArrow(0, width + 20, length, width + 20, "stroke:#000000; fill: #000000");
    }

    public void addText()
    {
        //Tekst til højre
        outerSvg.addText(length + 25, width / 2, 90, String.valueOf(width) + " cm");

        //Tekst ned
        outerSvg.addText(length / 2, width + 35, 0, String.valueOf(length) + " cm");
    }

    public void addDashLineTop()
    {
        innerSvg.addDashLine(firstTopPostX, firstTopPostY, lastBottomPostX, lastBottomPostY, "stroke:#000000; stroke-dasharray: 5 5");
    }

    public void addDashLineBottom()
    {
        innerSvg.addDashLine(firstBottomPostX, firstBottomPostY, lastTopPostX, lastTopPostY, "stroke:#000000; stroke-dasharray: 5 5");
    }

    @Override
    public String toString()
    {
        return outerSvg.toString();
    }
}