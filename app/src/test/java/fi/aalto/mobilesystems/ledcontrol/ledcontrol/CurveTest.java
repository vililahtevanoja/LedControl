package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

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
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    { 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f, 0.0, 0.0f, 0.0f},
                    { 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f, 0.5, 1.0f, 1.0f},
                    { 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f, 1.0, 2.0f, 2.0f}});
        }

        private Curve curve;
        private double ratio;
        private PointF expected;

        public PointOnCurveTest(float x1, float y1, float x2, float y2, float x3, float y3,
                                double ratio, float x4, float y4) {
            this.curve = new Curve();
            curve.addPoint(new PointF(x1, y1));
            curve.addPoint(new PointF(x2, y2));
            curve.addPoint(new PointF(x3, y3));
            this.ratio = ratio;
            this.expected = new PointF(x4, y4);
        }

        @Test
        public void testPointOnCurve() {
            PointF actual = this.curve.getPointOnCurve(ratio);
            assertTrue(actual.equals(expected));
        }
    }
}