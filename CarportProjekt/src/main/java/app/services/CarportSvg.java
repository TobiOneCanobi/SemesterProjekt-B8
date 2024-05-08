package app.services;

public class CarportSvg
{
    private int width;
    private int length;
    private Svg innerSvg;
    private Svg outerSvg;

    public CarportSvg(int width, int height)
    {
        this.width = width;
        this.length = length;
        outerSvg = new Svg(0, 0, "0 0 855 690", "75%" );
        innerSvg = new Svg(40, 20, "0 0 600 780", "100%" );
        innerSvg.addRectangle(0,0,600, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addArrow();
        addText();
        addBeams();
        addRafters();
        addPost();
        outerSvg.addSvg(innerSvg);

    }

    //Bjælker
    private void addBeams(){
        innerSvg.addRectangle(0,35,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        innerSvg.addRectangle(0,565,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    //Spær
    private void addRafters(){
        for (double i = 0; i < 780; i+= 55.714)
        {
            innerSvg.addRectangle(i, 0.0, 600, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }

    //Stolper
    private void addPost(){
        for (double i = 80; i < 780; i+= 200)
        {
            innerSvg.addRectangle(i, 32.5, 10, 10,"stroke:#000000; fill: #000000" );
        }

        for (double i = 80; i < 780; i+= 200)
        {
            innerSvg.addRectangle(i, 562.3, 10, 10,"stroke:#000000; fill: #000000" );
        }
    }

    public void addArrow()
    {
        outerSvg.addArrow(800, 0, 800, 600, "stroke:#000000; fill: #000000");
        outerSvg.addArrow(0, 620, 785, 620, "stroke:#000000; fill: #000000");
    }

    public void addText()
    {
        outerSvg.addText(815, 300, 90, "600");
        outerSvg.addText(390, 650, 0, "780");
    }




    @Override
    public String toString()
    {
        return outerSvg.toString();
    }
}