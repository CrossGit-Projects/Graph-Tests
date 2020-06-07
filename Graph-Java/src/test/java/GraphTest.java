import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {
    @Test
    public void testCheckVariableInChartObject() throws URISyntaxException {
        Chart chart = new Chart("Ilosc Miodu", Collections.singletonList(getPathToFile("coordinates-with-header.csv")));
        assertEquals(chart.title, "Ilosc Miodu");
        assertEquals(chart.xLabel, "objętość");
        assertEquals(chart.yLabel, "waga");
        assertEquals(chart.xmax, 788);
        assertEquals(chart.ymax, 400);
        assertEquals(chart.xmin, 421.3);
        assertEquals(chart.ymin, 121);
    }

    @Test
    public void testDrawEmptyChart() throws IOException, URISyntaxException {
        String testFileName = "emptyChart.svg";
        drawChart(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOneLineAxis("600", "600", "600", "100", getMapForLineWithIndex(xmlParser, 0));
        checkMapValueForOneLineAxis("600", "1100", "600", "600", getMapForLineWithIndex(xmlParser, 1));
    }

    @Test
    public void testCreateChartWithOnePoint() throws IOException, URISyntaxException {
        String testFileName = "onePoint.svg";
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-for-one-point.csv")));
        Draw draw = new Draw(chart, true, false);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
    }

    @Test
    public void testCreateChartWithTwoPoints() throws IOException, URISyntaxException {
        String testFileName = "twoPoint.svg";
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-for-two-point.csv")));
        Draw draw = new Draw(chart, true, false);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
        checkMapValueForOnePoint(543, 243, getMapForPointWithIndex(xmlParser, 1), draw);
    }

    @Test
    public void testCreateChartWithManyPoints() throws IOException, URISyntaxException {
        String testFileName = "manyPoint.svg";
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-without-header.csv")));
        Draw draw = new Draw(chart, true, false);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));

        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
        checkMapValueForOnePoint(543, 243, getMapForPointWithIndex(xmlParser, 1), draw);
        checkMapValueForOnePoint(600, 400, getMapForPointWithIndex(xmlParser, 2), draw);
        checkMapValueForOnePoint(670, 233, getMapForPointWithIndex(xmlParser, 3), draw);
        checkMapValueForOnePoint(720, 399, getMapForPointWithIndex(xmlParser, 4), draw);
        checkMapValueForOnePoint(788, 121, getMapForPointWithIndex(xmlParser, 5), draw);
    }

    @Test
    public void testCreateChartWithTwoPointsConnectedByLine() throws IOException, URISyntaxException {
        String testFileName = "twoPointsWithLine.svg";
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-for-two-point.csv")));
        Draw draw = new Draw(chart, true, true);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
        checkMapValueForOnePoint(543, 243, getMapForPointWithIndex(xmlParser, 1), draw);
        checkMapValueForOneLine(421.3, 543, 332.4, 243, getMapForLineWithIndex(xmlParser, 2), draw);
    }

    @Test
    public void testCreateChartWithManyPointsConnectedByLine() throws IOException, URISyntaxException {
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-without-header.csv")));
        String testFileName = "connectManyPoints.svg";
        Draw draw = new Draw(chart, true, true);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOneLine(421.3, 543, 332.4, 243, getMapForLineWithIndex(xmlParser, 2), draw);
        checkMapValueForOneLine(543, 600, 243, 400, getMapForLineWithIndex(xmlParser, 3), draw);
        checkMapValueForOneLine(600, 670, 400, 233, getMapForLineWithIndex(xmlParser, 4), draw);
        checkMapValueForOneLine(670, 720, 233, 399, getMapForLineWithIndex(xmlParser, 5), draw);
        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
        checkMapValueForOnePoint(543, 243, getMapForPointWithIndex(xmlParser, 1), draw);
    }


    @Test
    public void testCreateChartWithManyPointsConnectedByLineForOtherData() throws IOException, URISyntaxException {
        String testFileName = "anotherData.svg";
        Chart chart = new Chart("Maliny 2020", Collections.singletonList(getPathToFile("coordinates-another-data.csv")));
        Draw draw = new Draw(chart, false, true);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOneLine(3, 4, 6, 7, getMapForLineWithIndex(xmlParser, 2), draw);
        checkMapValueForOneLine(4, 15, 7, 22, getMapForLineWithIndex(xmlParser, 3), draw);
        checkMapValueForOneLine(15, 25, 22, 68, getMapForLineWithIndex(xmlParser, 4), draw);
        checkMapValueForOneLine(25, 49, 68, 22, getMapForLineWithIndex(xmlParser, 5), draw);
        checkMapValueForOneLine(49, 88, 22, 97, getMapForLineWithIndex(xmlParser, 6), draw);
    }

    @Test
    public void testForTwoLineOnOneChart() throws IOException, URISyntaxException {
        String testFileName = "anotherData1.svg";
        Chart chart = new Chart("Maliny 2020",
                Arrays.asList(getPathToFile("coordinates-without-headerOne.csv"), getPathToFile("coordinates-without-header.csv")));
        Draw draw = new Draw(chart, false, true);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        xmlParser.getAllObjectLines().forEach(System.out::println);
        //first file
        checkMapValueForOneLine(401, 500, 521.3, 50, getMapForLineWithIndex(xmlParser, 2), draw);
        checkMapValueForOneLine(500, 600, 50, 200, getMapForLineWithIndex(xmlParser, 3), draw);
        checkMapValueForOneLine(600, 633, 200, 288, getMapForLineWithIndex(xmlParser, 4), draw);
        //second file
        checkMapValueForOneLine(421.3, 543, 332.4, 243, getMapForLineWithIndex(xmlParser, 7), draw);
        checkMapValueForOneLine(543, 600, 243, 400, getMapForLineWithIndex(xmlParser, 8), draw);
        checkMapValueForOneLine(600, 670, 400, 233, getMapForLineWithIndex(xmlParser, 9), draw);
    }

    @Test
    public void testCreateChartWithOnlyLines() throws IOException, URISyntaxException {
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-without-header.csv")));
        String testFileName = "onlyLine.svg";
        Draw draw = new Draw(chart, false, true);
        draw.draw(getPathToFile(testFileName).toString());
        XmlParser xmlParser = new XmlParser(getPathToFile(testFileName));
        checkMapValueForOneLine(421.3, 543, 332.4, 243, getMapForLineWithIndex(xmlParser, 2), draw);
        checkMapValueForOneLine(543, 600, 243, 400, getMapForLineWithIndex(xmlParser, 3), draw);
        checkMapValueForOneLine(600, 670, 400, 233, getMapForLineWithIndex(xmlParser, 4), draw);
        checkMapValueForOneLine(670, 720, 233, 399, getMapForLineWithIndex(xmlParser, 5), draw);
    }

    private void drawChart(String fileName) throws URISyntaxException, IOException {
        Chart chart = new Chart("Tytul", Collections.singletonList(getPathToFile("coordinates-without-header.csv")));
        Draw draw = new Draw(chart, true, true);
        draw.draw(fileName);
    }

    private Path getPathToFile(String fileName) throws URISyntaxException {
        File file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        return Paths.get(absolutePath);
    }

    public static String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    private void checkMapValueForOneLineAxis(String x1, String x2, String y1, String y2, Map<String, String> map) {
        assertEquals(map.get("x1"), x1);
        assertEquals(map.get("x2"), x2);
        assertEquals(map.get("y1"), y1);
        assertEquals(map.get("y2"), y2);
    }

    private void checkMapValueForOneLine(double x1, double x2, double y1, double y2, Map<String, String> map, Draw draw) {
        assertEquals(map.get("x1"), fmt(draw.getCoordinatesX(x1)));
        assertEquals(map.get("x2"), fmt(draw.getCoordinatesX(x2)));
        assertEquals(map.get("y1"), fmt(draw.getCoordinatesY(y1)));
        assertEquals(map.get("y2"), fmt(draw.getCoordinatesY(y2)));
    }

    private void checkMapValueForOnePoint(double x1, double y1, Map<String, String> map, Draw draw) {
        assertEquals(map.get("cx"), fmt(draw.getCoordinatesX(x1)));
        assertEquals(map.get("cy"), fmt(draw.getCoordinatesY(y1)));
    }

    private Map<String, String> getMapForLineWithIndex(XmlParser xmlParser, int index) {
        return xmlParser.getAllValueFromLine(xmlParser.getAllObjectLines().get(index));
    }

    private Map<String, String> getMapForPointWithIndex(XmlParser xmlParser, int index) {
        return xmlParser.getAllValueFromLine(xmlParser.getAllCircles().get(index));
    }
}
