import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlParser {

    List<String> lines;

    public XmlParser(Path path) {
        this.lines = readFile(path);
    }

    private List<String> readFile(Path file) {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file");
        }
    }

    public Map<String, String> getAllValueFromLine(List<String> lines, int nofLine) {
        return getAllValueFromLine(lines.get(nofLine));
    }

    public Map<String, String> getAllValueFromLine(String line) {
        String[] elements = line.split(" ");
        List<String> znaczniki = Arrays.stream(elements).filter(arr -> arr.contains("=")).collect(Collectors.toList());
        return znaczniki.stream().collect(
                Collectors.toMap(x -> x.substring(0, x.indexOf("=")), x -> x.substring(x.indexOf("=") + 2, x.length() - 1)));

    }

    public List<String> getAllCircles() {
        return this.lines.stream().filter(line -> line.contains("<circle ")).collect(Collectors.toList());
    }

    public List<String> getAllObjectLines() {
        return this.lines.stream().filter(line -> line.contains("<line ")).collect(Collectors.toList());
    }
}
