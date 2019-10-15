package com.example.waytowork;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Itemdel extends AppCompatActivity implements OnMapReadyCallback {
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    GoogleMap map;
    Intent intent;
    Button tos,reset,regist;
    EditText item_detail;
    TextView start_po,end_po;
    View.OnClickListener cl;
    String kat = null;
    String getTos = null;

    String Id ;
    String Item_Kat ;
    String  Start_po ;
    String  End_po ;
    String content;
    String Tos ;
    String Regdt ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdel);

        tos = (Button) findViewById(R.id.tos);
        reset = (Button) findViewById(R.id.reset);
        regist = (Button) findViewById(R.id.regist);

        start_po = (TextView) findViewById(R.id.start_po);
        end_po = (TextView) findViewById(R.id.end_po);
        item_detail = (EditText) findViewById(R.id.item_detail);


        /*맵 설정.*/
        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.main_map)).getMapAsync(this);

        arrayList = new ArrayList<>();
        arrayList.add("분류");
        arrayList.add("문자");
        arrayList.add("음식");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        /*등록화면에서 넣어준 값들을 들고오는데 스피너는 데이터로 존재치 않아서... 이렇게 만듬*/
        String spin;
        spin = getIntent().getStringExtra("분류");
        int pin = 0;
        if(spin.equals("문자")){
            pin = 1;
        }else if(spin.equals("음식")){
            pin =2;
        }
        spinner.setSelection(pin); /*스피너 값 설정*/
        /*이 밑에꺼 지울예정*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(getApplicationContext(),"분류를 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
        });/*여까지*/

        /*등록하기에서 넘긴 데이터를 여기로 받아서 위젯에 옮겨줌*/
        start_po.setText((getIntent().getStringExtra("시작")));
        end_po.setText((getIntent().getStringExtra("도착")));
        item_detail.setText((getIntent().getStringExtra("내용")));




        cl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tos:
                        break;
                    case R.id.reset:
                        break;
                    case R.id.regist:

                        break;
                    case R.id.start_po:

                        break;
                    case R.id.end_po:
                        break;

                }
            }
        };
        start_po.setOnClickListener(cl);
        end_po.setOnClickListener(cl);
        tos.setOnClickListener(cl);
        reset.setOnClickListener(cl);
        regist.setOnClickListener(cl);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("Tos");
                getTos = result;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

            LatLng latLng = new LatLng(37.448864,127.167844);
            CameraPosition position=new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            /*지도를 위경도로 다시 바꿔서 마커를 옮겨 찍음*/
            MyReversThread reversThread = new MyReversThread(latLng);
            reversThread.run();
            /*지도를 위경도로 다시 바꿔서 마커를 옮겨 찍음2*/
            MyReversThread2 reversThread2 = new MyReversThread2(latLng);
            reversThread.run();

    }


    class MyReversThread extends Thread {
        LatLng latLng;
        public MyReversThread(LatLng latLng){
            this.latLng=latLng;
        }
        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                List<Address> results = geocoder.getFromLocationName(start_po.getText().toString(),1);
                Address resultAddress = results.get(0);
                LatLng latLng = new LatLng(resultAddress.getLatitude(),resultAddress.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start));
                markerOptions.position(latLng);
                map.addMarker(markerOptions);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyReversThread2 extends Thread {
        LatLng latLng;
        public MyReversThread2(LatLng latLng){
            this.latLng=latLng;
        }
        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try{
                List<Address> results = geocoder.getFromLocationName(end_po.getText().toString(),1);
                Address resultAddress = results.get(0);
                LatLng latLng = new LatLng(resultAddress.getLatitude(),resultAddress.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                markerOptions.position(latLng);
                map.addMarker(markerOptions);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}


