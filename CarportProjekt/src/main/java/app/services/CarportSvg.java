package app.services;

public class CarportSvg {
    private int width;
    private int length;
    private Svg innerSvg;
    private Svg outerSvg;

    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;
        outerSvg = new Svg(0, 0, "0 0 " + (width + 255) + " " + (length + 150), "75%");
        innerSvg = new Svg(40, 20, "0 0 " + width + " " + length, "100%");
        innerSvg.addRectangle(0, 0, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addArrow();
        addText();
        addBeams();
        addRafters();
        addPost();
        outerSvg.addSvg(innerSvg);
    }

    //Bjælker
    private void addBeams() {
        innerSvg.addRectangle(0, 35, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        innerSvg.addRectangle(0, width - 35, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    //Spær
    private void addRafters() {
        double spacing = 55.714;
        for (double i = 0; i < length; i += spacing) {
            innerSvg.addRectangle(i, 0.0, width, 4.5, "stroke:#000000; fill: #ffffff");
        }
    }

    //Stolper
    private void addPost() {
        double spacing = 200;
        for (double i = spacing; i < length; i += spacing) {
            innerSvg.addRectangle(i, 32.5, 10, 10, "stroke:#000000; fill: #000000");
            innerSvg.addRectangle(i, width - 37.5, 10, 10, "stroke:#000000; fill: #000000");
        }
    }

    public void addArrow() {
        outerSvg.addArrow(length + 50, 20, length + 50, width - 50, "stroke:#000000; fill: #000000");
        outerSvg.addArrow(40, width + 50, length - 45, width + 50, "stroke:#000000; fill: #000000");
    }

    public void addText() {
        outerSvg.addText(length + 60, width / 2, 90, String.valueOf(width));
        outerSvg.addText(length / 2, width + 70, 0, String.valueOf(length));
    }

    @Override
    public String toString() {
        return outerSvg.toString();
    }
}
