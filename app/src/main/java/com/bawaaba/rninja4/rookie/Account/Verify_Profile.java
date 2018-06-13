package com.bawaaba.rninja4.rookie.Account;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bawaaba.rninja4.rookie.App.AppConfig.URL_VERIFY_PROFILE;

public class Verify_Profile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Verify_Profile.class.getSimpleName();
    private static final int REQUEST_WRITE_PERMISSION = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int CAMERA_REQUEST_PROOF = 100;
    private static final int CAMERA_REQUEST_KEY_CODE = 110;
    private static int GALLERY_REQUEST_PROOF = 101;
    private static int GALLERY_REQUEST_KEY_CODE = 111;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    private TextView verify_profile;
    private TextView keykode;
    private Button keybutton;
    private Button save;
    private ImageView proof;
    private ImageView keycode;
    private ImageView proof_buttton;
    private ImageView keycode_button;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private SQLiteHandler db;
    private SessionManager session;
    private String imageProof = "";
    private String imageKeycode = "";
    private AppCompatTextView tvSave;
    private AppCompatTextView proof_edit;
    private AppCompatTextView keykode_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__profile);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitile_layout7);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));

        getSupportActionBar().hide();
        verify_profile = (TextView) findViewById(R.id.verify_profile);
        keykode = (TextView) findViewById(R.id.keykode);
        keybutton = (Button) findViewById(R.id.keybutton);
        proof_edit=(AppCompatTextView)findViewById(R.id.proof_edit);
        keykode_edit=(AppCompatTextView)findViewById(R.id.keycode_edit);

//        Intent from_profile_settings = getIntent();
//        final String verify =from_profile_settings.getStringExtra("verify");
//        Log.e("verifyyyy",verify);
        //keybutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient1));

        save = (Button) findViewById(R.id.save);
        tvSave = (AppCompatTextView) findViewById(R.id.tvSave);
        tvSave.setOnClickListener(this);
        proof_buttton = (ImageView) findViewById(R.id.proof_button);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String verify_code = user.get("verify_code");

        keybutton.setText(verify_code);

        keycode_button = (ImageView) findViewById(R.id.keycode_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id_document = getStringImage(bitmap);
                String keycode_img = getStringImage(bitmap1);


                if (!id_document.isEmpty() && !keycode_img.isEmpty()) {
                    verifyProfile(id_document, keycode_img);

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        proof_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result = ContextCompat.checkSelfPermission(Verify_Profile.this, Manifest.permission.CAMERA);
                int result1 = ContextCompat.checkSelfPermission(Verify_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        alert();
                    }
                } else {
                    buildAlertMessageNoCamera();
                }
            }
        });

        keycode_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int result = ContextCompat.checkSelfPermission(Verify_Profile.this, Manifest.permission.CAMERA);
                int result1 = ContextCompat.checkSelfPermission(Verify_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        takePhotoKeycode();
                    } else {
                        alert();
                    }
                } else {
                    buildAlertMessageNoCamera();
                }
            }
        });
    }

    private void takePhotoKeycode() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(Verify_Profile.this);
            builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //    buildAlertMessageNoCamera();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_KEY_CODE);
                        //Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);

                    } else if (items[item].equals(Constants.ProfileFragment.FROM_LIBRARY)) {

                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryintent, GALLERY_REQUEST_KEY_CODE);

                    } else if (items[item].equals(Constants.ProfileFragment.CANCEL)) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void takePhoto() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(Verify_Profile.this);
            builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //    buildAlertMessageNoCamera();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROOF);
                        //Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);

                    } else if (items[item].equals(Constants.ProfileFragment.FROM_LIBRARY)) {

                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryintent, GALLERY_REQUEST_PROOF);

                    } else if (items[item].equals(Constants.ProfileFragment.CANCEL)) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alert() {
        ActivityCompat.requestPermissions(Verify_Profile.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);
    }
    private void buildAlertMessageNoCamera() {
        ActivityCompat.requestPermissions(Verify_Profile.this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_PROOF) {
            final Bitmap photo;
            try {
                photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;
                imageProof = getStringImage(bitmap);
                File photopath = null;
                photopath = saveToInternalStorage(photo);
                proof_edit.setText("1 item selected");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_REQUEST_PROOF && (data != null)) {
            try {
                Uri selected_image = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(selected_image, filepath, null, null, null);
                cursor.moveToFirst();
                int columIntex = cursor.getColumnIndex(filepath[0]);
                String filePath = cursor.getString(columIntex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                bitmap = yourSelectedImage;
                imageProof = getStringImage(bitmap);
                proof_edit.setText("1 item selected");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_REQUEST_KEY_CODE) {
            final Bitmap photo;
            try {
                photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;
                imageKeycode = getStringImage(bitmap);
                File photopath = null;
                photopath = saveToInternalStorage(photo);
                keykode_edit.setText("1 item selected");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_REQUEST_KEY_CODE && (data != null)) {
            try {
                Uri selected_image = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(selected_image, filepath, null, null, null);
                cursor.moveToFirst();
                int columIntex = cursor.getColumnIndex(filepath[0]);
                String filePath = cursor.getString(columIntex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                bitmap = yourSelectedImage;
                imageKeycode = getStringImage(bitmap);
                keykode_edit.setText("1 item selected");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private File saveToInternalStorage(Bitmap bitmap) throws IOException {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
        }
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
        Random rand = new Random();
        int num = rand.nextInt(5000) + 1;
        File path = new File(directory, "" + num + ".jpg");
        FileOutputStream stream = null;


        try {
            stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return path;
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
             Log.e(TAG, "bitmap compression");
             proof = (ImageView) findViewById(R.id.proof_edit);
             bitmap = (Bitmap) data.getExtras().get("data");
             proof.setImageBitmap(bitmap);


         } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
             keycode = (ImageView) findViewById(R.id.keycode_edit);
             Log.e(TAG, "bitmap compression2");
             bitmap1 = (Bitmap) data.getExtras().get("data");
             keycode.setImageBitmap(bitmap1);
         }
     }
 */
    private void verifyProfile(final String id_document, final String keycode_img) {

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String user_id = user.get("uid");
        final String token = user.get("token");

        String tag_string_req = "req_verify_profile";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_VERIFY_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    Log.e(TAG, response);
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(),
                                "your profile is verified", Toast.LENGTH_LONG).show();

//                                Intent to_profile = new Intent(EditDetails.this, ProfileView.class);
//                                startActivity(to_profile);
//                                finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "new", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);
                params.put("id_document", id_document);
                params.put("keycode_img", keycode_img);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key", "123321");
                headers.put("Token", token);
                headers.put("User-Id", user_id);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSave:
                if (!TextUtils.isEmpty(imageProof) && !TextUtils.isEmpty(imageKeycode))
                    apiCallForVerifyProfile();
                else
                    Toast.makeText(this, "Please upload required documents.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void apiCallForVerifyProfile() {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Verify_Profile.this).showLoadingDialog(Verify_Profile.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Verify_Profile.this).getApiService().verifyProfile(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                imageProof,
                imageKeycode
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                System.out.println("ProfileView.onResponse " + responseString);

                                Log.e("verify value",responseString);
                                if (!jsonObject.getBoolean("error")) {
                                    Intent to_verify = new Intent(Verify_Profile.this, Verify_profil2.class);
                                    startActivity(to_verify);
                                    finish();
                                    Toast.makeText(Verify_Profile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Verify_Profile.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Verify_Profile.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Verify_Profile.this, Profile_settings.class);
//        startActivity(intent);
//        finish();
//    }
}