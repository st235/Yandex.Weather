package suhockii.dev.weather.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import suhockii.dev.weather.R;

/**
 * Created by Maksim Sukhotski on 8/13/2017.
 */

public class GoogleListAdapter extends SimpleCursorAdapter {
    private final Context context;

    public GoogleListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
        super(context, layout, cursor, from, to, flags);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        Cursor c = getCursor();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String cityString = c.getString(c.getColumnIndex("city"));

        if (position == c.getCount() - 1) {
            row = inflater.inflate(R.layout.item_search_suggest_ya, parent, false);
        } else {
            row = inflater.inflate(R.layout.item_search_suggest, parent, false);
        }
        TextView sender = row.findViewById(R.id.text1);
        sender.setText(cityString);
        return row;
    }
}
