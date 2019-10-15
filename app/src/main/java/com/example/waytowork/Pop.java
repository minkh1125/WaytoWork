package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Pop extends AppCompatActivity {
    TextView text;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);

        text = (TextView) findViewById(R.id.text);
        b1 = (Button) findViewById(R.id.button);


        Intent i = getIntent();
        String data = i.getStringExtra("data");
        if (data != null) {
            text.setText(data);
        }
//        String content = i.getStringExtra("content");  이 기능이 사라짐
//        if (content != null) {
//            text.setText(content);
//        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
