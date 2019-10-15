package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ItemaddPop extends AppCompatActivity {
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemaddpop);

        b1 = (Button)findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemaddPop.this, Itemadd.class);
                startActivity(intent);
            }
        });


    }

}
