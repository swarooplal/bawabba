package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.model.profile.Review;
import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.ReviewImageFullScreen;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_review;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rninja4 on 8/13/17.
 */

public class AdapterReviewResult extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterReviewResult.class.getSimpleName();
    List<ReviewResultData> data = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;

    private SQLiteHandler db;
    private SessionManager session;

    public AdapterReviewResult(Context context, List<ReviewResultData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.review_result, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyHolder myHolder = (MyHolder) holder;

        final ReviewResultData current = data.get(position);
        myHolder.textprofileName.setText(current.profileName);
        myHolder.textreviewDetails.setText(current.reviewdetails);

//        myHolder.textreviewDate.setText(current.reviewdate);

        Intent from_port_audio = ((Activity) context).getIntent();
        final String review = from_port_audio.getStringExtra("review");

        Glide.with(context).load(current.profileImage)
                .transform(new CircleTransform(context))
                .into(myHolder.ivprofile);

        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MMMM dd , yyyy");
            String inputDateStr[] = current.reviewdate.split(" ");
            Date date = inputFormat.parse(inputDateStr[0]);
            String outputDateStr = outputFormat.format(date);
            myHolder.textreviewDate.setText(outputDateStr);

        }catch (ParseException e){
            e.printStackTrace();
        }
        try {
            final ArrayList<String> user_id_list = new ArrayList<String>();

            JSONArray rev_prof_data = new JSONArray(review);

            for (int i = 0; i < rev_prof_data.length(); i++) {

                JSONObject object = rev_prof_data.getJSONObject(i);
                final String user_id = object.getString("user_id");
                user_id_list.add(user_id.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Glide.with(context).load(current.reviewImage).into(myHolder.review_image);
        Log.e("review_image", review);

        if (current.ratingdetails != "null") {
            String serverresponse = current.ratingdetails;
        }

        if (current.verify_value.matches("0")) {
            myHolder.verify_button.setVisibility(View.VISIBLE);
        } else {
            myHolder.verify_button.setVisibility(View.GONE);
        }

        String serverresponse = (current.ratingdetails != "null") ? current.ratingdetails : "0.0";
        myHolder.textRating.setRating(Float.parseFloat(serverresponse));
        myHolder.textRating.setFocusable(false);

        if (current.review_count != "null") {
            String serverresponse2 = current.ratingdetails;
        }

        String serverresponse2 = (current.review_count != "null") ? current.ratingdetails : "0.0";
        myHolder.count_review.setText(serverresponse2 + ".0");
        myHolder.count_review.setFocusable(false);

        String response = ObjectFactory.getInstance().getAppPreference(context).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);


        try {
//            myHolder.setItemClickListener_review.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent to_profile = new Intent(context,ProfileView.class);
//                    to_profile.putExtra("reg_id",profileresponse.getUserData().getReview().get(position).getUserId());
//                    context.startActivity(to_profile);
//                    //((Activity) context).finishActivity();
//
//
//                }
//            });

            myHolder.setItemClickListener_review(new ItemClickListener_review() {
                @Override
                public void onItemClick(int pos) {
                    Review review1=profileresponse.getUserData().getReview().get(position);
                    BaseBottomHelperActivity.start(context,ProfileViewFragment.class.getName(),review1.getUserId(),review1.getCurrentName());

                    /*Intent to_profile = new Intent(context, ProfileView.class);
                    to_profile.putExtra("reg_id", profileresponse.getUserData().getReview().get(position).getUserId());
                    context.startActivity(to_profile);*/

                }
            });

            myHolder.review_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent to_review_image = new Intent(context, ReviewImageFullScreen.class);
                    to_review_image.putExtra("review_image", data.get(position).reviewImage);
                    context.startActivity(to_review_image);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textprofileName;
        ImageView ivprofile;
        TextView textreviewDetails;
        RatingBar textRating;
        TextView textreviewDate;
        ImageView review_image;
        ImageButton verify_button;
        TextView count_review;

        ItemClickListener_review itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);

            textprofileName = (TextView) itemView.findViewById(R.id.textProfileName);
            ivprofile = (ImageView) itemView.findViewById(R.id.profile_image);
            textreviewDetails = (TextView) itemView.findViewById(R.id.textreview_details);
            textRating = (RatingBar) itemView.findViewById(R.id.ratings);
            textreviewDate = (TextView) itemView.findViewById(R.id.review_date);
            review_image = (ImageView) itemView.findViewById(R.id.review_image);
            count_review = (TextView) itemView.findViewById(R.id.count_review);
            verify_button = (ImageButton) itemView.findViewById(R.id.verify_button);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_review(ItemClickListener_review itemClickListener) {

          this.itemClickListener = (ItemClickListener_review) itemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}



