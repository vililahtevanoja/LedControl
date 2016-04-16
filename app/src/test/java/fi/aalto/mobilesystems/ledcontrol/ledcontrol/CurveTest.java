package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import org.junit.Test;

import java.util.List;

public class CurveTest {

    @Test
    public void testRefined() throws Exception {
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
}