package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for representing a curve
 */
public class Curve {
    private List<PointF> points;
    public Curve() {
        this.points = new LinkedList<>();
    }

    public Curve(List<PointF> points) {
        this.points = points;
    }

    /**
     * Add line to curve
     * Addition is made while ensuring that the start point is not the same as the last added point
     * @param line Line to add
     */
    public void addLine(Line line) {
        PointF lastAdded = this.points.get(points.size()-1);
        PointF a = line.getA();
        PointF b = line.getB();
        if(!lastAdded.equals(a)) {
            this.points.add(line.getA());
        }
        this.points.add(b);
    }

    /**
     * Add lines to curve. The lines are added so that continuous line combinations
     * don't cause duplicate entries.
     * e.g. Adding lines A -> B and B -> C cause points A, B, C to be added, with no duplicate B
     * @param lines Lines to add
     */
    public void addLines(List<Line> lines) {
        PointF lastAdded = this.points.get(points.size()-1);
        PointF a;
        PointF b;
        // add lines to curve while ensuring that no duplicates are added
        // e.g. from lines A -> B and B -> C points A, B and C are added
        for (Line line : lines) {
            a = line.getA();
            b = line.getB();
            if (!lastAdded.equals(a)) {
                this.points.add(a);
                lastAdded = a;
            }
            this.points.add(line.getB());
            lastAdded = b;
        }
    }

    /**
     * Add point to curve
     * @param point Point to add
     */
    public void addPoint(PointF point) {
        this.points.add(point);
    }

    /**
     * Add points to curve
     * @param points Points to add
     */
    public void addPoints(List<PointF> points) {
        this.points.addAll(points);
    }

    /**
     * Get all points oncurve
     * @return List containing all points on the curve
     */
    public List<PointF> getPoints() {
        return new LinkedList<>(points);
    }

    /**
     * Set curve points to new values
     * @param points New curve points
     */
    public void setPoints(List<PointF> points) {
        this.points = points;
    }

    public Curve refined(int resolution) {
        return getQuadraticBezierCurve(resolution);
    }

    private Curve getQuadraticBezierCurve(int steps) {
        Curve curve = new Curve();
        int numberOfCurvePoints = this.points.size();
        for (int i = 0; i <= numberOfCurvePoints - 3; i++) {
            curve.addPoints(calculateQuadratizBezierCurvePoints(steps, points.get(i), points.get(i+1), points.get(i+2)));
        }
        return curve;
    }

    private List<PointF> calculateQuadratizBezierCurvePoints(int steps, PointF p1, PointF p2, PointF p3) {
        LinkedList<PointF> ps = new LinkedList<>();
        for (int j = 0; j <= steps; j++) {
            float r = ((float)j) / steps;
            float x = (1 - r) * (1 - r) * p1.x + 2 * (1 - r) * r * p2.x + r * r * p3.x;
            float y = (1 - r) * (1 - r) * p1.y + 2 * (1 - r) * r * p2.y + r * r * p3.y;
            ps.add(new PointF(x, y));
        }
        return ps;
    }
}
