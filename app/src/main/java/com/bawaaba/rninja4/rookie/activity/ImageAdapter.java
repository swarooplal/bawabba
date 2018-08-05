//package com.example.rninja4.rookie.activity;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.rninja4.rookie.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**ll
// * Created by rninja4 on 4/16/17.
// */
//
//public class ImageAdapter extends BaseAdapter {
//    private Context mContext;
//    private final String[] gridViewString;
//    private final int[] gridViewImageId;
//
//    public ImageAdapter(Context context, ArrayList<HashMap<String, String>> categoryList, int activity_grid_view_image_text, String[] gridViewString, int[] gridViewImageId) {
//        mContext = context;
//        this.gridViewImageId = gridViewImageId;
//        this.gridViewString = gridViewString;
//    }
//    @Override
//    public int getCount() {
//        return gridViewString.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View convertView, ViewGroup parent) {
//        View gridViewAndroid;
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        if (convertView == null) {
//
//            gridViewAndroid = new View(mContext);
//            gridViewAndroid = inflater.inflate(R.layout.gridview_layout,null);
//            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
//            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
//            textViewAndroid.setText(gridViewString[i]);
//            imageViewAndroid.setImageResource(gridViewImageId[i]);
//        } else {
//            gridViewAndroid = (View) convertView;
//        }
//
//        return gridViewAndroid;
//    }
//
//}
