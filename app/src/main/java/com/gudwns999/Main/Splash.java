package com.gudwns999.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class Splash extends Activity {
    private static final String MY_DB = "my_db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.splash_layout);
        //자동 로그인
        SharedPreferences sp = getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
        new SendPost().execute();
        boolean loginCheck = sp.getBoolean("loginCheck", false);
        if(!loginCheck){
            android.os.Handler handler = new android.os.Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    startActivity(new Intent(Splash.this, STGVActivity.class));
                    finish();
                }
            };
            handler.sendEmptyMessageDelayed(0,1000);
        }
        else{
            android.os.Handler handler = new android.os.Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    startActivity(new Intent(Splash.this, STGVActivity.class));
                    finish();
                }
            };
            handler.sendEmptyMessageDelayed(0,1000);        }
    }

    private class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void...unused){
            String content = executeClient();
            return content;
        }
        protected void onPostExecute(String result){
            //모든 작업을 마치고 실행할 일.
        }
        //실제 전송하는 부분
        public String executeClient(){
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

            HttpClient client = new DefaultHttpClient();
            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            // Post객체 생성
            //HttpPost httpPost = new HttpPost("http://10.30.34.238/test.php");
            HttpPost httpPost = new HttpPost("http://52.74.13.142/photo/searchDirectory.php");
            //      HttpPost httpPost = new HttpPost("http://52.74.13.142/test1.php");
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);
                return EntityUtils.getContentCharSet(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}