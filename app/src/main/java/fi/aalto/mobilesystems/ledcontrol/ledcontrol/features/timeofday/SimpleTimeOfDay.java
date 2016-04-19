package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday;

public class SimpleTimeOfDay implements TimeOfDay {
    private static final int DEFAULT_MORNING = 6;
    private static final int DEFAULT_NIGHT = 22;
    private static final int DEFAULT_TRANSITION = 1;
    private int morningHour;
    private int nightHour;
    private int transition;

    public SimpleTimeOfDay() {
        this(DEFAULT_MORNING, DEFAULT_NIGHT, DEFAULT_TRANSITION);
    }

    public SimpleTimeOfDay(int morningHour, int nightHour) {
        this(morningHour, nightHour, DEFAULT_TRANSITION);
    }

    public SimpleTimeOfDay(int morningHour, int nightHour, int transition) {
        this.morningHour = morningHour;
        this.nightHour = nightHour;
        this.transition = transition;
    }

    protected double getTransitionValue(int hour, int minute) {
        double k;
        double x;
        double y;
        if (hour >= this.morningHour && hour < this.nightHour - this.transition) {
            return 1.0;
        }
        else if (hour < this.morningHour - transition || hour >= this.nightHour) {
            return 0.0;
        }
        else if (hour >= this.morningHour - transition && hour < this.nightHour - this.transition) {
            double time = hour + (minute / 60.0);
            double transitionStart = this.morningHour - this.transition;
            x = time - transitionStart;
            k = 1.0 / this.transition;
            y = 0.0;
        }
        else {
            double time = hour + (minute / 60.0);
            double transitionStart = this.nightHour - this.transition;
            x = time - transitionStart;
            k = -(1.0 / this.transition);
            y = 1.0;
        }
        return (k*x+y);
    }

    @Override
    public float[] fetchPHueXYColor() {
        float[] xy = new float[]{0.0f, 0.0f};
        return xy;
    }
}
