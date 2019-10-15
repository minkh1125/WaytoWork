package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Clause_Account1 extends AppCompatActivity {
    CheckBox option1, option2;
    Button check;
    View.OnClickListener cl;

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clause_add);
        option1 = (CheckBox) findViewById(R.id.checkBox1);
        option2 = (CheckBox) findViewById(R.id.checkBox2);
        check = (Button) findViewById(R.id.check);

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option1.isChecked()){
                    //데이터 전달
                    Intent intent = new Intent(getApplicationContext(),Itemadd.class);
                    intent.putExtra("result","1");
                    setResult(RESULT_OK,intent);
                    //엑티비티 닫기
                    finish();


                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"동의가 필요합니다",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };
        check.setOnClickListener(cl);



    }
}
