package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Setting_Select_Clause extends AppCompatActivity {

    Button b,b1,b2;
    ImageButton ib;
    View.OnClickListener cl;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_select_clause);

        ib = (ImageButton) findViewById(R.id.imageButton);
        b = (Button) findViewById(R.id.button);


        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imageButton:
                        onBackPressed();
                        break;
                    case R.id.button:
                        i = new Intent(getApplicationContext(),Clause_Show.class);
                        startActivityForResult(i,5);   // 첫번째만 전체보기 눌렀을때 이용약관 페이지 넘어가게끔만 해놓음
                        break;
                }
            }
        };
        ib.setOnClickListener(cl);
         b.setOnClickListener(cl);
//        ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

}
