package com.example.PeanArt;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button, select;
    private EditText editText;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        editText = (EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        select=(Button)findViewById(R.id.select);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(getApplicationContext());

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude;// 위도
                Double longitude = point.longitude;
                latitude = Math.round(latitude*100)/100.0;
                longitude = Math.round(longitude*100)/100.0;// 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);

                LatLng newPoint = new LatLng(latitude, longitude);
                List<Address> newaddrList = null;
                try {
                    newaddrList = geocoder.getFromLocation(latitude, longitude, 10);
                    System.out.println(newaddrList.get(0).toString());
                    // 콤마를 기준으로 split
                    String []splitStr = newaddrList.get(0).toString().split(",");
                    address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    System.out.println(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint,15));
                Toast.makeText(getApplicationContext(), "현재 주소 : " + address, Toast.LENGTH_LONG).show();
            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                    System.out.println(addressList.get(0).toString());
                    // 콤마를 기준으로 split
                    String []splitStr = addressList.get(0).toString().split(",");
                    address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    System.out.println(address);

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Math.round(Double.parseDouble(latitude)*100)/100.0, Math.round(Double.parseDouble(longitude)*100)/100.0);

                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(address);
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                    Toast.makeText(getApplicationContext(), "현재 주소 : " + address, Toast.LENGTH_LONG).show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addrIntent = new Intent();
                addrIntent.putExtra("addr", address);
                setResult(RESULT_OK, addrIntent);
                finish();
            }
        });
        ////////////////////

        // Add a marker in Sydney and move the camera
        LatLng korea = new LatLng(35, 128);
        mMap.addMarker(new MarkerOptions().position(korea).title("Marker in korea"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(korea));
    }
}

