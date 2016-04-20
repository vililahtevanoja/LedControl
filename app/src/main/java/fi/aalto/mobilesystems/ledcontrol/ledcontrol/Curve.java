package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

    public Curve(List<PointF> points, int steps) {
        this.points = calculateQuadraticBezierCurvePoints(points, steps);
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

    /**
     * Refines this curve using the quadratic Beziér formula.
     * @param steps Number of refinement steps. A larger number will result in a smoother curve with
     *              more points.
     */
    public void refine(int steps) {
        this.points = calculateQuadraticBezierCurvePoints(this.points, steps);
    }

    /**
     * Get length of the curve
     * @return
     */
    public double getLength() {
        if (this.points.size() <= 1)
            return 0.0;
        double length = 0.0;
        PointF next;
        Iterator<PointF> pointIterator = this.points.iterator();
        PointF current = pointIterator.next();
        while (pointIterator.hasNext()) {
            next = pointIterator.next();
            length += current.distanceTo(next);
            current = next;
        }
        return length;
    }

    /**
     * Get point on curve defined by the given curve ratio.
     * @param curveRatio Ratio for the point on the line. e.g. 0.5 is halfway, 1/3 is third of the
     *                   way
     * @return PointF object of the point dividing the curve according to the given ratio
     */
    public PointF getPointOnCurve(double curveRatio) {
        double curveLength = getLength();
        if (curveRatio > 1.0) {
            curveRatio = 1.0;
        }
        else if (curveRatio < 0) {
            curveRatio = 0;
        }
        if (curveLength == 0) {
            return new PointF(0.0f, 0.0f);
        }
        else if (curveLength == 1) {
            return new PointF(this.points.get(0));
        }

        double wantedPosition = curveLength * curveRatio;
        double currentPosition = 0.0;
        Iterator<PointF> pointIterator = this.points.iterator();
        PointF current = pointIterator.next();
        PointF next = new PointF();
        double lengthSoFar = 0.0;
        while (currentPosition < wantedPosition && pointIterator.hasNext()) {
            next = pointIterator.next();
            double distance = current.distanceTo(next);
            if (currentPosition + distance > wantedPosition) {
                break;
            }
            else {
                lengthSoFar += distance;
                currentPosition += current.distanceTo(next);
                current = next;
            }
        }

        if (currentPosition == wantedPosition) {
            return new PointF(current);
        }
        double distance = current.distanceTo(next);
        double ratio = (wantedPosition - lengthSoFar) / distance;
        PointF p = current.pointBetween(next, ratio);
        return p;
    }

    /**
     * Creates a curve refined from this curve with quadratic Beziér curve algorithm.
     *
     * @param steps Number of refinement steps. A larger number will result in a smoother curve with
     *              more points.
     * @return
     */
    public Curve refined(int steps) {
        Curve curve = new Curve();
        int numberOfCurvePoints = this.points.size();
        for (int i = 0; i <= numberOfCurvePoints - 3; i++) {
            curve.addPoints(calculateQuadratizBezierCurvePoints(steps, points.get(i),
                    points.get(i+1), points.get(i+2)));
        }
        return curve;
    }

    /**
     * Uses the quadratic Beziér curve algorithm to calculate a new curve point set.
     * @param points Input point set
     * @param steps Number of steps used in the algorithm. A larger number will result in a smoother
     *              curve with more points.
     * @return
     */
    private List<PointF> calculateQuadraticBezierCurvePoints(List<PointF> points, int steps) {
        int numberOfCurvePoints = this.points.size();
        List<PointF> newPoints = new LinkedList<>();
        for (int i = 0; i <= numberOfCurvePoints - 3; i++) {
            points.addAll(calculateQuadratizBezierCurvePoints(steps, points.get(i),
                    points.get(i+1), points.get(i+2)));
        }
        return newPoints;
    }

    /**
     * Uses the quadratic Beziér curve algorithm to calculate a new curve point set from three
     * input points.
     *
     * @param steps Number of steps used in the algorithm. A larger number will result in a smoother
     *              curve with more points.
     * @param p1
     * @param p2
     * @param p3
     * @return
     */
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
