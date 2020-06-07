import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinatesTest {
    @Test
    public void testCoordinatesX() {
        Chart chart = new Chart();
        chart.xmin = 100;
        chart.xmax = 200;
        Draw draw = new Draw(chart, false, false);
        assertEquals(draw.getCoordinatesX(150), 850);
        assertEquals(draw.getCoordinatesX(100), 600);
        assertEquals(draw.getCoordinatesX(200), 1100);
    }

    @Test
    public void testCoordinatesY() {
        Chart chart = new Chart();
        chart.ymin = 300;
        chart.ymax = 800;
        Draw draw = new Draw(chart, false, false);
        assertEquals(draw.getCoordinatesY(700), 200);
        assertEquals(draw.getCoordinatesY(600), 300);
        assertEquals(draw.getCoordinatesY(500), 400);
    }
}
