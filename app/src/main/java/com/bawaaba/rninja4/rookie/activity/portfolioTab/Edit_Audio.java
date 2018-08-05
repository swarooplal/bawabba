//package com.example.rninja4.rookie.activity.portfolioTab;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.example.rninja4.rookie.R;
//import com.example.rninja4.rookie.activity.AdaptereditAudio;
//import com.example.rninja4.rookie.activity.Edit_Audio_Data;
//import com.example.rninja4.rookie.activity.SimpleDividerItemDecoration;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Edit_Audio extends AppCompatActivity {
//
//    private String TAG = Edit_Audio.class.getSimpleName();
//
//    private RecyclerView mProfiledetails;
//
//    private ImageButton play_audio;
//    private ImageButton delete_audio;
//    private ImageButton edit_audio;
//    private AdaptereditAudio mAdapter;
//    private LinearLayoutManager mLayoutManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit__audio);
//
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout16);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
//        actionBar.setHomeButtonEnabled(true);
//        List<Edit_Audio_Data> data=new ArrayList<>();
//
//        Intent from_PortfolioAudio = getIntent();
//        final String portfolio_audio = from_PortfolioAudio.getStringExtra("portfolio_audio");
//        Log.e(TAG, "editAudio5: " + portfolio_audio);
//
//         Edit_Audio.this.runOnUiThread(new Runnable() {
//             @Override
//             public void run() {
//                 Intent to_Adapteredit = new Intent(Edit_Audio.this,AdaptereditAudio.class);
//                 to_Adapteredit.putExtra("portfolio_audio",portfolio_audio);
//                 //startActivity(to_Adapteredit);
//             }
//         });
//
//        try {
//            JSONArray audio_data = new JSONArray(portfolio_audio);
//            for (int i = 0; i < audio_data.length(); i++) {
//
//                JSONObject object = audio_data.getJSONObject(i);
//
//                Edit_Audio_Data audioData = new Edit_Audio_Data();
//
//                audioData.Audio_Title = object.getString("title");
//                audioData.Audio_Link = object.getString("audio_link");
//                data.add(audioData);
//            }
//            Log.e(TAG, "editAudio6:"+data);
//            mProfiledetails=(RecyclerView)findViewById(R.id.edit_audios);
//            mProfiledetails.setLayoutManager( mLayoutManager);
//            mProfiledetails.addItemDecoration(new SimpleDividerItemDecoration(this));
//            mAdapter = new AdaptereditAudio(Edit_Audio.this, data);
//            mProfiledetails.setLayoutManager( mLayoutManager);
//            mProfiledetails.setAdapter(mAdapter);
//            mProfiledetails.setLayoutManager(new LinearLayoutManager(Edit_Audio.this));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);
//        final View itemView = inflater.inflate(R.layout.dialogue_addaudio,null);
//
//        final TextView From_Gallery =(TextView) itemView.findViewById(R.id.from_gallery);
//        final TextView From_Url =(TextView) itemView.findViewById(R.id.from_url);
//        final TextView Cancel =(TextView) itemView.findViewById(R.id.cancel);
//
//
//        From_Gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent to_from_galery = new Intent(Edit_Audio.this,From_Gallery.class);
//                startActivity(to_from_galery);
//            }
//        });
//
//        From_Url.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent to_from_url = new Intent(Edit_Audio.this,From_Url.class);
//                startActivity(to_from_url);
//            }
//        });
//
//        int id = item.getItemId();
//
//        mBuilder.setView(itemView);
//        AlertDialog dialog =mBuilder.create();
//        dialog.show();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_add) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//}
//
//
//
