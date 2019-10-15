package com.example.waytowork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Tos extends AppCompatActivity{
    ImageButton ib_Back;
    CheckBox ch_All,ch_First,ch_Second,ch_Third;
    EditText ed_First,ed_Second,ed_Third;
    Button bt_Ok,bt_Cancel;
    View.OnClickListener cl;
    Intent intent;
    int REQUEST_TEST =1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos);
        ib_Back = (ImageButton) findViewById(R.id.ib_Back);
        ch_All = (CheckBox) findViewById(R.id.ch_All);
        ch_First = (CheckBox) findViewById(R.id.ch_First);
        ch_Second = (CheckBox) findViewById(R.id.ch_Second);
        ch_Third = (CheckBox) findViewById(R.id.ch_Third);
        ed_First = (EditText) findViewById(R.id.ed_First);
        ed_Second = (EditText) findViewById(R.id.ed_Second);
        ed_Third = (EditText) findViewById(R.id.ed_Third);
        bt_Ok = (Button) findViewById(R.id.bt_Ok);
        bt_Cancel = (Button) findViewById(R.id.bt_Cancel);
        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ib_Back:
                        break;
                    case R.id.ch_All:
                        ch_First.setChecked(true);
                        ch_Second.setChecked(true);
                        ch_Third.setChecked(true);
                        break;
                    case R.id.ch_First:
                        break;
                    case R.id.ch_Second:
                        break;
                    case R.id.ch_Third:
                        break;
                    case R.id.ed_First:
                        intent = new Intent(Tos.this,Clause_Account1.class);
                        startActivityForResult(intent, REQUEST_TEST);
                        break;
                    case R.id.ed_Second:
                        intent = new Intent(Tos.this,Clause_Account2.class);
                        startActivityForResult(intent, REQUEST_TEST);
                        break;
                    case R.id.ed_Third:
                        intent = new Intent(Tos.this,Clause_Account3.class);
                        startActivityForResult(intent, REQUEST_TEST);
                        break;
                    case R.id.bt_Ok:
                        intent = new Intent(Tos.this,JoinUser.class);
                        intent.putExtra("result","1");
                        setResult(RESULT_OK,intent);
                        //엑티비티 닫기
                        finish();
                        break;
                    case R.id.bt_Cancel:
                        break;
                }
            }
        };
        ib_Back.setOnClickListener(cl);
        ch_First.setOnClickListener(cl);
        ch_Second.setOnClickListener(cl);
        ch_Third.setOnClickListener(cl);
        ed_First.setOnClickListener(cl);
        ed_Second.setOnClickListener(cl);
        ed_Third.setOnClickListener(cl);
        bt_Ok.setOnClickListener(cl);
        bt_Cancel.setOnClickListener(cl);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(Tos.this, "Result: " + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                    if(data.getStringExtra("result").equals("1")){
                        ch_First.setChecked(true);
                    }else if(data.getStringExtra("result").equals("2")){
                        ch_Second.setChecked(true);
                    }else if(data.getStringExtra("result").equals("3")){
                        ch_Third.setChecked(true);
                }

                // 값 제대로 왔을떄
            } else {   // RESULT_CANCEL
               //값 제데로 안올때
                // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
