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
        outerSvg = new Svg(0, 0, "0 0 1000 1000", "100%");
        innerSvg = new Svg(0, 0, "0 0" + (width + 200) + " " + (length + 200), "100%");
        innerSvg.addRectangle(0, 0, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPost();
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
        //Pil til højre
        outerSvg.addArrow(length +20, 0, length + 20, width , "stroke:#000000; fill: #000000");

        //Pil ned
        outerSvg.addArrow(0, width + 20, length , width + 20, "stroke:#000000; fill: #000000");
    }

    public void addText()
    {
        //Tekst til højre
        outerSvg.addText(length +25, width / 2, 90, String.valueOf(width) + " cm");

        //Tekst ned
        outerSvg.addText(length / 2, width+35, 0, String.valueOf(length) + " cm");
    }

    @Override
    public String toString()
    {
        return outerSvg.toString();
    }
}