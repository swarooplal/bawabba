package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.activity.adapters.ImagesAsGridRecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import org.apache.commons.io.FileUtils;


public class ImageEditActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rvImages;
    private AppCompatTextView tvAddPhotos;
    private Button no_delete;
    private Button yes_delete;
    private AppCompatTextView No_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

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
                        Intent to_main = new Intent(ImageEditActivity.this, MainActivity.class);
                        startActivity(to_main);
                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(ImageEditActivity.this, SearchActivity.class);
                        startActivity(to_search);
                        finish();
                        break;
                    case 2:
                        Intent to_inbox = new Intent(ImageEditActivity.this, ChatActivity.class);
                        startActivity(to_inbox);
                        finish();
                        break;
                    case 3:
                        Intent to_profile = new Intent(ImageEditActivity.this, ProfileView.class);
                        startActivity(to_profile);
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

        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        rvImages = (RecyclerView) findViewById(R.id.rvImages);
        tvAddPhotos = (AppCompatTextView) findViewById(R.id.tvAddPhotos);
        No_images=(AppCompatTextView)findViewById(R.id.image_count);
        rvImages.setLayoutManager(new GridLayoutManager(ImageEditActivity.this, 2));
        setImageList();
        tvAddPhotos.setOnClickListener(this);
    }

    private void setImageList() {
        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
        if (profileresponse.getUserData().getPortfolioImage() != null)
            if (profileresponse.getUserData().getPortfolioImage().size() >0) {
                No_images.setVisibility(View.GONE);
                ImagesAsGridRecyclerviewAdapter adapter = new ImagesAsGridRecyclerviewAdapter(getApplicationContext(), profileresponse.getUserData().getPortfolioImage());
                rvImages.setAdapter(adapter);

                adapter.setOnClickListener(new ImagesAsGridRecyclerviewAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClicked(final int position, View v) {
                        switch (v.getId()) {
                            case R.id.ivDelete:
//
                                final iOSDialog iOSDialog = new iOSDialog(ImageEditActivity.this);
                                iOSDialog.setTitle( "Delete");
                                iOSDialog.setSubtitle("Are you sure,you want to delete the image?");
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

                                        apiCallTodelete(profileresponse.getUserData().getPortfolioImage().get(position).getId());
                                        iOSDialog.dismiss();
                                    }
                                });
                                iOSDialog.show();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }else{
                No_images.setVisibility(View.VISIBLE);
            }
    }
    private void apiCallTodelete(String rawId) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(ImageEditActivity.this).showLoadingDialog(ImageEditActivity.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ImageEditActivity.this).getApiService().deletePortfolio(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                "portfolio_img",
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
                                    Toast.makeText(ImageEditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();
                                } else {
                                    Toast.makeText(ImageEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ImageEditActivity.this, "failed..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvAddPhotos:
                selectMultipleImages();
                break;
            default:
                break;
        }
    }

    private void selectMultipleImages() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
     //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            List<MultipartBody.Part> parts = new ArrayList<>();
            RequestBody description = createPartFromString(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());

  /*          for (int i = 0, l = images.size(); i < l; i++) {
                stringBuffer.append(images.get(i).path + "\n");
                System.out.println("ImageEditActivity.onActivityResult " + images.get(i).path);
                Uri myUri = Uri.parse(images.get(i).path.replaceAll(" ", "%20"));
                if (images.get(i).path != null) {
                    parts.add(prepareFilePart("photo[" + i + "]", myUri));
                }

            }*/
            RequestBody userId =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), String.valueOf(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId()));
            for (int i = 0; i < images.size(); i++) {
                File file = new File(String.valueOf(Uri.parse(images.get(i).path.replaceAll(" ", "%20"))));

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagenPerfil = MultipartBody.Part.createFormData("portfolio_img[" + i + "]", file.getName(), requestFile);
                parts.add(imagenPerfil);

                File file1 = new File(String.valueOf(Uri.parse(images.get(i).path.replaceAll(" ", "%20"))));
                             // long lengthinMb = file1.length() / 1024;
                long lengthinMb = file1.length();
                Log.e("imagesize", String.valueOf(lengthinMb));
            }
            final Dialog dialog = ObjectFactory.getInstance().getUtils(ImageEditActivity.this).showLoadingDialog(ImageEditActivity.this);
            dialog.show();
            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ImageEditActivity.this).getApiService().uploadImage(
                    "app-client",
                    "123321", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                    ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                    userId,
                    parts);
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
                                    Toast.makeText(ImageEditActivity.this, "Successfully added.", Toast.LENGTH_SHORT).show();
                                    apiCallToUpdateProfileDatas();
                                }else {
                                    Toast.makeText(ImageEditActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ImageEditActivity.this, "failed to load..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

//
//    @NonNull
//    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
//        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
//        // use the FileUtils to get the actual file by uri
//        File file = null;
//        try {
//            file = FileUtils.toFile(new URL(fileUri.toString()));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("ImageEditActivity.prepareFilePart " + fileUri);
////        File file = new File(String.valueOf(fileUri));
//        // create RequestBody instance from file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse(getContentResolver().getType(fileUri)), file);
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCallToUpdateProfileDatas();
    }
    private void apiCallToUpdateProfileDatas() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ImageEditActivity.this).getApiService().getProfile(
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
                                    setImageList();
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



