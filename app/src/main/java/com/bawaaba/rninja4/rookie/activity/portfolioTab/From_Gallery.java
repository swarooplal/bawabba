package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;

public class From_Gallery extends AppCompatActivity implements View.OnClickListener {

    private EditText Audio_Title;
    private TextView Audio_File_name;
    private Button Audio_choose;
    public Button Save;
    private TextView textViewResponse;
    private SQLiteHandler db;

    private static final int SELECT_AUDIO = 3;
    private String selectedPath;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from__gallery);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actiontitle_layout18);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        actionBar.setHomeButtonEnabled(true);

        ActivityCompat.requestPermissions(From_Gallery.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


        Audio_Title = (EditText) findViewById(R.id.audio_titlename);
        Audio_File_name = (TextView) findViewById(R.id.galery_text);
        Audio_choose = (Button) findViewById(R.id.galery_button);
        Save=(Button)findViewById(R.id.galery_save);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        Audio_choose.setOnClickListener(this);
        Save.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                } else {
                    Toast.makeText(From_Gallery.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    private void chooseAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Audio "), SELECT_AUDIO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_AUDIO) {
                System.out.println("SELECT_AUDIO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                Audio_File_name.setText(selectedPath);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.moveToFirst();
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        cursor.close();
Log.e("pathcheck",path);
        return path;
    }



    private void uploadAudio() {
        class UploadAudio extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                uploading = ProgressDialog.show(From_Gallery.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();

                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {

                final Upload u = new Upload();
                String msg = u.uploadAudio(selectedPath);
                Log.e("selecte_path",selectedPath);

                return msg;

            }

        }
        UploadAudio uv = new UploadAudio();
        uv.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == Audio_choose) {
            chooseAudio();
        }
        if (v == Save) {
            uploadAudio();
        }
    }
}


