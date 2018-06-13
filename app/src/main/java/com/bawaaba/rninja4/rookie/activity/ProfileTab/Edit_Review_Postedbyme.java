package com.bawaaba.rninja4.rookie.activity.ProfileTab;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.EditReviewRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.model.profile.ReviewByMe;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.bawaaba.rninja4.rookie.utils.Constants;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Review_Postedbyme extends BaseActivity {

    private RecyclerView rvReviews;
    private ImagePicker imagePicker;
    private AppCompatTextView choose_file;
    private AppCompatTextView review_count;
    private AppCompatTextView review_posted;
    private SQLiteHandler db;
    private SessionManager session;
    private String imageRating = "";
    private static final int REQUEST_WRITE_PERMISSION = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int CAMERA_REQUEST_PROOF = 100;
    private static final int CAMERA_REQUEST_KEY_CODE = 110;
    private static int GALLERY_REQUEST_PROOF = 101;
    private static int GALLERY_REQUEST_KEY_CODE = 111;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    private Bitmap bitmap;
    private String imagebase = "";
    private TextView no_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__review__postedbyme);
        getSupportActionBar().hide();
        initViews();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        Intent from_profile = getIntent();
        final String verify = from_profile.getStringExtra("verify");
    }

    private void initViews() {

        rvReviews = (RecyclerView) findViewById(R.id.rvReviews);
        review_count = (AppCompatTextView) findViewById(R.id.review_count);
        review_posted = (AppCompatTextView) findViewById(R.id.post_review);
        no_review=(TextView)findViewById(R.id.no_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(layoutManager);
        setData();
    }

    private void setData() {

        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        String count_review = String.valueOf(profileresponse.getUserData().getReviewByMe().size());
        Log.e("countreview", count_review);
        review_count.setText("(" + count_review + " Reviews)");
        //review_posted.setPaintFlags(review_posted.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        if (profileresponse.getUserData().getReviewByMe().size() > 0) {
            EditReviewRecyclerviewAdapter adapter = new EditReviewRecyclerviewAdapter(getApplicationContext(), profileresponse.getUserData().getReviewByMe());
            no_review.setVisibility(View.GONE);
            rvReviews.setAdapter(adapter);

            adapter.setOnClickListener(new EditReviewRecyclerviewAdapter.AdvertisementsRecyclerViewClickListener() {
                @Override
                public void onClicked(final int position, View v) {
                    switch (v.getId()) {
                        case R.id.ivDeleteFile:
                            apiCallToDelete(profileresponse.getUserData().getReviewByMe().get(position));
                            break;
                        case R.id.ivEditFile:
                            final Dialog dialog = new Dialog(Edit_Review_Postedbyme.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.add_review);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT);

                            choose_file = (AppCompatTextView) dialog.findViewById(R.id.choose_file);
                            final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating);
                            final EditText review_text = (EditText) dialog.findViewById(R.id.review);
                            final ImageView pic_file = (ImageView) dialog.findViewById(R.id.pick_file);
                            final Button Submit = (Button) dialog.findViewById(R.id.btnreview);

                            ratingBar.setRating(Float.parseFloat(profileresponse.getUserData().getReviewByMe().get(position).getRating()));
                            review_text.setText(profileresponse.getUserData().getReviewByMe().get(position).getReview());



                            pic_file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // pickImageSingle();
                                    int result = ContextCompat.checkSelfPermission(Edit_Review_Postedbyme.this, Manifest.permission.CAMERA);
                                    int result1 = ContextCompat.checkSelfPermission(Edit_Review_Postedbyme.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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

                                private void takePhoto() {

                                    try {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Review_Postedbyme.this);
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
                                    ActivityCompat.requestPermissions(Edit_Review_Postedbyme.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_WRITE_PERMISSION);

                                }


                                private void buildAlertMessageNoCamera() {
                                    ActivityCompat.requestPermissions(Edit_Review_Postedbyme.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            MY_PERMISSIONS_REQUEST_CAMERA);

                                }
                            });

                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                    float rate = ratingBar.getRating();
                                    System.out.println("ProfileView.onRatingChanged " + rate);
                                }
                            });

                            Submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String review = review_text.getText().toString().trim();
                                    float rate = ratingBar.getRating();
                                    String rating = String.valueOf((int) rate);

                                    if (review != null && review.length() < 10) {
                                        review_text.setBackgroundResource(R.drawable.red_alert);
                                        Toast.makeText(Edit_Review_Postedbyme.this, "Minimum 10 characters required for review", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    //  String review_image = ;

                                    if (!review.isEmpty() && !rating.isEmpty()) {

                                        updateRewiew(profileresponse.getUserData().getReviewByMe().get(position), rating, rate, review, dialog);
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }

                            });

                            dialog.show();

                            break;
                        default:
                            break;
                    }
                }
            });
        }else{
            no_review.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_PROOF) {
            final Bitmap photo;
            try {
                photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;
                imagebase = getStringImage(bitmap);
                File photopath = null;
                photopath = saveToInternalStorage(photo);
                choose_file.setText("1 item selected");

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
                imagebase = getStringImage(bitmap);
                choose_file.setText("1 item selected");

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
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateRewiew(final ReviewByMe reviewByMe, final String rating, final float rate, final String review, final Dialog dialog1) {
        String image = "";
        if (TextUtils.isEmpty(imagebase)) {
            Log.e("imagecheck", "image no");
            image = "no";
        } else {

            image = "yes";
            Log.e("imagecheck", "image yes");
        }

//        URL url = null;

//        try {
//            url = new URL(imageRating);
//            Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            //imageBase = getStringImage(imageBitmap);
//        } catch (MalformedURLException e) {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Edit_Review_Postedbyme.this).showLoadingDialog(Edit_Review_Postedbyme.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Edit_Review_Postedbyme.this).getApiService().deleteReview(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),

                reviewByMe.getId(),
                rating,
                review,
                imagebase,
                "update",
                image
        );
        final String finalImage = image;
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
                                Log.e("review_image_prblm", finalImage);
                                if (!jsonObject.getBoolean("error")) {

                                    Toast.makeText(Edit_Review_Postedbyme.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                    apiCallToUpdateProfileDatas();

                                } else {
                                    Toast.makeText(Edit_Review_Postedbyme.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Edit_Review_Postedbyme.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apiCallToDelete(ReviewByMe reviewByMe) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(Edit_Review_Postedbyme.this).showLoadingDialog(Edit_Review_Postedbyme.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Edit_Review_Postedbyme.this).getApiService().deleteReview(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                reviewByMe.getId(),
                "",
                "",
                "",
                "delete", ""
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
                                    Toast.makeText(Edit_Review_Postedbyme.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();
                                } else {
                                    Toast.makeText(Edit_Review_Postedbyme.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Edit_Review_Postedbyme.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void apiCallToUpdateProfileDatas() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Edit_Review_Postedbyme.this).getApiService().getProfile(
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
                                    setData();
                                } else {
                                    Toast.makeText(Edit_Review_Postedbyme.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
