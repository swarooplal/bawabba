package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.ReviewImageFullScreen;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.model.profile.ReviewByMe;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rninja4 on 11/9/17.
 */

public class EditReviewRecyclerviewAdapter  extends RecyclerView.Adapter<EditReviewRecyclerviewAdapter.MainViewHolder>{
    FragmentActivity activity;
    List<ReviewByMe> review;
    List<ReviewByMe> data = Collections.emptyList();
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context context;
    private SQLiteHandler db;
    private SessionManager session;


    public EditReviewRecyclerviewAdapter(Context context, List<ReviewByMe> review) {
        this.review = review;
        this.context = context;
        this.data = data;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_edit_review, parent, false);
        return new MainViewHolder(adapterView);
    }
    public void setOnClickListener(AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }
    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.bindData(holder, position);
        String response = ObjectFactory.getInstance().getAppPreference(context).getProfileResponse();
        final Profileresponse profileresponse=new Gson().fromJson(response,Profileresponse.class);

        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MMMM dd , yyyy");
            String inputDateStr[] = review.get(position).getDate().split(" ");
            Date date = inputFormat.parse(inputDateStr[0]);
            String outputDateStr = outputFormat.format(date);
            holder.review_date.setText(outputDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        HashMap<String, String> user = db.getUserDetails();
//        String verify = user.get("verify_code"); // value from db when logged in

//        if (verify.matches("0")) {
//          holder.verify_button.setVisibility(View.VISIBLE);
//        } else {
//            holder.verify_button.setVisibility(View.GONE);


        db = new SQLiteHandler(context);
        session = new SessionManager(context);

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");


//if else condition for for our profile visible delete and editfile and invisble for others profile

        if(db_id.equals(profileresponse.getUserData().getReviewByMe().get(position).getUserId())){
            holder.ivDeleteFile.setVisibility(View.GONE);
            holder.ivEditFile.setVisibility(View.GONE);
        }else{
            holder.ivDeleteFile.setVisibility(View.VISIBLE);
            holder.ivEditFile.setVisibility(View.VISIBLE);
        }

        holder.textProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_profile = new Intent(context,ProfileView.class);
                to_profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                to_profile.putExtra("reg_id",profileresponse.getUserData().getReviewByMe().get(position).getUserId());
                context.startActivity(to_profile);

            }
        });

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_review_image = new Intent(context,ReviewImageFullScreen.class);
                to_review_image.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                to_review_image.putExtra("review_image",profileresponse.getUserData().getReviewByMe().get(position).getReviewImage());
                context.startActivity(to_review_image);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    public interface AdvertisementsRecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile_image,
                ivImage,
                ivEditFile,
                ivDeleteFile;
        RatingBar ratings;
        TextView review_date,
                textProfileName,
                textreview_details,
                count_review;
        ImageButton verify_button;

        public MainViewHolder(View itemView) {
            super(itemView);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
            ratings = (RatingBar) itemView.findViewById(R.id.ratings);
            count_review=(TextView)itemView.findViewById(R.id.count_review);

            review_date = (TextView) itemView.findViewById(R.id.review_date);
            textProfileName = (TextView) itemView.findViewById(R.id.textProfileName);
            textreview_details = (TextView) itemView.findViewById(R.id.textreview_details);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            ivEditFile = (ImageView) itemView.findViewById(R.id.ivEditFile);
            ivDeleteFile = (ImageView) itemView.findViewById(R.id.ivDeleteFile);
            verify_button=(ImageButton)itemView.findViewById(R.id.verify_button);
            ivEditFile.setOnClickListener(this);
            ivDeleteFile.setOnClickListener(this);
        }

        public void bindData(MainViewHolder holder, int position) {
            try {
                Glide.with(context)
                        .load(review.get(position).getProfileImg()).transform(new CircleTransform(context))
                        .into(holder.profile_image);
                Glide.with(context)
                        .load(review.get(position).getReviewImage())
                        .into(holder.ivImage);
                holder.ratings.setRating(Float.parseFloat(review.get(position).getRating()));
                holder.review_date.setText(review.get(position).getDate());
                holder.textProfileName.setText(review.get(position).getCurrentName());
                holder.textreview_details.setText(review.get(position).getReview());

                String serverresponse = (review.get(position).getRating() != "null") ? review.get(position).getRating() : "0.0";

                holder.count_review.setText(serverresponse+".0");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onClick(View v) {
            clickListener.onClicked(getAdapterPosition(), v);
        }
    }
}
