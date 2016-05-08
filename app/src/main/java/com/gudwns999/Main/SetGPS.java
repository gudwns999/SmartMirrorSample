package com.gudwns999.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HyungJun on 2015-08-10.
 */
public class SetGPS extends Activity{
    private static String TAG = "SetGPS";
    TextView addressText;
    Geocoder gc;
    Location location;
    LocationManager manager;
    String sLocationInfo = "";
    ProgressDialog dialog = null;


    //GPS 설정 체크
        private boolean chkGpsService() {
            String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            Log.d(gps, "gps found");
            if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
                // GPS OFF 일때 Dialog 표시
                AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
                gsDialog.setTitle("위치 서비스 설정");
                gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
                gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // GPS설정 화면으로 이동
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivity(intent);
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create().show();
                return false;

            } else {
                return true;
            }
        }
        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.setgps_layout);
            //GPS 설정 체크
            chkGpsService();
            Intent intent = getIntent();
            final String Id = intent.getExtras().getString("IDDATA");
            final String ps = intent.getExtras().getString("PASSWORDDATA");
            final String mi = intent.getExtras().getString("MIRRORDATA");

            gc = new Geocoder(this, Locale.KOREA);
            addressText = (TextView) findViewById(com.bulletnoid.android.gudwns999.Main.R.id.addressEdit);

            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //Criteria 클래스를 이용하여 요구조건을 명시하여, 가장 적합한 기술을 결정
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.NO_REQUIREMENT);
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);

            final String provider = manager.getBestProvider(criteria, true);
            location = manager.getLastKnownLocation(provider);
            manager.requestLocationUpdates(provider, 1000, 0, mLocationListener);
            //       ImageButton button01 = (ImageButton) findViewById(R.id.button01);
            addressText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //
                    dialog = ProgressDialog.show(SetGPS.this, "",
                            "GPS수신중입니다.", true);

                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //Criteria 클래스를 이용하여 요구조건을 명시하여, 가장 적합한 기술을 결정
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.NO_REQUIREMENT);
                    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                    criteria.setAltitudeRequired(false);
                    criteria.setBearingRequired(false);
                    criteria.setSpeedRequired(false);
                    criteria.setCostAllowed(true);

                    final String provider = manager.getBestProvider(criteria, true);
                    location = manager.getLastKnownLocation(provider);
                    try {
                        updateLocation(location.getLatitude(), location.getLongitude());
                        Toast.makeText(getApplicationContext(), "" + Id + " " + ps + " " + mi + " ", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), "GPS 수신 중입니다. 기다려주세요.", Toast.LENGTH_SHORT).show();
                    }
                    //이동
                    android.os.Handler handler = new android.os.Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Intent intent = new Intent(SetGPS.this, SetSubway.class);
                            //정보 전달
                            intent.putExtra("AddressData", sLocationInfo);
                            intent.putExtra("IDDATA", Id);
                            intent.putExtra("PASSWORDDATA", ps);
                            intent.putExtra("MIRRORDATA", mi);
                            startActivity(intent);
                            overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_left, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_left);
                            finish();
                        }
                    };
                    if(addressText.getText().length()!=0)
                        handler.sendEmptyMessageDelayed(0, 2000);
                }
            });
        }
        private void updateLocation(double lat, double lng) {
            if (location != null) {
                try {
                    List<Address> address = gc.getFromLocation(lat, lng, 1);
                    if (address != null) {
                        Address addr = address.get(0);
                        for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
                            String addLine = addr.getAddressLine(i);
                            sLocationInfo += String.format("%s", addLine);
                            addressText.setText(sLocationInfo);
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "찾지못함", Toast.LENGTH_SHORT).show();
                }
                location.setLatitude(lat);
                location.setLongitude(lng);
            } else {
                sLocationInfo = "위치를 찾을 수 없음";
            }
        }
LocationListener mLocationListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        //   updateLocation(location.getLatitude(),location.getLongitude());
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            double longitude = location.getLongitude();    //경도
            double latitude = location.getLatitude();         //위도
            float accuracy = location.getAccuracy();        //신뢰도
        }
        else {
//Network 위치제공자에 의한 위치변화
//Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
};
        private void registerLocationUpdates() {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1, mLocationListener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, mLocationListener);
//1000은 1초마다, 1은 1미터마다 해당 값을 갱신한다는 뜻으로, 딜레이마다 호출하기도 하지만
//위치값을 판별하여 일정 미터단위 움직임이 발생 했을 때에도 리스너를 호출 할 수 있다.
        }
}