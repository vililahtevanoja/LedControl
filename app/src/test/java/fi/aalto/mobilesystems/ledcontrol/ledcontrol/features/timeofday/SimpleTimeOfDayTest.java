package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

public class SimpleTimeOfDayTest {

    @RunWith(Parameterized.class)
    public static class GetTransitionValueTest {
        @Parameterized.Parameters
        public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {6, 22, 2, 12, 0, 1.0},
                    {6, 22, 2, 0, 0, 0.0},
                    {6, 22, 2, 4, 40, 0.33},
                    {6, 22, 2, 5, 20, 0.67},
                    {6, 22, 2, 6, 0, 1.0},
                    {6, 22, 2, 20, 0, 1.0},
                    {6, 22, 2, 20, 40, 0.67},
                    {6, 22, 2, 21, 20, 0.33},
                    {6, 22, 2, 22, 0, 0.0 }});
        }

        private int morningHour;
        private int nightHour;
        private int transitionHours;
        private int currentHour;
        private int currentMinute;
        private double expected;

        public GetTransitionValueTest(int morningHour, int nightHour, int transitionHours,
                                      int currentHour, int currentMinute, double expected) {
            this.morningHour = morningHour;
            this.nightHour = nightHour;
            this.transitionHours = transitionHours;
            this.currentHour = currentHour;
            this.currentMinute = currentMinute;
            this.expected = expected;
        }

        @Test
        public void testGetTransitionValue() {
            SimpleTimeOfDay mtod = new SimpleTimeOfDay(morningHour, nightHour, transitionHours);
            double actual = mtod.getTransitionValue(currentHour, currentMinute);
            assertEquals(expected, actual, 0.01);
        }
    }

    @Test
    public void testFetchColor() throws Exception {

    }
}