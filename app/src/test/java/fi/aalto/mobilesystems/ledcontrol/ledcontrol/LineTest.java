package fi.aalto.mobilesystems.ledcontrol.ledcontrol;
import org.junit.Test;

import static org.junit.Assert.*;
public class LineTest {
    @Test
    public void getPointOnLineTest() {
        float n1 = 0.5f;
        float n2 = 1.0f/3.0f;
        Line l1 = new Line(new PointF(0.0f, 0.0f), new PointF(2.0f, 2.0f));
        Line l2 = new Line(new PointF(0.0f, 0.0f), new PointF(3.0f, 3.0f));
        PointF expected = new PointF(1.0f, 1.0f);
        PointF actual1 = l1.getPointOnLine(n1);
        PointF actual2 = l2.getPointOnLine(n2);
        assertTrue(expected.equals(l1.getPointOnLine(n1)));
        assertTrue(expected.equals(l2.getPointOnLine(n2)));
    }
}
