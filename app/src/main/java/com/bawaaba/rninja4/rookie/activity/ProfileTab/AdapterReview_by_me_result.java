package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rninja4 on 11/20/17.
 */

 public class AdapterReview_by_me_result extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterReview_by_me_result.class.getSimpleName();
    List<ReviwbymeData> data = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;

    public AdapterReview_by_me_result(Context context, List<ReviwbymeData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_result, parent, false);
        AdapterReview_by_me_result.MyHolder holder = new AdapterReview_by_me_result.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdapterReview_by_me_result.MyHolder myHolder = (AdapterReview_by_me_result.MyHolder) holder;
        ReviwbymeData current = data.get(position);
        myHolder.textprofileName.setText(current.profileName);
        myHolder.textreviewDetails.setText(current.reviewdetails);

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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(context).load(current.reviewImage).into(myHolder.review_image);

        if (current.ratingdetails != "null") {
            String serverresponse = current.ratingdetails;
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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder  extends RecyclerView.ViewHolder{

        TextView textprofileName;
        ImageView ivprofile;
        TextView textreviewDetails;
        RatingBar textRating;
        TextView textreviewDate;
        ImageView review_image;
        TextView count_review;


        public MyHolder(View itemView) {
            super(itemView);

            textprofileName = (TextView) itemView.findViewById(R.id.textProfileName);
            ivprofile = (ImageView) itemView.findViewById(R.id.profile_image);
            textreviewDetails = (TextView) itemView.findViewById(R.id.textreview_details);
            textRating = (RatingBar) itemView.findViewById(R.id.ratings);
            textreviewDate = (TextView) itemView.findViewById(R.id.review_date);
            review_image = (ImageView) itemView.findViewById(R.id.review_image);
            count_review=(TextView)itemView.findViewById(R.id.count_review);
        }
    }
}
