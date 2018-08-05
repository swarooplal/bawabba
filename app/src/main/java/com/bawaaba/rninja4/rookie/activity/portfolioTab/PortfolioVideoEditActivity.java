package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.activity.adapters.VideoAsGridRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ChatFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.SearchFragment;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioVideoEditActivity extends BaseActivity {

    private AppCompatTextView tvAddVideos;
    private RecyclerView rvVideos;
    private List<PortfolioVideoData> videosData = new ArrayList<>();
    private Button no_delete;
    private Button yes_delete;
    private AppCompatTextView No_videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_video_edit);
        getSupportActionBar().hide();
        BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);
        bottomNavigationView.setFirstSelectedPosition(2);
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(3)
                .initialise();

        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
                        /*Intent to_main = new Intent(PortfolioVideoEditActivity.this, MainActivity.class);
                        startActivity(to_main);*/
                        finish();
                        break;
                    case 1:
                        BaseBottomHelperActivity.start(getApplicationContext(), SearchFragment.class.getName(),null,null);
                       /* Intent to_search = new Intent(PortfolioVideoEditActivity.this, SearchActivity.class);
                        startActivity(to_search);*/
                        finish();
                        break;
                    case 2:
                        BaseBottomHelperActivity.start(getApplicationContext(), ChatFragment.class.getName(),null,null);
                       /* Intent to_inbox = new Intent(PortfolioVideoEditActivity.this, ChatActivity.class);
                        startActivity(to_inbox);*/
                        finish();
                        break;
                    case 3:
                        AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                        BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                       /* Intent to_profile = new Intent(PortfolioVideoEditActivity.this, ProfileView.class);
                        startActivity(to_profile);*/
                        finish();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });



        tvAddVideos = (AppCompatTextView) findViewById(R.id.tvAddVideos);
        rvVideos = (RecyclerView) findViewById(R.id.rvVideos);
        No_videos=(AppCompatTextView)findViewById(R.id.video_count);
        rvVideos.setLayoutManager(new GridLayoutManager(PortfolioVideoEditActivity.this, 2));

        videosData = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getVideosData();

//        for (int i = 0; i< videosData.size(); i++) {
//            System.out.println("PortfolioVideoEditActivity.onCreate link" + videosData.get(i).getVideo_link());
//            System.out.println("PortfolioVideoEditActivity.onCreate thumpnail" + videosData.get(i).getVideo_thumbnail());
//            System.out.println("PortfolioVideoEditActivity.onCreate tittle" + videosData.get(i).getVideo_Title());
//        }

        setVideoDatas();
        tvAddVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortfolioVideoEditActivity.this, VideoAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setVideoDatas() {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (profileresponse.getUserData().getPortfolioVideo() != null)
            if (profileresponse.getUserData().getPortfolioVideo().size() > 0) {
                No_videos.setVisibility(View.GONE);
                final VideoAsGridRecyclerviewAdapter videoAsGridRecyclerviewAdapter = new VideoAsGridRecyclerviewAdapter(this, profileresponse.getUserData().getPortfolioVideo());
                rvVideos.setAdapter(videoAsGridRecyclerviewAdapter);
                videoAsGridRecyclerviewAdapter.setOnClickListener(new VideoAsGridRecyclerviewAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClicked(final int position, View v) {
                        switch (v.getId()) {
                            case R.id.ivDelete:
//                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PortfolioVideoEditActivity.this);
//                                View mView = getLayoutInflater().inflate(R.layout.dialogue_delete_video, null);
//
//                                no_delete=(Button)mView.findViewById(R.id.no_button);
//                                yes_delete=(Button)mView.findViewById(R.id.yes_button);
//
//                                mBuilder.setView(mView);
//                                final AlertDialog dialog = mBuilder.create();
//                                dialog.show();
//                                yes_delete.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Log.e("image delete","yesbutton");
//                                        apiCallTodelete(profileresponse.getUserData().getPortfolioVideo().get(position).getId());
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                                no_delete.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.dismiss();
//                                    }
//                                });
                                final iOSDialog iOSDialog = new iOSDialog(PortfolioVideoEditActivity.this);
                                iOSDialog.setTitle( "Delete");
                                iOSDialog.setSubtitle("Are you sure,you want to delete the Video?");
                                iOSDialog.setNegativeLabel("No");
                                iOSDialog.setPositiveLabel("Yes");
                                iOSDialog.setBoldPositiveLabel(true);

                                iOSDialog.setNegativeListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        iOSDialog.dismiss();
                                    }
                                });

                                iOSDialog.setPositiveListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //  Toast.makeText(MainActivity.this,"OK clicked",Toast.LENGTH_SHORT).show();

                                        apiCallTodelete(profileresponse.getUserData().getPortfolioVideo().get(position).getId());
                                        iOSDialog.dismiss();
                                    }
                                });
                                iOSDialog.show();
                                break;
                        }
                    }
                });
            }else{
            No_videos.setVisibility(View.VISIBLE);
            }
    }

    private void apiCallTodelete(String rawId) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(PortfolioVideoEditActivity.this).showLoadingDialog(PortfolioVideoEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(PortfolioVideoEditActivity.this).getApiService().deletePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_vid",
                rawId,
                "delete"
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("PortfolioVideoEditActivity.onResponse " + responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(PortfolioVideoEditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();
                                } else {
                                    Toast.makeText(PortfolioVideoEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PortfolioVideoEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void apiCallToUpdateProfileDatas() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(PortfolioVideoEditActivity.this).getApiService().getProfile(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setProfileResponse(responseString);
                                    setVideoDatas();
                                } else {
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCallToUpdateProfileDatas();
    }

}


