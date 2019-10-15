package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemPop extends AppCompatActivity {
    TextView text;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itempop);

        text = (TextView)findViewById(R.id.text);
        b1 = (Button)findViewById(R.id.button);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        text.setText(data);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
