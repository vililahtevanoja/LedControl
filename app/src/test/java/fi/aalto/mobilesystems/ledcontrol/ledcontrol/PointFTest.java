package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PointFTest {

    @RunWith(Parameterized.class)
    public static class DistanceToTest {
        @Parameterized.Parameters
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {0.0f, 0.0f, 1.0f, 1.0f, 1.414f},
                    {1.0f, 1.0f, 0.0f, 0.0f, 1.414f},
                    {0.0f, 0.0f, -1.0f, -1.0f, 1.414f},
                    {-1.0f, -1.0f, 0.0f, 0.0f, 1.414f},
                    {100.0f, 0.0f, 0.0f, 0.0f, 100.0f},
                    {0.0f, 0.0f, 100.0f, 0.0f, 100.0f},
                    {0.0f, 100.0f, 0.0f, 0.0f, 100.0f},
                    {0.0f, 0.0f, 0.0f, 100.0f, 100.0f},
                    {-1.0f, -1.0f, 1.0f, 1.0f, 2.828f},
                    {0.0f, 0.0f, 0.0f, 0.0f, 0.0f}});
        }

        private PointF p1;
        private PointF p2;
        private double expected;

        public DistanceToTest(float x1, float y1, float x2, float y2, double expected) {
            this.p1 = new PointF(x1, y1);
            this.p2 = new PointF(x2, y2);
            this.expected = expected;
        }

        @Test
        public void testDistanceTo() {
            double actual = p1.distanceTo(p2);
            assertEquals(this.expected, actual, 0.001);
        }
    }

    @RunWith(Parameterized.class)
    public static class PointBetweenTest {
        @Parameterized.Parameters
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    { 0.0f, 0.0f, 1.0f, 1.0f, 1.0, 1.0f, 1.0f},
                    { 0.0f, 0.0f, 1.0f, 1.0f, 0.0, 0.0f, 0.0f},
                    { 0.0f, 0.0f, 2.0f, 2.0f, 0.5, 1.0f, 1.0f},
                    { 0.0f, 0.0f, -1.0f, -1.0f, 1.0, -1.0f, -1.0f},
                    { -1.0f, -1.0f, 0.0f, 0.0f, 0.0, -1.0f, -1.0f},
                    { 0.0f, 0.0f, 3.0f, 3.0f, 0.5, 1.5f, 1.5f},
                    { 0.0f, 0.0f, 3.0f, 3.0f, (1/3.0), 1.0f, 1.0f}});
        }

        private PointF p1;
        private PointF p2;
        private double ratio;
        private PointF expected;

        public PointBetweenTest(float x1, float y1, float x2, float y2, double ratio,
                                float x3, float y3) {
            this.p1 = new PointF(x1, y1);
            this.p2 = new PointF(x2, y2);
            this.ratio = ratio;
            this.expected = new PointF(x3, y3);
        }

        @Test
        public void testPointBetween() {
            PointF actual = this.p1.pointBetween(this.p2, this.ratio);
            assertTrue(actual.equals(expected));
        }
    }

    @Test
    public void testEquals() throws Exception {
        PointF p1 = new PointF(1.0f, 1.0f);
        PointF p2 = new PointF(1.0f, 1.0f);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }

    @Test
    public void testEqualsShouldNot() throws Exception {
        PointF p1 = new PointF(1.001f, 1.0f);
        PointF p2 = new PointF(1.0f, 1.0f);
        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));
    }
}