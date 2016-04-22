package fi.aalto.mobilesystems.ledcontrol.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;

import fi.aalto.mobilesystems.ledcontrol.R;

public class SetLightColorView extends View {
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    public SetLightColorView(Context context, AttributeSet attribs) {
        super(context, attribs);
        inflate(context, R.layout.light_setting_view, (ViewGroup) findViewById(android.R.id.content));
    }

    public void setColor(float x, float y) {
        int color = PHUtilities.colorFromXY(new float[]{x, y}, "");
        String colorHex = String.format("%6s", Integer.toHexString(color)).replace(' ', '0');
        int r = Integer.valueOf(colorHex.substring(0, 2), 16);
        int g = Integer.valueOf(colorHex.substring(2, 4), 16);
        int b = Integer.valueOf(colorHex.substring(4, 6), 16);
        this.redSeekBar.setProgress(r);
        this.greenSeekBar.setProgress(g);
        this.blueSeekBar.setProgress(b);
    }


}
