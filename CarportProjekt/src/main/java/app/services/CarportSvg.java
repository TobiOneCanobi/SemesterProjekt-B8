package app.services;

public class CarportSvg
{
    private int width;
    private int length;
    private Svg innerSvg;
    private Svg outerSvg;

    public CarportSvg(int width, int length)
    {
        this.width = width;
        this.length = length;
        outerSvg = new Svg(0, 0, "0 0 855 690", "75%");
        innerSvg = new Svg(40, 20, "0 0 " + (width + 200) + " " + (length + 200), "100%");
        innerSvg.addRectangle(0, 0, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPost();
//        addArrow();
//        addText();
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
        for (double i = 0; i < length; i += 55.714)
        {
            innerSvg.addRectangle(i, 0.0, width, 12, "stroke:#000000; fill: #ffffff");
        }
    }

    //Stolper
    private void addPost()
    {
        for (double i = 100; i <= length; i += 200)
        {
            innerSvg.addRectangle(i, width - 37.5, 10, 10, "stroke:#000000; fill: #000000");
        }

        for (double i = 100; i <= length; i += 200)
        {
            innerSvg.addRectangle(i, 32.5, 10, 10, "stroke:#000000; fill: #000000");
        }
    }

    public void addArrow()
    {
        outerSvg.addArrow(width - 90, 20, width - 90, width - 37, "stroke:#000000; fill: #000000");
        outerSvg.addArrow(40, length + 0, width + 0, length + 0, "stroke:#000000; fill: #000000");
    }

    public void addText()
    {
        outerSvg.addText(width, length, 90, "600");
        outerSvg.addText(width, length, 0, "780");
    }

    @Override
    public String toString()
    {
        return outerSvg.toString();
    }
}