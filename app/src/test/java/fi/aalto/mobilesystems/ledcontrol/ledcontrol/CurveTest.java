package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurveTest {
    @Test
    public void testRefined() throws Exception {
        // need to build proper test data for this
        Curve c = new Curve();
        c.addPoint(new PointF(0.0f, 0.0f));
        c.addPoint(new PointF(1.0f, 1.1f));
        c.addPoint(new PointF(0.0f, 2.0f));
        Curve c2 = c.refined(100);
        List<PointF> ls = c2.getPoints();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{");
        for (PointF p : ls) {
            strBuilder.append("{");
            strBuilder.append(p.x);
            strBuilder.append(",");
            strBuilder.append(p.y);
            strBuilder.append("},");
        }
        strBuilder.deleteCharAt(strBuilder.length()-1);
        strBuilder.append("}");
        String res = strBuilder.toString();
        System.out.println(res);
    }

    @RunWith(Parameterized.class)
    public static class PointOnCurveTest {
        @Parameterized.Parameters
        public static Iterable<Object[][]> data() {
            return Arrays.asList(new Object[][][]{
                    { {0.0, 0.0f, 0.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {0.5, 1.0f, 1.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {1.0, 2.0f, 2.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {1.0, 0.0f, 0.0f},
                            {
                            0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    },
                    { {0.5, 4.0f, 4.0f},
                            {
                                    0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    },
                    { {0.625, 4.0f, 2.0f},
                            {
                                    0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    }});
        }

        private Curve curve;
        private double ratio;
        private PointF expected;

        public PointOnCurveTest(Object[] input, Object[] points) {
            this.curve = new Curve();
            this.ratio = (double)input[0];
            this.expected = new PointF((float)input[1], (float)input[2]);
            float last = 0.0f;
            int i = 0;
            for (Object n : points) {
                if (i % 2 == 0)
                    last = (float)n;
                else {
                    this.curve.addPoint(new PointF(last, (float)n));
                }
                i++;
            }
        }

        @Test
        public void testPointOnCurve() {
            PointF actual = this.curve.getPointOnCurve(ratio);
            assertTrue(actual.equals(expected));
        }
    }

    @Test
    public void addLine() {
        PointF p1 = new PointF(4.5f, 3.2f);
        PointF p2 = new PointF(4.53f, 4.2f);
        Curve curve = new Curve();
        curve.addPoint(new PointF(0.0f, 0.3f));
        curve.addPoint(new PointF(0.1f, 0.3f));
        curve.addPoint(new PointF(0.7f, 0.3f));
        curve.addPoint(new PointF(0.6f, 0.3f));
        curve.addLine(new Line(p1, p2));
        List<PointF> points = curve.getPoints();
        int size = points.size();
        assertTrue(points.get(size-2).equals(p1));
        assertTrue(points.get(size-1).equals(p2));
    }

    @Test
    public void addLineNoDuplicatePointsKept() {
        PointF p1 = new PointF(4.5f, 3.2f);
        PointF p2 = new PointF(4.53f, 4.2f);
        Curve curve = new Curve();
        curve.addPoint(new PointF(0.0f, 0.3f));
        curve.addPoint(new PointF(0.1f, 0.3f));
        curve.addPoint(new PointF(0.7f, 0.3f));
        curve.addPoint(new PointF(0.6f, 0.3f));
        curve.addPoint(new PointF(4.5f, 3.2f));
        curve.addLine(new Line(p1, p2));
        List<PointF> points = curve.getPoints();
        int size = points.size();
        assertFalse(points.get(size-3).equals(p1) && points.get(size-2).equals(p1));
        assertTrue(points.get(size-1).equals(p2));
    }

    @Test
    public void addLines() {
        PointF p1 = new PointF(4.5f, 3.2f);
        PointF p2 = new PointF(4.53f, 4.2f);
        PointF p3 = new PointF(6.53f, 4.7f);
        PointF p4 = new PointF(7.32f, 0.31f);
        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p3, p4);
        List<Line> lines = new LinkedList<>();
        lines.add(l1);
        lines.add(l2);
        Curve curve = new Curve();
        curve.addPoint(new PointF(0.0f, 0.3f));
        curve.addPoint(new PointF(0.1f, 0.3f));
        curve.addPoint(new PointF(0.7f, 0.3f));
        curve.addPoint(new PointF(4.5f, 3.2f));
        curve.addLines(lines);
        List<PointF> points = curve.getPoints();
        int size = points.size();
        assertTrue(points.get(size-4).equals(p1));
        assertTrue(points.get(size-3).equals(p2));
        assertTrue(points.get(size-2).equals(p3));
        assertTrue(points.get(size-1).equals(p4));
    }

    @Test
    public void addLinesNoDuplicates() {
        PointF p1 = new PointF(4.5f, 3.2f);
        PointF p2 = new PointF(4.53f, 4.2f);
        PointF p3 = new PointF(4.53f, 4.2f);
        PointF p4 = new PointF(7.32f, 0.31f);
        Curve curve = new Curve();
        curve.addPoint(new PointF(0.0f, 0.3f));
        curve.addPoint(new PointF(0.1f, 0.3f));
        curve.addPoint(new PointF(0.7f, 0.3f));
        curve.addPoint(new PointF(0.6f, 0.3f));
        curve.addLine(new Line(p1, p2));
        curve.addLine(new Line(p3, p4));
        List<PointF> points = curve.getPoints();
        int size = points.size();
        assertTrue(points.get(size-2).equals(p2));
        assertTrue(points.get(size-1).equals(p4));
    }

    @Test
    public void refineIncreasesNumberOfPointsLowPoints() {
        int steps = 3;
        Curve curve = new Curve();
        curve.addPoint(new PointF(10.0f, 0.3f));
        curve.addPoint(new PointF(5.3f, 7.6f));
        curve.addPoint(new PointF(3.2f, 2.3f));
        int oldSize = curve.getPoints().size();
        int expectedNewSize = (oldSize-2)*4;
        curve.refine(steps);
        assertEquals(expectedNewSize, curve.getPoints().size());
    }

    @Test
    public void refineIncreasesNumberOfPoints() {
        int steps = 3;
        Curve curve = new Curve();
        float px = 0;
        float py = 0;
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            px = Math.abs(rand.nextFloat());
            py = Math.abs(rand.nextFloat());
            curve.addPoint(new PointF(px, py));
        }
        int oldSize = curve.getPoints().size();
        int expectedNewSize = (oldSize-2)*4;
        curve.refine(steps);
        assertEquals(expectedNewSize, curve.getPoints().size());
    }

    @Test
    public void refineWithNotEnoughPointsDoesNothing() {
        int steps = 3;
        Curve curve = new Curve();
        PointF p1 = new PointF(10.0f, 0.3f);
        PointF p2 = new PointF(5.3f, 7.6f);
        curve.addPoint(p1);
        curve.addPoint(p2);
        int oldSize = curve.getPoints().size();
        curve.refine(steps);
        List<PointF> points = curve.getPoints();
        assertEquals(points.size(), oldSize);
        assertEquals(points.get(0), p1);
        assertEquals(points.get(1), p2);
    }
}