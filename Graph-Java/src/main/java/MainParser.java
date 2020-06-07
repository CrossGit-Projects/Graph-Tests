import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainParser {

    String title;
    int style;
    List<Path> paths;
    String outputFileName;


    public MainParser(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("Wrong number of arguments: " + args.length + " It must be no less than 3 arguments.");
        }
        if (isNotInteger(args[1])) {
            throw new NumberFormatException("Wrong argument: " + args[1] + " It must be integer.");
        }
        paths = new ArrayList<>();
        title = args[0];
        style = Integer.parseInt(args[1]);
        outputFileName = args[2];
        for (int i = 3; i < args.length; i++) {
            paths.add(Paths.get(args[i]));
        }
    }

    private boolean isNotInteger(String arguments) {
        try {
            Double.parseDouble(arguments);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

}
