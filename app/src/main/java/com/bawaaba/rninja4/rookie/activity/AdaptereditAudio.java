//package com.example.rninja4.rookie.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.rninja4.rookie.App.AppController;
//import com.example.rninja4.rookie.R;
//import com.example.rninja4.rookie.activity.portfolioTab.ItemClickListener_edit_audio;
//import com.example.rninja4.rookie.activity.portfolioTab.Portfolio_Audio_Web;
//import com.example.rninja4.rookie.helper.SQLiteHandler;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.example.rninja4.rookie.App.AppConfig.URL_UPDATE_PORTFOLIO;
//import static com.facebook.FacebookSdk.getApplicationContext;
//
///**
// * Created by rninja4 on 9/27/17.
// */
//
//public class AdaptereditAudio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private static final String TAG = AdaptereditAudio.class.getSimpleName();
//    private Context context;
//    private SQLiteHandler db;
//
//    private LayoutInflater inflater;
//    List<Edit_Audio_Data> data = Collections.emptyList();
//
//    public AdaptereditAudio(Context context, List<Edit_Audio_Data> data) {
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//        this.data = data;
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//
//        View view = inflater.inflate(R.layout.audio_list_edit, parent, false);
//        AdaptereditAudio.MyHolder holder = new AdaptereditAudio.MyHolder(view);
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        AdaptereditAudio.MyHolder myHolder = (AdaptereditAudio.MyHolder) holder;
//        Edit_Audio_Data current = data.get(position);
//
//        myHolder.audio_title.setText(current.Audio_Title);
//        myHolder.play_audio.setImageURI(Uri.parse(current.Audio_Link));
//
//        Intent from_edit_audio = ((Activity) context).getIntent();
//        final String portfolio_audio = from_edit_audio.getStringExtra("portfolio_audio");
//
//        try {
//            final ArrayList<String> audio_edit_list_link = new ArrayList<String>();
//
//            JSONArray audio_edit_data = new JSONArray(portfolio_audio);
//            for (int i = 0; i < audio_edit_data.length(); i++) {
//                JSONObject object = audio_edit_data.getJSONObject(i);
//                final String audio_edit_link = object.getString("audio_link");
//                audio_edit_list_link.add(audio_edit_link.toString());
//
//            }
//
//            myHolder.setItemClickListener_edit_audio(new ItemClickListener_edit_audio() {
//                @Override
//                public void onItemClick(int pos) {
//                    Log.e("position_audio", String.valueOf(pos));
//                    Intent to_audio_web = new Intent(context, Portfolio_Audio_Web.class);
//                    to_audio_web.putExtra("audio_link", audio_edit_list_link.get(pos));
//                    context.startActivity(to_audio_web);
//
//                }
//            });
//
////            myHolder.setItemClickListener_edit_audio(new ItemClickListener_edit_audio() {
////                @Override
////                public void onItemClick(int pos) {
////
////
////
////
////
////                }
////            });
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private ImageButton play_audio;
//        private TextView audio_title;
//        private ImageButton delete_audio;
//        private ImageButton edit_audio;
//        private Button Audio_save;
//        private RelativeLayout layout_delete;
//        ItemClickListener_edit_audio itemClickListener;
//        //   private EditText Audio_title;
//
//
//        public MyHolder(final View itemView) {
//            super(itemView);
//
//            play_audio = (ImageButton) itemView.findViewById(R.id.audio_button);
//            audio_title = (TextView) itemView.findViewById(R.id.text_audio);
//            edit_audio = (ImageButton) itemView.findViewById(R.id.edit_button);
//            layout_delete = (RelativeLayout) itemView.findViewById(R.id.layout_delete);
//            itemView.setOnClickListener(this);
//        }
//
//        public void setItemClickListener_edit_audio(ItemClickListener_edit_audio itemClickListener) {
//
//            this.itemClickListener = itemClickListener;
//
//
////        @Override
////        public void onClick(View v) {
////          this.itemClickListener.onItemClick(this.getLayoutPosition());
//
//
//            edit_audio.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent from_edit_audio = ((Activity) context).getIntent();
//                    final String portfolio_audio = from_edit_audio.getStringExtra("portfolio_audio");
//                    Log.e("audio", portfolio_audio);
//                    Context context = v.getContext();
//                    LayoutInflater inflater = LayoutInflater.from(v.getContext().getApplicationContext());
//
//                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
//                    final View itemView = inflater.inflate(R.layout.dialogue_editaudio, null);
//
//                    try {
//
//                          final ArrayList<String> audio_edit_list_title = new ArrayList<String>();
//
//                        JSONArray audio_data = new JSONArray(portfolio_audio);
//                        for (int i = 0; i < audio_data.length(); i++) {
//                            final EditText Audio_title = (EditText) itemView.findViewById(R.id.audio_titlename);
//                            final TextView hidden_id = (TextView) itemView.findViewById(R.id.hidden_id);
//                            JSONObject object = audio_data.getJSONObject(i);
//
//                            final String id = object.getString("id");
//                            final String audio_edit_title = object.getString("title");
//                           audio_edit_list_title.add(audio_edit_title.toString());
//                            hidden_id.setText(id);
//                            hidden_id.setVisibility(View.GONE);
//                            Audio_title.setText(audio_edit_title);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    Audio_save = (Button) itemView.findViewById(R.id.Audio_save);
//                    Audio_save.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            final EditText Audio_title = (EditText) itemView.findViewById(R.id.audio_titlename);
//                            final TextView hidden_id = (TextView) itemView.findViewById(R.id.hidden_id);
//                            String title = Audio_title.getText().toString().trim();
//                            String row_id = hidden_id.getText().toString().trim();
//                            Log.e("id_check", row_id);
//                            if (!title.isEmpty()) {
//                                Edit_Audio(title, row_id);
//                            } else {
//                                Toast.makeText(getApplicationContext(),
//                                        "Please enter the credentials!", Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        }
//
//                        private void Edit_Audio(final String title, final String row_id) {
//
//                            db = new SQLiteHandler(getApplicationContext());
//                            HashMap<String, String> user = db.getUserDetails();
//                            final String user_id = user.get("uid");
//                            final String token = user.get("token");
//
//                            String tag_string_req = "req_account";
////
//                            StringRequest strReq = new StringRequest(Request.Method.POST,
//                                    URL_UPDATE_PORTFOLIO, new Response.Listener<String>() {
//
//                                @Override
//                                public void onResponse(String response) {
//
//                                    try {
//
//                                        JSONObject jObj = new JSONObject(response);
//                                        boolean error = jObj.getBoolean("error");
//                                        if (!error) {
//                                            Log.e("change title: ", response);
//
//                                            Toast.makeText(getApplicationContext(),
//                                                    "Your audio title is changed", Toast.LENGTH_LONG).show();
//                                        } else {
//                                            String errorMsg = jObj.getString("error_msg");
//                                            Toast.makeText(getApplicationContext(),
//                                                    errorMsg, Toast.LENGTH_LONG).show();
//                                        }
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e(TAG, "Registration Error: " + error.getMessage());
//                                    Toast.makeText(getApplicationContext(),
//                                            error.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            }) {
//
//                                @Override
//                                protected Map<String, String> getParams() {
//                                    Map<String, String> params = new HashMap<String, String>();
//
//                                    params.put("user_id", user_id);
//                                    params.put("table_name", "portfolio_aud");
//                                    params.put("action", "update");
//                                    params.put("title", title);
//                                    params.put("row_id", row_id);
//
//
//                                    return params;
//                                }
//
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//                                    Map headers = new HashMap();
//                                    headers.put("Client-Service", "app-client");
//                                    headers.put("Auth-Key", "123321");
//                                    headers.put("Token", token);
//                                    headers.put("User-Id", user_id);
//                                    return headers;
//                                }
//                            };
//                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//                        }
//                    });
//
//                    mBuilder.setView(itemView);
//                    AlertDialog dialog = mBuilder.create();
//                    dialog.show();
//
//                }
//
//            });
//            delete_audio = (ImageButton) itemView.findViewById(R.id.delete_button);
//            delete_audio.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent from_edit_audio = ((Activity) context).getIntent();
//                    final String portfolio_audio = from_edit_audio.getStringExtra("portfolio_audio");
//                    Log.e("audio_delete", portfolio_audio);
//
//                    try {
//                        JSONArray audio_data = new JSONArray(portfolio_audio);
//                        //final TextView hidden_id =(TextView)itemView.findViewById(R.id.hidden_id);
//                        for (int i = 0; i < audio_data.length(); i++) {
//                            JSONObject object = audio_data.getJSONObject(i);
//                            final String row_id = object.getString("id");
//                            //  hidden_id.setText(id);
//                            // hidden_id.setVisibility(View.GONE);
//
//                            // Delete_audio(row_id);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    layout_delete.setVisibility(View.GONE);
//                }
//
//                private void Delete_audio(final String row_id) {
//
//                    db = new SQLiteHandler(getApplicationContext());
//                    HashMap<String, String> user = db.getUserDetails();
//                    final String user_id = user.get("uid");
//                    final String token = user.get("token");
//
//                    String tag_string_req = "req_account";
////
//                    StringRequest strReq = new StringRequest(Request.Method.POST,
//                            URL_UPDATE_PORTFOLIO, new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//
//                            try {
//
//                                JSONObject jObj = new JSONObject(response);
//                                boolean error = jObj.getBoolean("error");
//                                if (!error) {
//                                    Log.e("delete: ", response);
//
//                                    Toast.makeText(getApplicationContext(),
//                                            "Your audio is deleted!!!", Toast.LENGTH_LONG).show();
//                                } else {
//                                    String errorMsg = jObj.getString("error_msg");
//                                    Toast.makeText(getApplicationContext(),
//                                            errorMsg, Toast.LENGTH_LONG).show();
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, "Registration Error: " + error.getMessage());
//                            Toast.makeText(getApplicationContext(),
//                                    error.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<String, String>();
//
//                            params.put("user_id", user_id);
//                            params.put("table_name", "portfolio_aud");
//                            params.put("action", "delete");
//                            params.put("row_id", row_id);
//                            Log.e("row_id", row_id);
//
//
//                            return params;
//                        }
//
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map headers = new HashMap();
//                            headers.put("Client-Service", "app-client");
//                            headers.put("Auth-Key", "123321");
//                            headers.put("Token", token);
//                            headers.put("User-Id", user_id);
//                            return headers;
//                        }
//                    };
//                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//                }
//
//            });
//
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            this.itemClickListener.onItemClick(this.getLayoutPosition());
//        }
//    }
//}
//
//
//
//
