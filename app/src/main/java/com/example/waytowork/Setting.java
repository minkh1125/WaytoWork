package com.example.waytowork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    ImageButton ib;
    Button logout, version, push, clause, out;
    View.OnClickListener cl;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ib = (ImageButton) findViewById(R.id.imageButton);
        logout = (Button) findViewById(R.id.logout);
        version = (Button) findViewById(R.id.version);
        push = (Button) findViewById(R.id.push);
        clause = (Button) findViewById(R.id.clause);
        out = (Button) findViewById(R.id.out);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imageButton:
                            onBackPressed();
                            break;
                        case R.id.logout:
                            i = new Intent( Setting.this, Login.class );
                            i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP ); //로 LogIn Activity 를 호출하면 콘덴츠 activity와 Logout Activity는 자동으로 종료되면서 LogIn Activity가 최상단에 위치하게 됩니다.
                            i.putExtra( "KILL", true );//..?
                            startActivity(i);
                            SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = appData.edit();
                            editor.clear(); // auto에 들어있는 모든 정보를 기기에서 지웁니다.
                            editor.commit();
                            Toast.makeText(Setting.this, "로그아웃", Toast.LENGTH_SHORT).show();
                        break;
                        case R.id.version:
                            i = new Intent(getApplicationContext(),Version.class);
                            startActivity(i);
                            break;
                        case R.id.push:
                            i = new Intent(getApplicationContext(),Push.class);
                            startActivity(i);
                            break;
                        case R.id.clause:
                            i = new Intent(getApplicationContext(), Setting_Select_Clause.class);
                            startActivity(i);
                            break;
                        case R.id.out:
                            i = new Intent(getApplicationContext(),Out.class);
                            startActivity(i);
                            break;

                    }

            }
        };
        ib.setOnClickListener(cl);
        logout.setOnClickListener(cl);
        version.setOnClickListener(cl);
        push.setOnClickListener(cl);
        clause.setOnClickListener(cl);
        out.setOnClickListener(cl);
    }
}