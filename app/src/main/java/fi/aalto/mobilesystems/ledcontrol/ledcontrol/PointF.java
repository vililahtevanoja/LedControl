package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import java.lang.Math;

/**
 * Class for a representing a point
 */
public class PointF extends com.philips.lighting.hue.sdk.utilities.impl.PointF {
    public PointF() {
        this(0.0f, 0.0f);
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(PointF p) {
        this(p.x, p.y);
    }

    public double distanceTo(PointF p) {
        double xDelta = p.x - this.x;
        double yDelta = p.y - this.y;
        return Math.sqrt(Math.pow(xDelta, 2.0) + Math.pow(yDelta,2.0));
    }

    public PointF pointBetween(PointF p, double ratio) {
        Line line = new Line(this, p);
        return line.getPointOnLine(ratio);
    }

    public boolean equals(PointF p) {
        return (this.x == p.x) && (this.y == p.y);
    }
}
