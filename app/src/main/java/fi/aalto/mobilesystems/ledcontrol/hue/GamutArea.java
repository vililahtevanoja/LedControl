package fi.aalto.mobilesystems.ledcontrol.hue;

/**
 * Class for Philips Hue light gamut areas
 */
public class GamutArea {
    private PointF red;
    private PointF green;
    private PointF blue;

    public GamutArea(PointF red, PointF green, PointF blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Constructor for gamut area type
     * For explanations on the area values, see:
     * http://www.developers.meethue.com/documentation/supported-lights
     *
     * @param gamutType Gamut type for this area
     */
    public GamutArea(GamutTypes gamutType) {
        switch (gamutType) {
            case A:
                this.red = new PointF(0.704f, 0.296f);
                this.green = new PointF(0.2151f, 0.7106f);
                this.blue = new PointF(0.138f, 0.08f);
                break;
            case B:
                this.red = new PointF(0.675f, 0.322f);
                this.green = new PointF(0.409f, 0.518f);
                this.blue = new PointF(0.167f, 0.04f);
                break;
            case C:
                this.red = new PointF(0.692f, 0.308f);
                this.green = new PointF(0.17f, 0.7f);
                this.blue = new PointF(0.153f, 0.048f);
                break;
            default:
        }
    }

    public PointF getRed() {
        return red;
    }

    public PointF getGreen() {
        return green;
    }

    public PointF getBlue() {
        return blue;
    }

    /**
     * Finds out whether a given point resides within this gamut area
     * @param p Given point
     * @return True if within this gamut area, else false
     */
    public boolean isPointInsideArea(PointF p) {
        float s = red.y * blue.x - red.x * blue.y + (blue.y - red.y) * p.x + (red.x - blue.x) * p.y;
        float t = red.x * green.y - red.y * green.x + (red.y - green.y) * p.x + (green.x - red.x) * p.y;

        if ((s < 0.0f) != (t < 0.0f))
            return false;

        float A = -green.y * blue.x + red.y * (blue.x - green.x) + red.x * (green.y - blue.y) + green.x * blue.y;
        return (s > 0.0f) && (t > 0.0f) && ((s + t) <= A);
    }
}