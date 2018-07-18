//package com.bawaaba.rninja4.rookie.activity.portfolioTab;
//
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.bawaaba.rninja4.rookie.R;
//import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
//import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//
//public class From_Url extends AppCompatActivity {
//
//    private String TAG =From_Url.class.getSimpleName();
//
//    private EditText Url_Title;
//    private EditText Url_text;
//    private Button Url_save;
//    private SQLiteHandler db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_from__url);
//
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout18);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
//        actionBar.setHomeButtonEnabled(true);
//
//        db = new SQLiteHandler(getApplicationContext());
//        HashMap<String, String> user = db.getUserDetails();
//        final String user_id = user.get("uid");
//        final String token = user.get("token");
//
//        Url_Title=(EditText)findViewById(R.id.audio_titlename);
//        Url_text=(EditText)findViewById(R.id.audio_url);
//        Url_save=(Button)findViewById(R.id.url_save);
//
//        Url_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String title= Url_Title.getText().toString().trim();
//                String url=Url_text.getText().toString().trim();
//
//                if(!title.isEmpty()&&! url.isEmpty()){
//                    add_audio(title,url,user_id);
//                }else {
//
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter your details!", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//
//        });
//    }
//
//    private void add_audio(final String url,final String title, final String user_id) {
//
//        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().addPortFolioAudioUrl("app-client",
//                "123321", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(), ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
//                user_id, url,title,"url","portfolio_aud");
//
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                if (response.body() != null) {
//                    try {
//                        String responseString = new String(response.body().bytes());
//                        JSONObject jObj = new JSONObject(responseString);
//                        boolean error = jObj.getBoolean("error");
//                        if (!error) {
////                            Log.e("audiourl: ",response);
//                            Toast.makeText(getApplicationContext(),
//                                    "Your are adding Audio Successfully!!", Toast.LENGTH_LONG).show();
//                        }else{
//                            String errorMsg = jObj.getString("error_msg");
//                            Toast.makeText(getApplicationContext(),
//                                   "not sucess full...>>>", Toast.LENGTH_LONG).show();
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
////    private void addnn_audio(final String title, final String url, final String user_id, final String token) {
////
////        String tag_string_req = "req_account";
////
////        StringRequest strReq = new StringRequest(Request.Method.POST,
////                URL_ADD_PORTFOLIO, new Response.Listener<String>(){
////
////            @Override
////            public void onResponse(String response) {
////
////                try {
////
////                    JSONObject jObj = new JSONObject(response);
////                    boolean error = jObj.getBoolean("error");
////                    if (!error) {
////                        Log.e("audiourl: ",response);
////                        Toast.makeText(getApplicationContext(),
////                                "Your are adding Audio Successfully!!", Toast.LENGTH_LONG).show();
////                    }else{
////                        String errorMsg = jObj.getString("error_msg");
////                        Toast.makeText(getApplicationContext(),
////                                errorMsg, Toast.LENGTH_LONG).show();
////                    }
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Log.e(TAG, "Registration Error: " + error.getMessage());
////                Toast.makeText(getApplicationContext(),
////                        error.getMessage(), Toast.LENGTH_LONG).show();
////
////            }
////        }) {
////
////            @Override
////            protected Map<String, String> getParams() {
////                Map<String, String> params = new HashMap<String, String>();
////
////                params.put("user_id", user_id);
////                params.put("url[]",url);
////                params.put("title[]",title);
////                params.put("upload_type","url");
////                params.put("table_name","portfolio_aud");
////
////                return params;
////            }
////
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                Map headers = new HashMap();
////                headers.put("Client-Service", "app-client");
////                headers.put("Auth-Key", "123321");
////                headers.put("Token", token);
////                headers.put("User-Id", user_id);
////                return headers;
////            }
////        };
////        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
////    }
//}
