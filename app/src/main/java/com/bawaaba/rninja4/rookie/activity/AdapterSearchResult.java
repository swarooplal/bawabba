package com.bawaaba.rninja4.rookie.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_search;
import com.bawaaba.rninja4.rookie.model.searchResult.User;

import java.util.List;

/**
 * Created by rninja4 on 6/13/17.
 */

public class AdapterSearchResult extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterSearchResult.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<User> data;
    String reg_id;

    public AdapterSearchResult(Context context, List<User> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_result, parent, false);
        AdapterSearchResult.MyHolder holder = new AdapterSearchResult.MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdapterSearchResult.MyHolder myHolder = (AdapterSearchResult.MyHolder) holder;


        myHolder.textprofileName.setText(data.get(position).getCurrentName());

        String skills ="";

        for (int i = 0; i < data.get(position).getSkills().size(); i++) {
            // skills =skills + ", " + data.get(position).getSkills().get(i);
            skills = skills + data.get(position).getSkills().get(i);
            if(i < (data.get(position).getSkills().size()-1)){
                skills = skills + ", ";
            }
        }

        myHolder.textskill.setText(skills);
        myHolder.textBio.setText(data.get(position).getExperience());

        myHolder.count_review.setText(data.get(position).getReviewCount() +' '+"Reviews");

        if(data.get(position).getRating()!="null"){
            String serverresponse = (data.get(position).getRating() == null)?"0.0":data.get(position).getRating();
            myHolder.count_rating.setText(serverresponse);
        }
        try {
            myHolder.textRating.setRating(Float.parseFloat(data.get(position).getRating()));
            myHolder.textRating.setFocusable(false);
        } catch (Exception e) {
        }

//        if(data.get(position).getRating()!="null"){
//            String serverresponse=data.get(position).getRating();
//        }
//        String serverresponse=(data.get(position).getRating() != "null")?data.get(position).getRating():"0.0";
//        myHolder.textRating.setRating(Float.parseFloat(serverresponse));
//        myHolder.textRating.setFocusable(false);
//
//        if(data.get(position).getRating()!="null"){
//            String serverresponse2=data.get(position).getRating();
//
//            Log.e("serverresponse",serverresponse2);
//        }
//
//         String serverresponse2=(data.get(position).getRating() != "null")?data.get(position).getRating():"0.0";
//         myHolder.count_rating.setText(serverresponse2);
//         myHolder.count_rating.setFocusable(false);
//
//        myHolder.textRating.setRating(Float.parseFloat(data.get(position).getRating()));
        Glide.with(context).load(data.get(position).getProfileImg())
                .transform(new CircleTransform(context))
                .into(myHolder.ivprofile);

        if(data.get(position).getVerify().matches("0")){
            myHolder.verify_button.setVisibility(View.VISIBLE);
        }else{
            myHolder.verify_button.setVisibility(View.GONE);
        }


        // reg_id = current.reg_id;

//        Intent from_search_result = ((Activity) context).getIntent();
//        final String search_result = from_search_result.getStringExtra("search_result");
//
//
//        try {
//            final ArrayList<String> search_list_name = new ArrayList<String>();
//            final ArrayList<String> search_list_skill = new ArrayList<String>();
//            final ArrayList<String> search_list_bio = new ArrayList<String>();
//            final ArrayList<String> search_list_reg_id = new ArrayList<String>();
//            final ArrayList<String> search_list_image = new ArrayList<String>();
//            final ArrayList<String> search_list_rating = new ArrayList<String>();
//            final ArrayList<String> search_list_raing_count = new ArrayList<String>();
//            final ArrayList<String> search_list_review_count = new ArrayList<String>();
//
//            JSONArray search_data = new JSONArray(search_result);
//
//            for (int i = 0; i < search_data.length(); i++) {
//                JSONObject object = search_data.getJSONObject(i);
//                final String profile_image = object.getString("profile_img");
//                final String profileName = object.getString("current_name");
//                final String biography = object.getString("experience");
//                final String skillName = object.getString("skills");
//                final String reg_id = object.getString("registration");
//               final String rating_count = object.getString("rating");
//              final String review_count = object.getString("review_count");
//                final String rating = object.getString("rating");
//
//                search_list_name.add(profileName.toString());
//                search_list_skill.add(skillName.toString());
//                search_list_bio.add(biography.toString());
//                search_list_reg_id.add(reg_id.toString());
//                search_list_image.add(profile_image.toString());
//                search_list_raing_count.add(rating_count.toString());
//                 search_list_review_count.add(review_count.toString());
//                search_list_rating.add(rating.toString());
//            }

        myHolder.setItemClickListener_serch(new ItemClickListener_search() {
            @Override
            public void onItemClick(int pos) {
                User review1=data.get(pos);
                BaseBottomHelperActivity.start(context,ProfileViewFragment.class.getName(),review1.getRegistration(),review1.getCurrentName());
//                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textprofileName;
        ImageView ivprofile;
        TextView textskill;
        TextView textBio;
        RatingBar textRating;
        TextView count_rating;
        TextView count_review;
        ImageButton verify_button;

        ItemClickListener_search itemClickListener;


        public MyHolder(final View itemView) {
            super(itemView);

            textprofileName= (TextView) itemView.findViewById(R.id.textProfileName);
            ivprofile= (ImageView) itemView.findViewById(R.id.profile_img);
            textskill = (TextView) itemView.findViewById(R.id.profile_skill);
            textBio = (TextView) itemView.findViewById(R.id.profile_bio);
            textRating=(RatingBar)itemView.findViewById(R.id.ratings);
            count_rating=(TextView)itemView.findViewById(R.id.count_review);
            count_review=(TextView)itemView.findViewById(R.id.Review_count);
            verify_button=(ImageButton)itemView.findViewById(R.id.verify_button);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_serch(ItemClickListener_search itemClickListener){

            this.itemClickListener=itemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
