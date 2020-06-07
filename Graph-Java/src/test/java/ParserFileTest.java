import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ParserFileTest {

    @Test
    public void parseCorrectCsvFileWithoutHeaderToPoints() throws URISyntaxException {
        Parser parser = new Parser();
        ParsedFile parsedFile = parser.parse(getPathToFile("coordinates-without-header.csv"));
        assertEquals(parsedFile.points.size(), 6);
        assertEquals(parsedFile.points.get(0).x, 421.3);
        assertEquals(parsedFile.points.get(0).y, 332.4);
        assertEquals(parsedFile.points.get(4).x, 720);
        assertEquals(parsedFile.points.get(4).y, 399);
        assertNull(parsedFile.xLabel);
        assertNull(parsedFile.yLabel);
        assertNull(parsedFile.legend);

    }

    @Test
    public void parseCorrectCsvFileWithHeaderToPoints() throws URISyntaxException {
        Parser parser = new Parser();
        ParsedFile parsedFile = parser.parse(getPathToFile("coordinates-with-header.csv"));
        assertEquals(parsedFile.points.size(), 6);
        assertEquals(parsedFile.points.get(0).x, 421.3);
        assertEquals(parsedFile.points.get(0).y, 332.4);
        assertEquals(parsedFile.points.get(4).x, 720);
        assertEquals(parsedFile.points.get(4).y, 399);
        assertEquals(parsedFile.xLabel, "objętość");
        assertEquals(parsedFile.yLabel, "waga");
        assertNull(parsedFile.legend);
    }

    @Test
    public void parseCorrectCsvFileWithHeaderAndLegendsToPoints() throws URISyntaxException {
        Parser parser = new Parser();
        ParsedFile parsedFile = parser.parse(getPathToFile("coordinates-with-header-and-legend.csv"));
        assertEquals(parsedFile.points.size(), 6);
        assertEquals(parsedFile.points.get(0).x, 421.3);
        assertEquals(parsedFile.points.get(0).y, 332.4);
        assertEquals(parsedFile.points.get(4).x, 720);
        assertEquals(parsedFile.points.get(4).y, 399);
        assertEquals(parsedFile.xLabel, "objętość");
        assertEquals(parsedFile.yLabel, "waga");
        assertEquals(parsedFile.legend, "Jabłka");
    }

    @Test
    public void parseCorrectTwoCsvFileWithOneHeaderAndTwoLegends() throws URISyntaxException {
        Chart chart = new Chart("Tytul", Arrays.asList(getPathToFile("coordinates-with-header-and-legend.csv"), getPathToFile("coordinates-for-two-point.csv")));
        assertEquals(chart.points.get(0).size(), 6);
        assertEquals(chart.points.get(1).size(), 2);
        assertEquals(chart.xLabel, "objętość");
        assertEquals(chart.yLabel, "waga");
        assertEquals(chart.legends.get(0), "Jabłka");
        assertEquals(chart.legends.get(1), "Gruszki");
    }

    @Test
    public void testExceptionNotExistFile() {
        Path path = Paths.get("not-exist-file.csv");
        Parser parser = new Parser();
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            parser.parse(path);
        });
        assertEquals(thrown.getMessage(), "Cannot read file: " + path);
    }

    @Test
    public void testExceptionEmptyFile() throws URISyntaxException {
        Path path = getPathToFile("empty-file.csv");
        Parser parser = new Parser();
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            parser.parse(path);
        });
        assertEquals(thrown.getMessage(), "File is empty: " + path);
    }

    private Path getPathToFile(String fileName) throws URISyntaxException {
        File file = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        return Paths.get(absolutePath);
    }
}

