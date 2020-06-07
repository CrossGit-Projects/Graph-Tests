import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    void mainIntegrationTest() throws URISyntaxException, IOException {
        String testFileName = "twoPointsWithLine.svg";
        Main main = new Main();
        String[] arguments = new String[]{"title", "2", getPathToFile(testFileName), getPathToFile("coordinates-for-two-point.csv")};
        main.run(arguments);
        XmlParser xmlParser = new XmlParser(Paths.get(getPathToFile(testFileName)));
        Chart chart = new Chart();
        chart.xmin = 421.3;
        chart.xmax = 543.0;
        chart.ymin = 243.0;
        chart.ymax = 332.4;
        Draw draw = new Draw(chart, false, false);
        checkMapValueForOnePoint(421.3, 332.4, getMapForPointWithIndex(xmlParser, 0), draw);
        checkMapValueForOnePoint(543, 243, getMapForPointWithIndex(xmlParser, 1), draw);
        checkMapValueForOneLine(421.3, 543, 332.4, 243, getMapForLineWithIndex(xmlParser, 2), draw);
    }

    private String getPathToFile(String fileName) throws URISyntaxException {
        File file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()).toFile();
        return file.getAbsolutePath();
    }

    private Map<String, String> getMapForLineWithIndex(XmlParser xmlParser, int index) {
        return xmlParser.getAllValueFromLine(xmlParser.getAllObjectLines().get(index));
    }

    private Map<String, String> getMapForPointWithIndex(XmlParser xmlParser, int index) {
        return xmlParser.getAllValueFromLine(xmlParser.getAllCircles().get(index));
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

    public static String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

}
