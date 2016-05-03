package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

/**
 * Class for representing a line
 */
public class Line {
    private PointF a;
    private PointF b;
    private double length;

    public Line(PointF a, PointF b) {
        this.a = a;
        this.b = b;
        this.length = a.distanceTo(b);
    }

    public Line(Line line) {
        this(line.getA(), line.getB());
    }

    public PointF getA() {
        return new PointF(a);
    }

    public void setA(PointF a) {
        this.a = a;
        this.length = a.distanceTo(b);
    }

    public PointF getB() {
        return new PointF(b);
    }

    public void setB(PointF b) {
        this.b = b;
        this.length = a.distanceTo(b);
    }

    public double length() {
        return this.length;
    }

    /**
     * Get point n-way through a line
     * @param ratio Ratio for the point on the line. e.g. 0.5 is halfway, 1/3 is third of the way
     * @return Calculated point on the line
     */
    public PointF getPointOnLine(double ratio) {
        float x = (float) ((1-ratio) * a.x + ratio * b.x);
        float y = (float) ((1-ratio) * a.y + ratio * b.y);
        return new PointF(x, y);
    }

}
