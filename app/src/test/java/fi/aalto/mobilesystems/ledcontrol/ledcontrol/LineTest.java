package fi.aalto.mobilesystems.ledcontrol.ledcontrol;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

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
        assertTrue(actual1.equals(expected));
        assertTrue(actual2.equals(expected));
    }

    @Test
    public void testGetA() throws Exception {
        PointF a = new PointF(2.0f, 2.0f);
        PointF b = new PointF(0.0f, 0.0f);
        Line line = new Line(a, b);
        assertTrue(line.getA().equals(a));
    }

    @Test
    public void testSetA() throws Exception {
        PointF a = new PointF(1.0f, 1.0f);
        PointF b = new PointF(0.0f, 0.0f);
        PointF c = new PointF(2.0f, 2.0f);
        Line line = new Line(a, b);
        line.setA(c);
        assertTrue(line.getA().equals(c));
    }

    @Test
    public void testGetB() throws Exception {
        PointF a = new PointF(0.0f, 0.0f);
        PointF b = new PointF(2.0f, 2.0f);
        Line line = new Line(a, b);
        assertTrue(line.getB().equals(b));
    }

    @Test
    public void testSetB() throws Exception {
        PointF a = new PointF(1.0f, 1.0f);
        PointF b = new PointF(0.0f, 0.0f);
        PointF c = new PointF(2.0f, 2.0f);
        Line line = new Line(a, b);
        line.setB(c);
        assertTrue(line.getB().equals(c));
    }

    @RunWith(Parameterized.class)
    public static class LengthTest {
        @Parameterized.Parameters
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {0.0f, 0.0f, 0.0f, 0.0f, 0.0},
                    {0.0f, 0.0f, 1.0f, 1.0f, 1.414},
                    {1.0f, 1.0f, 0.0f, 0.0f, 1.414},
                    {1.0f, 0.0f, 0.0f, 0.0f, 1.0},
                    {0.0f, 1.0f, 0.0f, 0.0f, 1.0},
                    {0.0f, 0.0f, 1.0f, 0.0f, 1.0},
                    {0.0f, 0.0f, 0.0f, 1.0f, 1.0},
                    {-1.0f, 0.0f, 0.0f, 0.0f, 1.0},
                    {0.0f, -1.0f, 0.0f, 0.0f, 1.0},
                    {0.0f, 0.0f, -1.0f, 0.0f, 1.0},
                    {0.0f, 0.0f, 0.0f, -1.0f, 1.0}});
        }

        private Line line;
        private double expected;

        public LengthTest(float x1, float y1, float x2, float y2, double expected) {
            PointF p1 = new PointF(x1, y1);
            PointF p2 = new PointF(x2, y2);
            this.line = new Line(p1, p2);
            this.expected = expected;
        }

        @Test
        public void testDistanceTo() {
            double actual = this.line.length();
            assertEquals(this.expected, actual, 0.001);
        }
    }
}
