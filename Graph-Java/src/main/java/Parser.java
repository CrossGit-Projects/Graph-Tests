import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String SEPARATOR = ";";

    public ParsedFile parse(Path path) {
        ParsedFile parsedFile = new ParsedFile();
        List<String> lines = readFile(path);
        if (lines.isEmpty()) {
            throw new RuntimeException("File is empty: " + path);
        }
        List<Point> points = new ArrayList<>();
        if (isLegendLine(lines.get(0))) {
            parsedFile.legend = lines.get(0);
            lines.remove(lines.get(0));
        }
        if (isHeaderLine(lines.get(0))) {
            String[] header = lines.get(0).split(SEPARATOR);
            parsedFile.xLabel = header[0];
            parsedFile.yLabel = header[1];
            lines.remove(0);
        }
        for (String line : lines) {
            String[] point = line.split(SEPARATOR);
            points.add(new Point(Double.parseDouble(point[0]), Double.parseDouble(point[1])));
        }
        parsedFile.points = points;
        return parsedFile;
    }

    private List<String> readFile(Path file) {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file: " + file.toString());
        }
    }

    private boolean isHeaderLine(String parseLine) {
        String[] header = parseLine.split(SEPARATOR);
        try {
            Double.parseDouble(header[0]);
            Double.parseDouble(header[1]);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private boolean isLegendLine(String legend) {
        String[] legends = legend.split(SEPARATOR);
        if (legends.length == 1) {
            try {
                Double.parseDouble(legends[0]);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }
}
