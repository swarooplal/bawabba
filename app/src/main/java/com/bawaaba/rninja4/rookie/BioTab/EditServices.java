package com.bawaaba.rninja4.rookie.BioTab;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;

public class EditServices extends AppCompatActivity {


    private EditText Title;
    private EditText Prize;
    private EditText Description;
    private Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_services);

        Title = (EditText) findViewById(R.id.title);
        Prize = (EditText) findViewById(R.id.prize);
        Description = (EditText) findViewById(R.id.descrption);
        Save = (Button) findViewById(R.id.save);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actiontitle_layout12);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = Title.getText().toString().trim();
                String prize = Prize.getText().toString().trim();
                String description = Description.getText().toString().trim();

                if (!title.isEmpty()&&!prize.isEmpty()&&!description.isEmpty()) {

                    editd_services(title, prize,description);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();

                }
            }

            private void editd_services(String title, String prize, String description) {


            }

        });
    }
}
