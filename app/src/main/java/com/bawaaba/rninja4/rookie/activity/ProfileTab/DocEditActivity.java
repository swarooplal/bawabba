package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.bawaaba.rninja4.rookie.activity.adapters.FileEditRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.DocAddActivity;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocEditActivity extends AppCompatActivity  implements View.OnClickListener{

    private RecyclerView rvFileData;
    private AppCompatTextView tvAddFile;
    private Button no_delete;
    private Button yes_delete;
    private AppCompatTextView No_Doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_edit);

        getSupportActionBar().hide();
        initViews();
    }
    private void initViews() {
        rvFileData = (RecyclerView) findViewById(R.id.rvFileData);
        tvAddFile = (AppCompatTextView) findViewById(R.id.tvAddFile);
        No_Doc= (AppCompatTextView) findViewById(R.id.pdf_count);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvFileData.setLayoutManager(layoutManager);
        tvAddFile.setOnClickListener(this);

        setDocDetails();
    }

    private void setDocDetails() {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (profileresponse.getUserData().getPortfolioDoc().size() > 0) {
            No_Doc.setVisibility(View.GONE);
            FileEditRecyclerviewAdapter fIleEditRecyclerviewAdapter = new FileEditRecyclerviewAdapter(getApplicationContext(), profileresponse.getUserData().getPortfolioDoc());
            rvFileData.setAdapter(fIleEditRecyclerviewAdapter);
            fIleEditRecyclerviewAdapter.setOnClickListener(new FileEditRecyclerviewAdapter.AdvertisementsRecyclerViewClickListener() {
                @Override
                public void onClicked(final int position, View v) {
                    switch (v.getId()) {
                        case R.id.ivDeleteFile:

                            final iOSDialog iOSDialog = new iOSDialog(DocEditActivity.this);
                            iOSDialog.setTitle( "Delete");
                            iOSDialog.setSubtitle("Are you sure,you want to delete the File?");
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

                                    apiCallTodelete(profileresponse.getUserData().getPortfolioDoc().get(position).getId());
                                    iOSDialog.dismiss();
                                }
                            });
                            iOSDialog.show();




                            break;
                        case R.id.ivEditFile:
                            showDialogToUpdate(profileresponse.getUserData().getPortfolioDoc().get(position).getTitle(), profileresponse.getUserData().getPortfolioDoc().get(position).getThumbnail(), profileresponse.getUserData().getPortfolioDoc().get(position).getId());
                            break;
                    }
                }
            });
        }else{
            No_Doc.setVisibility(View.VISIBLE);
        }
    }

    private void showDialogToUpdate(final String title, String thumbnail, final String id) {
        final Dialog dialog = new Dialog(DocEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_tittle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // ImageView ivThumbnail = (ImageView) dialog.findViewById(R.id.ivThumbnail);
        final AppCompatEditText tvTitle = (AppCompatEditText) dialog.findViewById(R.id.tvTitle);
        AppCompatButton btnSave = (AppCompatButton) dialog.findViewById(R.id.btnSave);
//        Glide.with(dialog.getContext()).load(thumbnail).into(ivThumbnail);
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
        final Dialog dialog = ObjectFactory.getInstance().getUtils(DocEditActivity.this).showLoadingDialog(DocEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(DocEditActivity.this).getApiService().updatePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_doc",
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
                                    Toast.makeText(DocEditActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                    if (dialogUpdate != null) {
                                        dialogUpdate.dismiss();
                                    }
                                    apiCallToUpdateProfileDatas();
                                } else {
                                    Toast.makeText(DocEditActivity.this,"Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DocEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void apiCallTodelete(String rawId) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(DocEditActivity.this).showLoadingDialog(DocEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(DocEditActivity.this).getApiService().deletePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_doc",
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
                                    Toast.makeText(DocEditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();

                                } else {
                                    Toast.makeText(DocEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DocEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddFile:
                Intent intent = new Intent(DocEditActivity.this, DocAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCallToUpdateProfileDatas();
    }

    private void apiCallToUpdateProfileDatas() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(DocEditActivity.this).getApiService().getProfile(
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
                                    setDocDetails();
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

}
