import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Objects.isNull;

public class Draw {
    private Chart chart;
    private static final double RADIUS = 5;
    private static final double VECTOR = 100;
    static final double POINT0 = 500;
    static final double SPACE = 20;
    private List<Color> colors;
    //Red two points
    static final double oYx1 = VECTOR + POINT0;
    static final double oYy1 = VECTOR + POINT0;
    static final double oYx2 = VECTOR + POINT0;
    static final double oYy2 = VECTOR;
    //Green two points
    static final double oXx1 = VECTOR + POINT0;
    static final double oXy1 = VECTOR + POINT0;
    static final double oXx2 = VECTOR + POINT0 + POINT0;
    static final double oXy2 = VECTOR + POINT0;
    private Color color;
    private boolean scatterPlot;
    private boolean graphLine;

    public Draw(Chart chart, boolean scatterPlot, boolean graphLine) {
        colors = new ArrayList<>();
        colors.addAll(Arrays.asList(Color.BLACK, Color.GRAY, Color.BLUE, Color.ORANGE, Color.CYAN));
        this.chart = chart;
        this.scatterPlot = scatterPlot;
        this.graphLine = graphLine;
    }

    public void paint(Graphics2D g) {
        Graphics2D g2d = g;
        g2d.setFont(new Font("Calibri", Font.BOLD, 20));
        g2d.setColor(Color.RED);
        Stroke stroke = new BasicStroke(4f);
        g2d.setStroke(stroke);
        g2d.draw(new Line2D.Double(oYx1, oYy1, oYx2, oYy2));
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(4f));
        g2d.draw(new Line2D.Double(oXx1, oXy1, oXx2, oXy2));

        int legendDistance = 0;
        for (List<Point> pointList : chart.points) {
            randomColor();
            for (int i = 0; i < pointList.size(); i++) {
                g2d.setColor(color);
                if (scatterPlot) {
                    g2d.draw(new Ellipse2D.Double(getCoordinatesX(pointList.get(i).x) - RADIUS, getCoordinatesY(pointList.get(i).y) - RADIUS, 2.0 * RADIUS, 2.0 * RADIUS));
                }
                if (i < pointList.size() - 1) {
                    if (graphLine) {
                        Point2D.Double point = new Point2D.Double(getCoordinatesX(pointList.get(i).x), getCoordinatesY(pointList.get(i).y));
                        Point2D.Double point1 = new Point2D.Double(getCoordinatesX(pointList.get(i + 1).x), getCoordinatesY(pointList.get(i + 1).y));
                        g2d.draw(new Line2D.Double(point, point1));
                    }
                }
            }
            g2d.draw(new Line2D.Double((float) (oYx1 - 120), (float) ((oYy2 + oYy1) / 2) + legendDistance * 40, (float) (oYx1 - 110), (float) ((oYy2 + oYy1) / 2) + legendDistance * 40));
            g2d.setColor(Color.BLACK);
            g2d.drawString(isNull(chart.legends.get(legendDistance)) ? "wykres " + legendDistance + 1 : chart.legends.get(legendDistance), (float) (oYx1 - 100), (float) ((oYy2 + oYy1) / 2) + 10 + legendDistance * 40);
            legendDistance++;
        }
        drawLabels(g2d);
    }

    private void randomColor() {
        Random r = new Random();
        int index = r.nextInt(colors.size());
        color = colors.get(index);
        colors.remove(index);
    }

    private void drawLabels(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawString(chart.title, (float) ((oXx1 + oXx2) / 2), (float) (oYy2 - SPACE));
        g2d.drawString(chart.xLabel, (float) ((oXx1 + oXx2) / 2), (float) (oXy2 + SPACE));
        g2d.drawString(String.valueOf(chart.xmin), (float) ((oXx1)), (float) (oXy2 + SPACE));
        g2d.drawString(String.valueOf(chart.xmax), (float) ((oXx2)), (float) (oXy2 + SPACE));
        g2d.drawString(String.valueOf(chart.ymin), (float) (oYx1 - SPACE * 3), (float) ((oYy1)));
        g2d.drawString(String.valueOf(chart.ymax), (float) (oYx1 - SPACE * 3), (float) ((oYy2)));
        g2d.rotate(Math.toRadians(270), (float) (oYx1 - SPACE), (float) ((oYy2 + oYy1) / 2));
        g2d.drawString(chart.yLabel, (float) (oYx1 - SPACE), (float) ((oYy2 + oYy1) / 2));
        g2d.drawString(chart.yLabel, (float) (oYx1 - SPACE), (float) ((oYy2 + oYy1) / 2));
    }

    private double getScaleCoordinateX() {
        return (chart.xmax - chart.xmin) / POINT0;
    }

    private double getScaleCoordinateY() {
        return (chart.ymax - chart.ymin) / POINT0;
    }

    public double getCoordinatesX(double x) {
        return Math.round((x - chart.xmin) / getScaleCoordinateX() + oXx1);
    }

    public double getCoordinatesY(double y) {
        return Math.round(-(y - chart.ymin) / getScaleCoordinateY() + oYy1);
    }

    public void draw(String outputNameFile) throws IOException {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        paint(svgGenerator);
        boolean useCSS = true;
        File f = new File(outputNameFile);
        OutputStream outp = new FileOutputStream(f);
        Writer out = new OutputStreamWriter(outp, StandardCharsets.UTF_8);
        svgGenerator.stream(out, useCSS);
    }
}
