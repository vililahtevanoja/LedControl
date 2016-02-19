package fi.aalto.mobilesystems.ledcontrol.hue;


import com.philips.lighting.hue.sdk.utilities.impl.PointF;

import org.junit.Test;

import static org.junit.Assert.*;

public class GamutAreaTest {
    @Test
    public void testIsPointInsideArea() throws Exception {
        GamutArea area = new GamutArea(GamutTypes.A);
        PointF p = new PointF(0.4f, 0.4f);
        assertTrue(area.isPointInsideArea(p));
    }

    @Test
    public void testIsPointNotInsideArea() throws Exception {
        GamutArea area = new GamutArea(GamutTypes.A);
        PointF p = new PointF(0.8f, 0.8f);
        assertFalse(area.isPointInsideArea(p));
    }
}