//package com.example.rninja4.rookie.activity.portfolioTab;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;
//import com.example.rninja4.rookie.R;
//
//import java.util.ArrayList;
//
///**
// * Created by rninja4 on 8/2/17.
// */
//
//public class GridThumbnailAdapter extends BaseAdapter {
//
//    private ImageLoader imageLoader;
//
//    //Context
//    private Context context;
//
//    //Array List that would contain the urls and the titles for the images
//    private ArrayList<String> thumbnails;
//    private ArrayList<String> names;
//
//    public GridThumbnailAdapter(Context context, ArrayList<String> thumbnails){
//        //Getting all the values
//        this.context = context;
//        this.thumbnails = thumbnails;
//        // this.names = names;
//    }
//
//    @Override
//    public int getCount() {
//        return thumbnails.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return thumbnails.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //Creating a linear layout
//        LinearLayout linearLayout = new LinearLayout(context);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        //NetworkImageView
//        NetworkImageView networkImageView = new NetworkImageView(context);
//
//        //Initializing ImageLoader
//        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
//        imageLoader.get(thumbnails.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
//
//        //Setting the image url to load
//        networkImageView.setImageUrl(thumbnails.get(position),imageLoader);
//
//        //Creating a textview to show the title
////        TextView textView = new TextView(context);
////        textView.setText(names.get(position));
//
//        //Scaling the imageview
//        networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        networkImageView.setLayoutParams(new GridView.LayoutParams(200,200));
//
//        //Adding views to the layout
//        // linearLayout.addView(textView);
//        linearLayout.addView(networkImageView);
//
//        //Returnint the layout
//        return linearLayout;
//    }
//}
