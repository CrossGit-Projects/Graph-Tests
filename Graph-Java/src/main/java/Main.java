import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run(args);
    }

    public void run(String[] args) throws IOException {
        MainParser mainParser = new MainParser(args);
        Chart chart = new Chart(mainParser.title, mainParser.paths);
        boolean notLineStyle = mainParser.style != 0;
        boolean notCircleStyle = mainParser.style != 1;
        Draw draw = new Draw(chart, notCircleStyle, notLineStyle);
        draw.draw(mainParser.outputFileName);
    }
}
