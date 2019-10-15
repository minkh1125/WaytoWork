package com.example.waytowork;

import android.content.Intent;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<MainData> arrayList = new ArrayList<>();
    private MainAdapter mainAdapter = null;
    MainData data = null;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager; //리사이클 뷰에서 사용하는거
    private String requestUrl;
    Intent i;

    Application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //팝업창 구현부
        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 담아서 팝업(액티비티) 호출
                Intent popIntent = new Intent(MainActivity.this, Pop.class);
                popIntent.putExtra("data", "Test Popup");
                startActivity(popIntent);
            }
        });


//네비게이션 드로얼,스와이프 구현부
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//여기있어야 가는김에라고 뜨는데 왜지..?
//        FloatingActionButton fab = findViewById(R.id.fab);  //fab는 오른쪽 아래에 떠있는 버튼 필요없는
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);  //액티비티 메인에 전체레이아웃을 drawerlayout으로 잡아놨음
        NavigationView navigationView = findViewById(R.id.nav_view);  //스와이프 화면 = nav_view

        //NavigationDrawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(   //스와이프되게 버튼이 생기면서 열었다가 닫았다가를 제어
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //values ->string에 저장 되어있음
        drawer.addDrawerListener(toggle); //버튼이 toogle , toogle을 눌렀을대 드로우 한다
        toggle.syncState();//

        //NavigationView
        navigationView.setNavigationItemSelectedListener(this); //스와이프 화면에 있는 아이템(메뉴버튼)들을 사용하기위해 선언

        //nav_header_main xml을 제어하고 싶으면 이렇게 선언후에 가능
        app = (Application)getApplication();
        View header = navigationView.getHeaderView(0);
        TextView tvName =  header.findViewById(R.id.tvName);
        tvName.setText(app.preference.getValue("ID", ""));

//새로고침
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        swipeRefreshLayout.setRefreshing(false);

//리사이클러뷰 구현부
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager); //위에만든 매니저를 리사이클 뷰에 해줘라

        //AsyncTask 실행하는곳
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        /*arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList); // MainAdapter에서 가져와서 arrylist값을 넣어준것
        recyclerView.setAdapter(mainAdapter);//mainAdapter에 담겨져있는걸 recyclerView에가  다시 담는다
        */

        //검색기능 구현부
        final EditText e1;
        e1 = (EditText) findViewById(R.id.editText);
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = e1.getText().toString().toLowerCase(Locale.getDefault());
                mainAdapter.filterList(text);
            }
        });

    }


    //메인엑티비티에 드로우 레이아웃을 펼치고 접고를 처리하는곳
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

// 이 부분은 기본으로 만들어 질때 오른쪽 구석에 있는 또다른 메뉴
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {          d
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    //여기부분은 뭐인지 잘몰라서 주석 처리했더니 오류사라짐 깡샘책 603p
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    //스와이프화면 메뉴들
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            i = new Intent(getApplicationContext(), Profile.class);
            startActivity(i);
        } else if (id == R.id.itemadd) {
            i = new Intent(getApplicationContext(), Itemadd.class);
            startActivity(i);
        } else if (id == R.id.pointshop) {
            i = new Intent(getApplicationContext(), PointShop.class);
            startActivity(i);
        } else if (id == R.id.setting) {
            i = new Intent(getApplicationContext(), Setting.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            requestUrl = "http://shingu.freehost.kr/3_project/07_main.php";
            try {
                Boolean b_item_kat = false;
                Boolean b_start_po = false;    //상태값(참,거짓)을 알기위해서
                Boolean b_end_po = false;
                Boolean b_content = false;

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) { //end 도큐먼트가 끝이 아닐때 까지 파씽
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT: //start도큐먼트는 문서의 시작
                            //arrayList = new ArrayList<MainData>();
                            break;
                        case XmlPullParser.START_TAG: //Item태그 시작일때
                            if (parser.getName().equals("item")) {
                                data = new MainData();
                            }
                            if (parser.getName().equals("item_kat")) b_item_kat = true;
                            if (parser.getName().equals("start_po")) b_start_po = true;
                            if (parser.getName().equals("end_po")) b_end_po = true;
                            if (parser.getName().equals("content")) b_content = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (b_item_kat) {
                                data.setIv_item_kat(parser.getText()); // 이게 투르인상태일때 data에다가 넣어주고  텍스트에보여준다 그다음 상태값은 다시 false로 바뀜
                                b_item_kat = false;
                            } else if (b_start_po) {
                                data.setTv_start_po(parser.getText());
                                b_start_po = false;
                            } else if (b_end_po) {
                                data.setTv_end_po(parser.getText());
                                b_end_po = false;
                            } else if (b_content) {
                                data.setTv_content(parser.getText());
                                b_content = false;
                            }
                            break;
                        case XmlPullParser.END_TAG: //item태그가 끝일때
                            if (parser.getName().equals("item") && data != null) { //반복태그 item
                                Log.i("ENT_TAG", "ENTER");
                                arrayList.add(data);
                            }
                            break;
                    }
                    eventType = parser.next();//?
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) { //백그라운드 작업이 완료된 후 결과값을 얻습니다
            super.onPostExecute(s);

            //어댑터 연결
            mainAdapter = new MainAdapter(getApplicationContext(), arrayList);
            recyclerView.setAdapter(mainAdapter);
        }
    }

}


