package fi.aalto.mobilesystems.ledcontrol.hue;

/**
 * Class for a representing a point
 */
public class PointF extends com.philips.lighting.hue.sdk.utilities.impl.PointF {
    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(PointF p) {
        this(p.x, p.y);
    }

    public boolean equals(PointF p) {
        return (this.x == p.x) && (this.y == p.y);
    }
}
