import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Chart {
    public String title;
    public String xLabel;
    public List<List<Point>> points;
    public String yLabel;
    public double xmin;
    public double xmax;
    public double ymin;
    public double ymax;
    public List<String> legends;

    public Chart() {

    }

    public Chart(String title, List<Path> pathsToPoints) {
        Parser parser = new Parser();
        List<ParsedFile> parsedFiles = pathsToPoints.stream().map(parser::parse).collect(Collectors.toList());
        this.points = parsedFiles.stream().map(pf -> pf.points).collect(Collectors.toList());
        this.xLabel = parsedFiles.stream()
                .filter(parsedFile -> nonNull(parsedFile.xLabel))
                .findFirst()
                .map(parsedFile -> parsedFile.xLabel)
                .orElse("xlabel");
        this.yLabel = parsedFiles.stream()
                .filter(parsedFile -> nonNull(parsedFile.yLabel))
                .findFirst()
                .map(parsedFile -> parsedFile.yLabel)
                .orElse("ylabel");
        this.title = title;
        this.xmin = getMinimumX();
        this.xmax = getMaximumX();
        this.ymax = getMaximumY();
        this.ymin = getMinimumY();
        this.legends = parsedFiles.stream()
                .map(parsedFile -> parsedFile.legend).collect(Collectors.toList());
    }

    private double getMinimumX() {
        return Collections.min(this.points.stream()
                .flatMap(List::stream)
                .map(point -> point.x)
                .collect(Collectors.toList()));
    }

    private double getMinimumY() {
        return Collections.min(this.points.stream().flatMap(List::stream).map(point -> point.y).collect(Collectors.toList()));
    }

    private double getMaximumX() {
        return Collections.max(this.points.stream().flatMap(List::stream).map(point -> point.x).collect(Collectors.toList()));
    }

    private double getMaximumY() {
        return Collections.max(this.points.stream().flatMap(List::stream).map(point -> point.y).collect(Collectors.toList()));
    }

}
