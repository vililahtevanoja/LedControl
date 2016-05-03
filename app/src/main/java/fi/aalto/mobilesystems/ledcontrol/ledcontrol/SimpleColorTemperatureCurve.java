package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import java.util.LinkedList;

public class SimpleColorTemperatureCurve {
    private static final float[][] coordinates = new float[][]
            {
                {0.52f, 0.412f},
                {0.46f, 0.41f},
                {0.44f, 0.48f},
                {0.38f, 0.378f},
                {0.348f,0.352f},
                {0.32f, 0.332f},
                {0.308f,0.32f},
                //{0.28f, 0.29f},
                //{0.26f, 0.26f},
            };
    public static Curve getCurve() {
        LinkedList<PointF> points = new LinkedList<>();
        for (float[] point : coordinates) {
            float px = point[0];
            float py = point[1];
            points.add(new PointF(px, py));
        }
        return new Curve(points);
    }

    public static Curve getRefinedCurve(int steps) {
        return getCurve().refined(steps);
    }
}
