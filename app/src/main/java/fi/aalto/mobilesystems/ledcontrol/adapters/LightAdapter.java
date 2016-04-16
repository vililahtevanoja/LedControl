package fi.aalto.mobilesystems.ledcontrol.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;

import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.R;

public class LightAdapter implements ListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PHLight> lightList;

    public LightAdapter(Context context, List<PHLight> lights) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.lightList = lights;
    }

    @Override
    public int getCount() {
        return this.lightList.size();
    }

    @Override
    public PHLight getItem(int position) {
        return this.lightList.get(position);
    }

    @Override
    public long getItemId(int i) {
        String itemIdStr = this.lightList.get(i).getUniqueId();
        long itemId = Integer.parseInt(itemIdStr);
        return itemId;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.light_setting_view, viewGroup, false);
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.lightList.isEmpty();
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
