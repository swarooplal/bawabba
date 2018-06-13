package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioAddActivity  extends BaseActivity implements View.OnClickListener, AudioPickerCallback {

    private AppCompatEditText tvTttle;
    private AppCompatTextView tvNumberofItemsSelected;
    private ImageView ivSelectFromGallery;
    private AppCompatButton btnSave;
    private Uri uri = null;
    private File file;
    private String selectedPath = "";
    private AudioPicker audioPicker;
    int selectedPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_add);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {

        tvTttle = (AppCompatEditText) findViewById(R.id.tvTttle);
        tvNumberofItemsSelected = (AppCompatTextView) findViewById(R.id.tvNumberofItemsSelected);
        ivSelectFromGallery = (ImageView) findViewById(R.id.ivSelectFromGallery);
        btnSave = (AppCompatButton) findViewById(R.id.btnSave);

        ivSelectFromGallery.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSave:
                if(tvTttle.length()==0 && tvNumberofItemsSelected.length()==0){
                    Toast.makeText(AudioAddActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                }else {
                    apiCallToSaveAudioFile();
                }
                break;
            case R.id.ivSelectFromGallery:
                audioPicker = getAudioPicker();
                audioPicker.pickAudio();

                break;
            default:
                break;
        }
    }
    private AudioPicker getAudioPicker() {
        audioPicker = new AudioPicker(this);
        audioPicker.setAudioPickerCallback(this);
        return audioPicker;
    }

    private void apiCallToSaveAudioFile() {
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

        RequestBody tittle =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), tvTttle.getText().toString().trim());

        Log.e("title audiochecking", String.valueOf(tvTttle));

        RequestBody table_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "portfolio_aud");


//        System.out.println("AudioAddActivity.apiCallToSaveAudioFile " + file.getAbsolutePath());
//        System.out.println("AudioAddActivity.apiCallToSaveAudioFile " + file.getTotalSpace());


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagenPerfil = MultipartBody.Part.createFormData("media_file", file.getName(), requestFile);


        final Dialog dialog = ObjectFactory.getInstance().getUtils(AudioAddActivity.this).showLoadingDialog(AudioAddActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(AudioAddActivity.this).getApiService().uploadAudioFile(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                userId,
                tittle,
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
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("AddServiceActivity.onResponse" + responseString);
                            if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(AudioAddActivity.this, "Successfully added.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AudioAddActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AudioAddActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Picker.PICK_AUDIO && resultCode == RESULT_OK) {
            audioPicker.submit(data);
            tvNumberofItemsSelected.setText( " 1 item selected");
        }

    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onAudiosChosen(List<ChosenAudio> list) {
        System.out.println("AudioAddActivity.onAudiosChosen " + list.get(0).getOriginalPath());
        file = new File(list.get(0).getOriginalPath());

    }

    @Override
    public void onError(String s) {

    }

}
