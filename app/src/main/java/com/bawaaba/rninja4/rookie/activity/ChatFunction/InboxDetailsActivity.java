package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bawaaba.rninja4.rookie.R;

public class InboxDetailsActivity extends AppCompatActivity {

    private AppCompatTextView tvFrom;
    private AppCompatTextView tvContact;
    private AppCompatTextView tvMessage;
    private AppCompatTextView tvDate;
    private ImageButton Reply_button;
    private String from = "";
    private String contact = "";
    private String message = "";
    private String date = "";
    private AppCompatTextView tvName;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);

        getSupportActionBar().hide();
        tvName = (AppCompatTextView) findViewById(R.id.tvName);
        Reply_button=(ImageButton)findViewById(R.id.reply_but);
        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("FROM");
        contact = bundle.getString("CONTACT");
        message = bundle.getString("MESSAGE");
        date = bundle.getString("DATE");
        name = bundle.getString("NAME");
        tvName.setText(name);
        initViews();

        Log.e("inboxmail",from);

        Reply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, from);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {from});
                startActivity(emailIntent);
                finish();
            }
        });
    }

    private void initViews() {
        tvFrom = (AppCompatTextView) findViewById(R.id.tvFrom);
        tvContact = (AppCompatTextView) findViewById(R.id.tvContact);
        tvMessage = (AppCompatTextView) findViewById(R.id.tvMessage);
        tvDate = (AppCompatTextView) findViewById(R.id.tvDate);

        tvMessage.setText(message);
        tvFrom.setText("From  :   " + from);
        tvContact.setText("Contact  :   " + contact);
        tvDate.setText(date);
    }
}
