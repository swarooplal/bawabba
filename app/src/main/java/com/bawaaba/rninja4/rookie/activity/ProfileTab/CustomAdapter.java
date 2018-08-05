package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;

/**
 * Created by rninja4 on 9/17/17.
 */

public class CustomAdapter extends BaseAdapter{


    private Context mContext;
    private String[]  Title;
    private int[] imge;

    public CustomAdapter(Context context, String[] account, int[] drawableIds) {

        mContext = context;
        Title = account;
        imge = drawableIds;
    }

    @Override
    public int getCount() {
        return Title.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row;
        row = inflater.inflate(R.layout.simplerow, parent, false);
        TextView profile_settings;
        ImageView settings_icons;
        settings_icons = (ImageView) row.findViewById(R.id.rowImageView);
        profile_settings = (TextView) row.findViewById(R.id.rowTextView);
        profile_settings.setText(Title[position]);
        settings_icons.setImageResource(imge[position]);

        return (row);


}
}
