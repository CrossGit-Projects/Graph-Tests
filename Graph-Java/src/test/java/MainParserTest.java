import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainParserTest {

    @Test
    public void constructorMainParser() {
        String[] arguments = new String[]{"title", "23", "file.svg", "path.csv"};
        MainParser test = new MainParser(arguments);
        assertEquals(test.title, "title");
        assertEquals(test.style, 23);
        assertEquals(test.outputFileName, "file.svg");
        assertEquals(test.paths.get(0).toString(), "path.csv");
    }

    @Test
    public void constructorMainParserArgumentStyle() {
        String[] arguments = new String[]{"title", "StringTestStyle", "file.svg", "path.csv"};
        compileNegativesExceptionMainParser(arguments, "Wrong argument: " + arguments[1] + " It must be integer.");
    }

    @Test
    void testForArgumentsMainGivenNoLessThanThreeParameters() {
        String[] arguments;
        compileNegativesException(arguments = new String[]{"title", "karol.svg"},
                "Wrong number of arguments: " + arguments.length + " It must be no less than 3 arguments.");
        compileNegativesException(arguments = new String[]{"file.svg"},
                "Wrong number of arguments: " + arguments.length + " It must be no less than 3 arguments.");
    }

    @Test
    void testForSecondArgumentMustBeInteger() {
        String[] arguments;
        compileNegativesException(arguments = new String[]{"title", "string", "file.svg"},
                "Wrong argument: " + arguments[1] + " It must be integer.");
    }

    private void compileNegativesException(String[] negativeArguments, String exceptionMessage) {
        Main call = new Main();
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            call.run(negativeArguments);
        });
        assertEquals(thrown.getMessage(), exceptionMessage);
    }

    private void compileNegativesExceptionMainParser(String[] negativeArguments, String exceptionMessage) {
        NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> {
            new MainParser(negativeArguments);
        });
        assertEquals(thrown.getMessage(), exceptionMessage);
    }
}
