package com.example.waytowork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    Application app;
    EditText id,pw;
    TextView sign_up, pwchange;
    Button login;
    View.OnClickListener cl;
    ProgressDialog dialog = null;
    CheckBox checkBox;

    // 이것들 사용하기 위해 모듈에 compileOnly 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2' 적용
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    SharedPreferences appData;//자동로그인 하려고 ,이걸 사용하면 어플을 종료해도 값을 가지고있음
    boolean saveLoginData;
    String id1, pwd;//EditTextd에 ID랑 겹쳐서 id1이라고 해놓음



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 개발자 문제점 발견에 도움을 주는 친구
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        login = (Button) findViewById(R.id.login);
        sign_up = (TextView) findViewById(R.id.sign_up);
        pwchange = (TextView) findViewById(R.id.pwchange);  // 비밀번호 찾기(휴대폰번호로 아이디를 바꿨으므로 아이디찾기는 지웠음)

        //설정값 불러오기
        app = (Application) getApplication(); // 애플리케이션 클래스에서 가져오는거
        appData = getSharedPreferences("appData", MODE_PRIVATE); // 로그인클래스에서
        load();

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            id.setText(id1);
            pw.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }

        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.login:
                        if( id.getText().toString().equals("")) {
                            Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                        } else{
                            new Thread(new Runnable(){
                                public void run(){
                                    Looper.prepare();
                                    login();
                                    save();// 로그인 성공시 저장 처리, 예제는 무조건 저장
                                    Looper.loop();
                                }
                            }).start();
                        }
                        break;
                    case R.id.sign_up:
                        Intent intent = new Intent(Login.this, JoinUser.class);
                        startActivity(intent);
                        break;
                    case R.id.pwchange:
                        Intent intent1 = new Intent(Login.this, PwPop.class);
                        startActivity(intent1);
                        // 비밀번호 찾기 팝업창 인텐트

                }
            }
        };
        login.setOnClickListener(cl);
        sign_up.setOnClickListener(cl);
        pwchange.setOnClickListener(cl);
    }


    class Dialog extends Thread {
        public void run() {
            try {
                Thread.sleep(500);
                dialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    void login(){
        try{
            httpclient = new DefaultHttpClient();  //안드로이드에서 접속할때 클라이언트
            httppost = new HttpPost("http://shingu.freehost.kr/3_project/02_login_check.php");  //접속할주소
            nameValuePairs = new ArrayList<NameValuePair>(2);  //배열에 ip,비번 배열에 추가하기
            nameValuePairs.add(new BasicNameValuePair("member_id",id.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("member_pw",pw.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);//실행된값
            ResponseHandler<String> responseHandler = new BasicResponseHandler();  //받아온 값들을 string으로 반환해준다,핸들러 역할도 한다
            final String response = httpclient.execute(httppost,responseHandler);


            if(response.contains(id.getText().toString())){
                dialog = ProgressDialog.show(Login.this, "", // 여기 에러존재
                        "로그인 중...", true);
                Dialog thread = new Dialog();
                thread.start();
                Intent in = new Intent(Login.this, MainActivity.class);
                startActivity(in);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){

        }
    }
    private void save() {

        app.preference.put("SAVE_LOGIN_DATA", checkBox.isChecked());  //isChecked() 체크여부 확인
        app.preference.put("ID", id.getText().toString().trim());
        app.preference.put("PWD", pw.getText().toString().trim());
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
//        SharedPreferences.Editor editor = appData.edit();
//
//        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
//        // 저장시킬 이름이 이미 존재하면 덮어씌움
//        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
//        editor.putString("ID", id.getText().toString().trim());
//        editor.putString("PWD", pw.getText().toString().trim());
//
//        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
//        editor.apply();
    }

    // 설정값을 불러오는 함수
    public void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
//        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
//        id1 = appData.getString("ID", "");
//        pwd = appData.getString("PWD", "");

        saveLoginData = app.preference.getValue("SAVE_LOGIN_DATA", false); // 아이디 비밀번호를 세이브 로그인 데이터에 저장
        id1 = app.preference.getValue("ID", "");
        pwd = app.preference.getValue("PWD", "");

        //Log.e("ss", "login id : " + app.preference.getValue("ID", "") + " pwd : " + app.preference.getValue("PWD", ""));

    }
}