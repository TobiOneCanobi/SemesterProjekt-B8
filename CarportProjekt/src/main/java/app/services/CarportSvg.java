package app.services;

public class CarportSvg
{
    private int width;
    private int length;
    private app.services.Svg carportSvg;

    public CarportSvg(int width, int height)
    {
        this.width = width;
        this.length = length;
        carportSvg = new app.services.Svg(0, 0, "0 0 855 690", "75%" );
        carportSvg.addRectangle(0,0,600, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPost();
        addArrow();
        addText();
    }

    //Bjælker
    private void addBeams(){
        carportSvg.addRectangle(0,35,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0,565,4.5, 780, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    //Spær
    private void addRafters(){
        for (double i = 0; i < 780; i+= 55.714)
        {
            carportSvg.addRectangle(i, 0.0, 600, 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }

    //Stolper
    private void addPost(){
        for (double i = 80; i < 780; i+= 200)
        {
            carportSvg.addRectangle(i, 32.5, 10, 10,"stroke:#000000; fill: #000000" );
        }

        for (double i = 80; i < 780; i+= 200)
        {
            carportSvg.addRectangle(i, 562.3, 10, 10,"stroke:#000000; fill: #000000" );
        }
    }

    public void addArrow()
    {
        carportSvg.addArrow(800, 0, 800, 600, "stroke:#000000; fill: #000000");
        carportSvg.addArrow(0, 620, 785, 620, "stroke:#000000; fill: #000000");
    }

    public void addText()
    {
        carportSvg.addText(300, 805, 90, "600");
        carportSvg.addText(0, 0, 0, "780");
    }


    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}