package com.example.waytowork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
/*Map 만드는 실습 해봄 이거 보면서 이해하세용*/
public class Map extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    /*지도 객체를 얻기위해 OnMapReadyCallback 인터페이스를 구현한 getMapAsync함수를 이용해 등록*/
    GoogleApiClient googleApiClient;
    FusedLocationProviderApi fusedLocationProviderApi;
    GoogleMap map;
    Location location;

    Marker marker;
    TextView addressView;

    String resultAddress;
    double resultLat;
    double resutlLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_activity);
        addressView = (TextView) findViewById(R.id.pin);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview)).getMapAsync(this); // OnMapReadCallback 인터페이스를 선언

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = fusedLocationProviderApi.getLastLocation(googleApiClient);
            moveMap();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @Nullable String[] permissions, @Nullable int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                location = fusedLocationProviderApi.getLastLocation(googleApiClient);
                moveMap();
            }else{
                showToast("no permission...");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast("access location fail...");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveListener(this);
        moveMap();
    }
    private void moveMap(){
        if(location!= null && map != null){
            LatLng gpsLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            CameraPosition position = new CameraPosition.Builder().target(gpsLatLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            if(marker != null) marker.remove();
            marker = map.addMarker(new MarkerOptions().position(gpsLatLng).title("Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));

            MyGeocodingThread thread = new MyGeocodingThread(gpsLatLng);
            thread.start();
        }
    }

    @Override
    public void onCameraMove() {
        marker.setPosition(map.getCameraPosition().target);
    }

    @Override
    public void onCameraIdle() {
        MyGeocodingThread thread = new MyGeocodingThread(map.getCameraPosition().target);
        thread.start();
        resultLat = map.getCameraPosition().target.latitude;
        resutlLng = map.getCameraPosition().target.longitude;

    }

    class MyReverseGercodingThread extends Thread{
        String address;

        public MyReverseGercodingThread(String address){
            this.address = address;

        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(Map.this);
            try{
                List<Address> results=geocoder.getFromLocationName(address,1); // getFromLocationName 함수를 ㅣ호출하면서 매개변수로 위경도 값을 전달하면 구글 서버의 주소 문자열이 반환됨.
                Address resultAddress = results.get(0); // Address 타입으로 전달된 것을 getter 함수로 주소 값을 얻음.
                LatLng latLng = new LatLng(resultAddress.getLatitude(),resultAddress.getLongitude());

                Message msg = new Message();
                msg.what=200;
                msg.obj = latLng;
                handler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyGeocodingThread extends Thread{
        LatLng latLng;
        public MyGeocodingThread(LatLng latLng){
            this.latLng=latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(Map.this);
            List<Address> addresses = null;
            String addressText="";

            try{
                addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                Thread.sleep(500);
                if(addresses != null && addresses.size()>0){ // 어드레스 객체가 널이 아니고 사이즈 값이 0보다 크면
                    Address address = addresses.get(0); // 어드레스 객체의 get 함수를 이용해 받음.
                    addressText = address.getAdminArea(/*서울특별시*/)+ " "+(address.getMaxAddressLineIndex(/*0*/)>0 ?
                            address.getAddressLine(0/*대한민국 서울특별시 중구 명동 35*/) : address.getLocality(/*중구*/))+" ";
                    String txt = address.getSubLocality(/*null*/);
                    if(txt != null){
                        addressText += txt +" ";
                        addressText += address.getThoroughfare(/*명동*/)+ " "+address.getSubThoroughfare(/*35*/);


                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 100;
            msg.obj = addressText;
            handler.sendMessage(msg);
            resultAddress=addressText;

        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 100:
                    addressView.setText((String)msg.obj);
                    Intent intent = new Intent(Map.this,Itemadd.class);
                    intent.putExtra("start_po",addressView.getText());
                    break;
                case 200:

            }
        }
    };
}