package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.Edit_Audio_Data;
import com.bawaaba.rninja4.rookie.activity.adapters.AudioListingRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.google.gson.Gson;
import com.kcode.bottomlib.BottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioEditActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvAudioData;
    private AppCompatTextView tvAddAudio;
    private List<Edit_Audio_Data> audioData = new ArrayList<>();
    private Button no_delete;
    private Button yes_delete;
    private AppCompatTextView No_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_edit);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        rvAudioData = (RecyclerView) findViewById(R.id.rvAudioData);
        tvAddAudio = (AppCompatTextView) findViewById(R.id.tvAddAudio);
        No_audio=(AppCompatTextView) findViewById(R.id.audio_count);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvAudioData.setLayoutManager(layoutManager);
        tvAddAudio.setOnClickListener(this);
        setAudioData();
    }
    private void setAudioData() {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);

        audioData = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getAudioData();
        if (profileresponse.getUserData().getPortfolioAudio().size() > 0) {
            No_audio.setVisibility(View.GONE);
            AudioListingRecyclerviewAdapter adapter = new AudioListingRecyclerviewAdapter(getApplicationContext(), profileresponse.getUserData().getPortfolioAudio());
            rvAudioData.setAdapter(adapter);

            adapter.setOnClickListener(new AudioListingRecyclerviewAdapter.AdvertisementsRecyclerViewClickListener() {
                @Override
                public void onClicked(final int position, View v) {
                    switch (v.getId()) {
                        case R.id.tvDeleteAudio:
//                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AudioEditActivity.this);
//                            View mView = getLayoutInflater().inflate(R.layout.dialogue_delete_audio, null);
//
//                            no_delete=(Button)mView.findViewById(R.id.no_button);
//                            yes_delete=(Button)mView.findViewById(R.id.yes_button);
//
//                            mBuilder.setView(mView);
//                            final AlertDialog dialog = mBuilder.create();
//                            dialog.show();
//                            yes_delete.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Log.e("image delete","yesbutton");
//                                    apiCallTodelete(profileresponse.getUserData().getPortfolioAudio().get(position).getId());
//                                    dialog.dismiss();
//                                }
//                            });
//
//                            no_delete.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog.dismiss();
//                                }
//                            });
                            final iOSDialog iOSDialog = new iOSDialog(AudioEditActivity.this);
                            iOSDialog.setTitle( "Delete");
                            iOSDialog.setSubtitle("Are you sure,you want to delete the Audio?");
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

                                    apiCallTodelete(profileresponse.getUserData().getPortfolioAudio().get(position).getId());
                                    iOSDialog.dismiss();
                                }
                            });
                            iOSDialog.show();
                            break;
                        case R.id.ivEditAudio:
                            showDialogToUpdate(profileresponse.getUserData().getPortfolioAudio().get(position).getTitle(), "", profileresponse.getUserData().getPortfolioAudio().get(position).getId());
                            break;
                    }
                }
            });
        }else{
            No_audio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvAddAudio:
                BottomDialog dialog = BottomDialog.newInstance("Select", "Cancel", new String[]{"From Gallery", "From URL"
                });
                dialog.show(getSupportFragmentManager(), "dialog");
                //add item click listener
                dialog.setListener(new BottomDialog.OnClickListener() {
                    @Override
                    public void click(int position) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), AudioAddActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(getApplicationContext(), AudioUrlActivity.class);
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }

                    }
                });
                break;
        }
    }

    private void showDialogToUpdate(final String title, String thumbnail, final String id) {
        final Dialog dialog = new Dialog(AudioEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_tittle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //ImageView ivThumbnail = (ImageView) dialog.findViewById(R.id.ivThumbnail);
        final AppCompatEditText tvTitle = (AppCompatEditText) dialog.findViewById(R.id.tvTitle);
        AppCompatButton btnSave = (AppCompatButton) dialog.findViewById(R.id.btnSave);
        tvTitle.setText(title);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCallToUpdateContent(tvTitle.getText().toString().trim(), id, dialog);
            }
        });
        dialog.show();
    }

    private void apiCallToUpdateContent(String trim, final String id, final Dialog dialogUpdate) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(AudioEditActivity.this).showLoadingDialog(AudioEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AudioEditActivity.this).getApiService().updatePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_aud",
                id,
                "update",
                trim.toString()
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
                                    Toast.makeText(AudioEditActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();
                                    if (dialogUpdate != null) {
                                        dialogUpdate.dismiss();
                                    }
                                } else {
                                    Toast.makeText(AudioEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                    if (dialogUpdate != null) {
                                        dialogUpdate.dismiss();
                                    }
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
                Toast.makeText(AudioEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void apiCallTodelete(String rawId) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(AudioEditActivity.this).showLoadingDialog(AudioEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AudioEditActivity.this).getApiService().deletePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_aud",
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
                                    Toast.makeText(AudioEditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();

                                } else {
                                    Toast.makeText(AudioEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AudioEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        apiCallToUpdateProfileDatas();
    }

    private void apiCallToUpdateProfileDatas() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AudioEditActivity.this).getApiService().getProfile(
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
                                    //  Toast.makeText(AudioEditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setProfileResponse(responseString);
                                    setAudioData();
                                } else {
                                    Toast.makeText(AudioEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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

}
