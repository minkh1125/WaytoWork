package com.example.waytowork;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class JoinUser extends AppCompatActivity {
    private EditText editTextId,editTextPw,checkPw,editTextName,editTextEmail;
    Button join,authentication,tos;
    View.OnClickListener cl;
    String getTos;
    int REQUEST_TEST =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinuser);
        editTextId = (EditText) findViewById(R.id.input_id);
        editTextPw = (EditText) findViewById(R.id.input_pw);
        checkPw = (EditText) findViewById(R.id.input_pw2);
        editTextName = (EditText) findViewById(R.id.input_name);
        editTextEmail = (EditText) findViewById(R.id.input_email);


        join = (Button) findViewById(R.id.join);
        authentication = (Button) findViewById(R.id.authentication);
        tos = (Button) findViewById(R.id.tos);
        cl = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.join:
                        // 넣어야 할 값들을 스트링 변수에 할당
                        String Id = editTextId.getText().toString();
                        String Pw = editTextPw.getText().toString();
                        String Chpw = checkPw.getText().toString();
                        String Name = editTextName.getText().toString();
                        String Email = editTextEmail.getText().toString();
                        String Tos = getTos;
                        String Regdt="";  /*날짜는 알아서 디비에서 넣어주므로 공백처리해줘서 보내면 됨.*/
                        String Mod = "1";
                        if(Pw.equalsIgnoreCase(Chpw)) {

                            insertoToDatabase(Id, Pw, Name, Email,Tos, Regdt, Mod); /*회원가입 인서트*/

                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else checkPw.setText("");
                        break;
                    case R.id.authentication: // 휴대폰번호로 본인인증으로 변경

                        break;
                    case R.id.tos:
                        Intent intent = new Intent(JoinUser.this, Tos.class);
                        startActivityForResult(intent,REQUEST_TEST); /*Tos 클래스에서 값을 받아오려고 선언.*/
                        break;
                }

            }
        };
        join.setOnClickListener(cl);
        authentication.setOnClickListener(cl);
        tos.setOnClickListener(cl);
    }
   /* // 핸드폰번호 usim에서 불러오기. 출처 : http://i5on9i.blogspot.com/2015/10/blog-post_70.html
    public String getLine1NumberForSubscriber(int subId) {
        String number = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null)
                number = telephony.getLine1NumberForDisplay(subId, mContext.getOpPackageName());
        } catch (RemoteException ex) {
        } catch (NullPointerException ex) {
        }
        if (number != null) {
            return number;
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null)
                return null;
            return info.getLine1NumberForSubscriber(subId, mContext.getOpPackageName());
        } catch (RemoteException ex) {
            return null;
        } catch (NullPointerException ex) {
            // This could happen before phone restarts due to crashing
            return null;
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                getTos=data.getStringExtra("result");/*Tos 클래스에 갔다가 와서 얻은 약관 동의값을 getTos 에 저장*/
            } else {
                Toast.makeText(JoinUser.this, "약관을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void selectId(String getId ) {
        class select_Id extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();/*xml의 결과값이 여기에 s 를 통해 출력*/
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String get_Id = (String) params[0];
                    String link = "http://shingu.freehost.kr/3_project/select.php";
                    String data = URLEncoder.encode("my_id", "UTF-8") + "=" + URLEncoder.encode(get_Id, "UTF-8");/*아이디 중복확인을 위함*/

                    URL url = new URL(link);

                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        select_Id task = new select_Id();
        task.execute(getId);
    }

    private void insertoToDatabase(String Id, String Pw, String Name, String Email, String Tos, String Regdt, String Mod) {
        class InsertData extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Name = (String) params[2];
                    String Email = (String) params[3];
                    String Tos = (String) params[4];
                    String Regdt = (String) params[5];
                    String Mod = (String) params[6];

                    String link = "http://shingu.freehost.kr/3_project/03_member_insert.php";
                    String data = URLEncoder.encode("member_id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("member_pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("member_name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");
                    data += "&" + URLEncoder.encode("member_email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");
                    data += "&" + URLEncoder.encode("tos", "UTF-8") + "=" + URLEncoder.encode(Tos, "UTF-8");
                    data += "&" + URLEncoder.encode("regdt","UTF-8")+ "=" + URLEncoder.encode(Regdt,"UTF-8");
                    data += "&" + URLEncoder.encode("mod","UTF-8")+ "=" + URLEncoder.encode(Mod,"UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id,Pw,Name,Email,Tos, Regdt, Mod);
    }
}


