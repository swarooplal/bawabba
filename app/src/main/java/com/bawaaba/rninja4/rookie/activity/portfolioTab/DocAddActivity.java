package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocAddActivity extends AppCompatActivity implements View.OnClickListener, FilePickerCallback {

    private AppCompatEditText tvTttle;
    private AppCompatTextView tvNumberofItemsSelected;
    private AppCompatTextView btnSave;
    private ImageView ivSelectFromGallery;
    private FilePicker filePicker;
    private String encodedFile = "";
    private LinearLayout llFile;
    private ImageView tvFileIcon;
    private AppCompatTextView tvFileName;
    private LinearLayout llUrl;
    private AppCompatButton tvAddMore;

    private ArrayList<AppCompatEditText> urlEditTexts = new ArrayList<>();
    private ArrayList<AppCompatTextView> urletAddTittle = new ArrayList<AppCompatTextView>();
    private ArrayList<ImageView> ivDeletes = new ArrayList<ImageView>();
    private List<ChosenFile> selectedFileList = new ArrayList<>();
    List<String> encodedFileList = new ArrayList<>();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_add);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {

        llUrl = (LinearLayout) findViewById(R.id.llUrl);
        tvAddMore = (AppCompatButton) findViewById(R.id.tvAddMore);
        btnSave = (AppCompatTextView) findViewById(R.id.btnSave);
//        ivSelectFromGallery = (ImageView) findViewById(R.id.ivSelectFromGallery);
        tvAddMore.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        //  ivSelectFromGallery.setOnClickListener(this);

//        llFile = (LinearLayout) findViewById(R.id.llFile);
//        tvFileIcon = (ImageView) findViewById(R.id.tvFileIcon);
//        tvFileName = (AppCompatTextView) findViewById(R.id.tvFileName);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvAddMore:
                addUrls();
                break;
            case R.id.btnSave:
                if (ifValid()) {
                    apiCall();
                } else {
                    //Toast.makeText(this, "No files selected", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.ivSelectFromGallery:
//                filePicker = getFilePicker();
//                filePicker.setMimeType("application/pdf");
//                filePicker.pickFile();
//                break;
//            default:
//                break;
        }
    }

    private boolean ifValid() {
        boolean status = true;
        for (int i = 0; i <urlEditTexts.size(); i++) {
            if (TextUtils.isEmpty(urlEditTexts.get(i).getText().toString())) {
                status = false;
               // urlEditTexts.get(i).setError("Cant be empty");
                urlEditTexts.get(i).setBackgroundResource(R.drawable.red_alert_round);
                urletAddTittle.get(i).setBackgroundResource(R.drawable.red_alert_round);
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            }


        }
        if (selectedFileList.size() == 0) {
            status = false;
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    private void addUrls() {

        final View view = DocAddActivity.this.getLayoutInflater().inflate(R.layout.layout_add_pdf, null);
        final AppCompatEditText tvTttle = (AppCompatEditText)view.findViewById(R.id.etAddTittle);
        final AppCompatTextView tvNumberofItemsSelected = (AppCompatTextView)view.findViewById(R.id.tvNumberofItemsSelected);
        ImageView ivSelectFromGallery = (ImageView) view.findViewById(R.id.ivSelectFromGallery);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        urlEditTexts.add(tvTttle);
        urletAddTittle.add(tvNumberofItemsSelected);
        ivDeletes.add(ivDelete);
        ivDelete.setVisibility(View.GONE);
        //ivSelectFromGallery.setOnClickListener(this);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                urlEditTexts.remove(tvTttle);
                urletAddTittle.remove(tvNumberofItemsSelected);
                //selectedFileList.remove((selectedFileList.size() - 1));
                llUrl.removeView(view);

            }
        });

        ivSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker = getFilePicker();
                filePicker.setMimeType("application/pdf");
                filePicker.pickFile();
            }
        });
       ivDeletes.get(ivDeletes.size() - 1).setVisibility(View.VISIBLE);

        llUrl.addView(view);
    }

    private void apiCall() {

        HashMap<String, String> postvalue = new HashMap<>();

        RequestBody userId =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId()));

        for (int i = 0; i < selectedFileList.size(); i++) {
            postvalue.put("document_file[" + i + "]", getStringFile(new File(selectedFileList.get(i).getOriginalPath())));
            postvalue.put("title[" + i + "]", urlEditTexts.get(i).getText().toString().trim());
        }

        final Dialog dialog = ObjectFactory.getInstance().getUtils(DocAddActivity.this).showLoadingDialog(DocAddActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(DocAddActivity.this).getApiService().uploadFile(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(), postvalue

        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            System.out.println("DocAddActivity.onResponse " + responseString);
                            if (jsonObject != null) {
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(DocAddActivity.this, "Document Added", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(DocAddActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
//                progressDialog.dismiss();
                Toast.makeText(DocAddActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private FilePicker getFilePicker() {
        filePicker = new FilePicker(this);
        filePicker.setFilePickerCallback(this);
        Log.e("encodepdf", String.valueOf(filePicker));

        return filePicker;
    }

    @Override
    public void onFilesChosen(List<ChosenFile> list) {

        final AppCompatTextView tvNumberofItemsSelected = (AppCompatTextView)findViewById(R.id.tvNumberofItemsSelected);

        System.out.println("DocAddActivity.onFilesChosen " + list.get(0).getOriginalPath());
        selectedFileList.add(list.get(0));
        encodedFileList.add(getStringFile(new File(list.get(0).getOriginalPath())));

        tvNumberofItemsSelected.setText("1 file selected");
        encodedFile = getStringFile(new File(list.get(0).getOriginalPath()));
        Log.e("encodepdf",encodedFile);

        // llFile.setVisibility(View.VISIBLE);
//        tvFileName.setText(list.get(0).getDisplayName());
//        tvFileIcon.setImageResource(R.drawable.pdf);
    }

    @Override
    public void onError(String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_FILE && resultCode == RESULT_OK) {
            filePicker.submit(data);
            Log.e("encodepdf", String.valueOf(filePicker));



        }
    }

    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        Log.e("Encodedfile",encodedFile);
        return lastVal;
    }

}
