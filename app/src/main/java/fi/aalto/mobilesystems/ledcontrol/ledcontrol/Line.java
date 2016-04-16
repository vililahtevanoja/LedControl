package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

/**
 * Class for representing a line
 */
public class Line {
    private PointF a;
    private PointF b;

    public Line(PointF a, PointF b) {
        this.a = a;
        this.b = b;
    }

    public Line(Line line) {
        this(line.getA(), line.getB());
    }

    public PointF getA() {
        return new PointF(a);
    }

    public PointF getB() {
        return new PointF(b);
    }

    /**
     * Get point n-way through a line
     * @param n Ratio for the point on the line. e.g. 0.5 is halfway, 1/3 is third of the way
     * @return Calculated point on the line
     */
    public PointF getPointOnLine(float n) {
        float x = (1 - n) * a.x + n * b.x;
        float y = (1 - n) * a.y + n * b.y;
        return new PointF(x, y);
    }
}
