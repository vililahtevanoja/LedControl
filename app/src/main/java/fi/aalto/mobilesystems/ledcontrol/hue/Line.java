package fi.aalto.mobilesystems.ledcontrol.hue;

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

    /**
     * Get point n-way through a line
     * @param n
     * @return
     */
    public PointF getPointOnLine(float n) {
        float x = (1 - n) * a.x + n * b.x;
        float y = (1 - n) * a.y + n * b.y;
        return new PointF(x, y);
    }
}
