package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.SelectedVideoGridRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.Utilities;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAddActivity extends AppCompatActivity implements View.OnClickListener {

    int selectedPos = 0;
    private AppCompatTextView tvFromGallery;
    private AppCompatTextView tvFromURL;
    private AppCompatTextView tvNumberofItemsSelected;
    private ImageView ivSelectFromGallery;
    private View viewGallery;
    private View viewFromURL;
    private LinearLayout llFromUrl;
    private LinearLayout llFromGallery;
    private LinearLayout llUrl;
    private RecyclerView rvItemsList;
    private AppCompatButton tvAddMore;
    private AppCompatTextView tvAddVideos;
    private ArrayList<String> filePath = new ArrayList<>();
    private ArrayList<AppCompatEditText> urlEditTexts = new ArrayList<>();
    List<String> mPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_add);

        getSupportActionBar().hide();
        initViews();
        addUrls();
    }

    private void initViews() {
        tvFromGallery = (AppCompatTextView) findViewById(R.id.tvFromGallery);
        tvFromURL = (AppCompatTextView) findViewById(R.id.tvFromURL);
        tvAddVideos = (AppCompatTextView) findViewById(R.id.tvAddVideos);
        tvNumberofItemsSelected = (AppCompatTextView) findViewById(R.id.tvNumberofItemsSelected);
        ivSelectFromGallery = (ImageView) findViewById(R.id.ivSelectFromGallery);
        viewGallery = (View) findViewById(R.id.viewGallery);
        viewFromURL = (View) findViewById(R.id.viewFromURL);
        llFromUrl = (LinearLayout) findViewById(R.id.llFromUrl);
        llFromGallery = (LinearLayout) findViewById(R.id.llFromGallery);
        llUrl = (LinearLayout) findViewById(R.id.llUrl);
        rvItemsList = (RecyclerView) findViewById(R.id.rvItemsList);
        tvAddMore = (AppCompatButton) findViewById(R.id.tvAddMore);

        viewFromURL.setVisibility(View.GONE);
        llFromGallery.setVisibility(View.VISIBLE);
        llFromUrl.setVisibility(View.GONE);

        tvFromGallery.setOnClickListener(this);
        tvFromURL.setOnClickListener(this);
        ivSelectFromGallery.setOnClickListener(this);
        tvAddVideos.setOnClickListener(this);
        tvAddMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvFromGallery:
                viewGallery.setVisibility(View.VISIBLE);
                viewFromURL.setVisibility(View.GONE);
                llFromGallery.setVisibility(View.VISIBLE);
                llFromUrl.setVisibility(View.GONE);
                selectedPos = 0;
                break;
            case R.id.tvFromURL:
                viewGallery.setVisibility(View.GONE);
                viewFromURL.setVisibility(View.VISIBLE);
                llFromGallery.setVisibility(View.GONE);
                llFromUrl.setVisibility(View.VISIBLE);
                selectedPos = 1;
                break;
            case R.id.ivSelectFromGallery:
                if (Utilities.hasPermission(this, 11, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, "Need Permission", true)) {
//                    Pico.openMultipleFiles(VideoAddActivity.this, Pico.TYPE_VIDEO);
                    new VideoPicker.Builder(VideoAddActivity.this)
                            .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                            .directory(VideoPicker.Directory.DEFAULT)
                            .extension(VideoPicker.Extension.MP4)
                            .enableDebuggingMode(true)
                            .build();
                }
                break;
            case R.id.tvAddVideos:
                if (selectedPos == 0) {
                    if (mPaths.size() > 0)
                        loadFileApi();
                    else {
                        Toast.makeText(this, "Please upload video file.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addUrlsApi();
                }
                break;
            case R.id.tvAddMore:
                addUrls();
                break;
            default:
                break;

        }
    }

    private void adddelete() {
        View view = VideoAddActivity.this.getLayoutInflater().inflate(R.layout.layout_add_url, null);
        final ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        AppCompatEditText etAddUrl = (AppCompatEditText) view.findViewById(R.id.etAddUrl);
        urlEditTexts.remove(etAddUrl);
        llUrl.removeView(view);

    }

    private void addUrlsApi() {
        boolean status = true;
        for (int i = 0; i < urlEditTexts.size(); i++) {
            if (TextUtils.isEmpty(urlEditTexts.get(i).getText().toString().trim())) {
                // urlEditTexts.get(i).setError("");
                urlEditTexts.get(i).setBackgroundResource(R.drawable.red_alert_round);
                Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                status = false;
            }
        }
        if (status) {


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10 && grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED) {
            uploadVideo();
        } else if(requestCode==11 && grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED) {
            onClick(findViewById(R.id.ivSelectFromGallery));
        } else if(requestCode==12 && grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED) {
            loadFileApi();
        }
    }

    private void uploadVideo() {
        if (Utilities.hasPermission(this, 10, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, "Need Permission", true)) {
            HashMap<String, String> postvalue = new HashMap<>();
            for (int i = 0; i < urlEditTexts.size(); i++) {
//                postvalue.put("url[" + i + "]", "https://www.youtube.com/watch?v=JGwWNGJdvx8&app=desktop");
                postvalue.put("url[" + i + "]", urlEditTexts.get(i).getText().toString().trim());
                System.out.println("VideoAddActivity.addUrlsApi " + urlEditTexts.get(i).getText().toString().trim());
            }
            RequestBody userId =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId()));
            RequestBody uploadType =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "url");

            RequestBody table_name =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "portfolio_vid");
            final Dialog dialog = ObjectFactory.getInstance().getUtils(VideoAddActivity.this).showLoadingDialog(VideoAddActivity.this);
            dialog.show();
            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(VideoAddActivity.this).getApiService().uploadUrlVideo(
                    "app-client",
                    "123321",
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    "url",
                    postvalue,
                    "portfolio_vid"
            );


            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        try {
                            String responseString = new String(response.body().bytes());
                            Log.e("VIDEOACT","="+responseString);
                            if (responseString != null) {
                                JSONObject jsonObject = new JSONObject(responseString);
                                Log.e("videochecking", responseString);
                                System.out.println("AddServiceActivity.onResponse" + responseString);
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(VideoAddActivity.this, "Successfully added.", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(VideoAddActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("VIDEOACT","",e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("VIDEOACT", "=", t);
                    dialog.dismiss();
                    Toast.makeText(VideoAddActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addUrls() {
        final View view = VideoAddActivity.this.getLayoutInflater().inflate(R.layout.layout_add_url, null);
        final AppCompatEditText etAddUrl = (AppCompatEditText) view.findViewById(R.id.etAddUrl);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

        urlEditTexts.add(etAddUrl);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                urlEditTexts.remove(etAddUrl);
                llUrl.removeView(view);
            }
        });
        llUrl.addView(view);
    }

    private void loadFileApi() {
        if (Utilities.hasPermission(this, 12, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, "Need Permission", true)) {
            MultipartBody.Part parts = null;
            RequestBody userId =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId()));
            RequestBody uploadType =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "file");

            RequestBody url =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "no");
            RequestBody table_name =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "portfolio_vid");

   /*     for (int i = 0; i < filePath.size(); i++) {
            File file = new File(String.valueOf(Uri.parse(filePath.get(i)*//*.replaceAll(" ", "%20"))*//*)));

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagenPerfil = MultipartBody.Part.createFormData("media_file[" + i + "]",file.getName(), requestFile);
            parts=imagenPerfil;

        }*/

            File file = new File(String.valueOf(Uri.parse(mPaths.get(0)/*.replaceAll(" ", "%20"))*/)));

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagenPerfil = MultipartBody.Part.createFormData("media_file", file.getName(), requestFile);

            final Dialog dialog = ObjectFactory.getInstance().getUtils(VideoAddActivity.this).showLoadingDialog(VideoAddActivity.this);
            dialog.show();
            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(VideoAddActivity.this).getApiService().uploadVideoFile(
                    "app-client",
                    "123321",
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                    userId,
                    uploadType,
                    url,
                    table_name,
                    imagenPerfil);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        try {
                            String responseString = new String(response.body().bytes());
                            Log.e("VIDEOACT","="+responseString);
                            if (responseString != null) {
                                JSONObject jsonObject = new JSONObject(responseString);
                                System.out.println("AddServiceActivity.onResponse" + responseString);
                                if (!jsonObject.getBoolean("error")) {

                                    BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),null,null);
                                } else {
                                    Toast.makeText(VideoAddActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("VIDEOACT","=",e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("VIDEOACT","=",t);
                    dialog.dismiss();
                    Toast.makeText(VideoAddActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
          mPaths = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);
            tvNumberofItemsSelected.setText(mPaths.size() + " items selected");

            //loadSelected videos showing the selected video
            loadSelectedVideos(mPaths);
            //Your Code
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Pico.onActivityResult(this, requestCode, resultCode, data, new Pico.onActivityResultHandler() {
//
//            @Override
//            public void onActivityResult(List<File> files) {
//                filePath = new ArrayList<String>();
//
//                for (File file : files) {
//                    System.out.println("VideoAddActivity.onActivityResult " + file.getAbsolutePath());
//                    filePath.add(file.getAbsolutePath());
//                    StringBuffer stringBuffer = new StringBuffer();
//                }
//                tvNumberofItemsSelected.setText(filePath.size() + " items selected");
//
//                //loadSelected videos showing the selected video
//                loadSelectedVideos(files);
//            }
//
//            @Override
//            public void onFailure(Exception error) {
//                Log.e("ERROR", error.getMessage());
//                Toast.makeText(VideoAddActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void loadSelectedVideos(List<String > files) {
        rvItemsList.setLayoutManager(new GridLayoutManager(VideoAddActivity.this, 3));
        SelectedVideoGridRecyclerviewAdapter selectedVideoGridRecyclerviewAdapter = new SelectedVideoGridRecyclerviewAdapter(this, files);
        rvItemsList.setAdapter(selectedVideoGridRecyclerviewAdapter);
    }
}

