//package com.example.rninja4.rookie.activity.portfolioTab;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.rninja4.rookie.App.AppController;
//import com.example.rninja4.rookie.R;
//import com.example.rninja4.rookie.activity.ProfileTab.GaleryAdapter2;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class PortfolioVideo2 extends AppCompatActivity {
//
//    private String TAG = PortfolioVideo2.class.getSimpleName();
//    private ProgressDialog pDialog;
//    private ArrayList<Image> images;
//    private GaleryAdapter2 mAdapter;
//    private RecyclerView recyclerView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_portfolio_video2);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//
//        pDialog = new ProgressDialog(this);
//        images = new ArrayList<>();
//        mAdapter = new GaleryAdapter2(getApplicationContext(), images);
//
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//        recyclerView.addOnItemTouchListener(new GaleryAdapter2.RecyclerTouchListener(getApplicationContext(), recyclerView, new GaleryAdapter2.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("images", images);
//                bundle.putInt("position", position);
//
////                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
////                newFragment.setArguments(bundle);
////                newFragment.setCancelable(true);
////                newFragment.show(ft, "slideshow");
//
//                final WebView myWebView = (WebView) findViewById(R.id.webView1);
//
//
//                               // For playing youtube videos
//                               WebSettings webSettings = myWebView.getSettings();webSettings.setJavaScriptEnabled(true);
//                               String frameVideo = "<iframe  width=\'180px\' height=\'180px\' src='" +" https:www.youtube.com/results?search_query=android+development+tutorial "+ "?rel=0' type=\'text/html\' frameborder=\'0\' allowfullscreen=\'allowfullscreen\'></iframe>";
//                                myWebView.loadData(frameVideo, "text/html", "utf-8");
//                                myWebView.setWebViewClient(new WebViewClient());
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
//        fetchImages();
//
//    }
//
//    private void fetchImages() {
//
//        Intent from_profile = getIntent();
//        String user_id = from_profile.getStringExtra("user_id");
//
//        final String portfolio_video = "http://demo.rookieninja.com/api/user/port_video?user_id=" + user_id;
//
//        StringRequest req = new StringRequest(portfolio_video,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e(TAG, response.toString());
//                        // pDialog.hide();
//                        images.clear();
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            JSONArray portfolio_videos = jObj.getJSONArray("pvideo");
////                            JSONArray user = jObj.getJSONArray("user");
//                            for (int i = 0; i < portfolio_videos.length(); i++) {
//
//
//                              JSONObject object = portfolio_videos.getJSONObject(i);
//                              Image image = new Image();
//                                Log.e(TAG,"thumb: "+object.getString("thumbnail"));
//                               // image.setName(object.getString("title"));
//                                image.setMedium(object.getString("thumbnail"));
//                                images.add(image);
//
//                            }
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                // pDialog.hide();
//
//
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Client-Service", "app-client");
//                headers.put("Auth-Key","123321");
//                return headers;
//            }
//        };
//
//
//
//        AppController.getInstance().addToRequestQueue(req);
//    }
//
//
//
//}
//
//
//
//
