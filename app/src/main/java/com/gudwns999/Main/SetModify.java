package com.gudwns999.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HyungJun on 2015-08-10.
 */
public class SetModify extends Activity{
    private static final String MY_DB = "my_db";
    SharedPreferences sp;
    private static final int DIALOG_MESSAGE=1;
    String userEmailCache;
    String userPwdCache;
    String userMirrorCache;
    String userAddressCache;
    String userSubwayCache;

    TextView bt0;
    TextView bt1;
    TextView bt2;

    ImageButton bt3;
    Button bt4;

        @Override
        protected Dialog onCreateDialog(int id){
            switch (id) {
                case DIALOG_MESSAGE:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(" 로그아웃").setMessage("로그아웃 하시겠습니까?").setCancelable(false).setNeutralButton("네",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sp= getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor e1 = sp.edit();
                                    e1.clear();
                                    e1.commit();
                                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SetModify.this, LoginForm.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("아니오",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){dialog.cancel();}
                    });
                    AlertDialog alert = builder.create();
                    return alert;
            }
            return null;
        }
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.setmodify_layout);
            //커스텀 액션바로 갈아타기
            ActionBar actionBar = null;
            actionBar = getActionBar();
            View layout = (View) View.inflate(this, com.bulletnoid.android.gudwns999.Main.R.layout.customactionbar2_layout, null);
            ImageButton button = (ImageButton) layout.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.imageButton12);
            actionBar.setCustomView(layout);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SetModify.this, STGVActivity.class);
                    startActivity(intent);
                    overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
                }
            });

            //자동로긴에 저장된 값 불러오기.
            sp= getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
            SharedPreferences.Editor e2 = sp.edit();
            userEmailCache = sp.getString("userNameCache","입력된 이메일 없음");
            userPwdCache = sp.getString("userPwdCache","입력된 패스워드 없음");
            userMirrorCache = sp.getString("userMirrorCache","입력된 거울정보 없음");
            userAddressCache = sp.getString("userAddressCache","입력된 주소 없음");
            userSubwayCache = sp.getString("userSubwayCache","입력된 지하철역 없음");

            bt0 = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.setting_button);
            bt1 = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.button1);
            bt2 = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.button2);
            bt3 = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.button3);
            bt4 = (Button)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.button4);

            //버튼 이름.
            bt0.setText(userEmailCache); bt0.setTextSize(25);
            bt1.setText(userAddressCache); bt1.setTextSize(25);
            bt2.setText(userSubwayCache); bt2.setTextSize(25);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SetModify.this, ModifyGPS.class);
                    SetModify.this.startActivity(intent);
                    overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_left, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_left);
                    finish();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SetModify.this, ModifySubway.class);
                    SetModify.this.startActivity(intent);
                    overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_left, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_left);
                    finish();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DIALOG_MESSAGE);
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp= getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
                    SharedPreferences.Editor e1 = sp.edit();
                    e1.clear();
                    e1.commit();
                    new SendPost().execute();
                    startActivity(new Intent(SetModify.this, LoginForm.class));
                    overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
                    finish();
                }
            });
        }
private class SendPost extends AsyncTask<Void, Void, String> {
    protected String doInBackground(Void...unused){
        String content = executeClient();
        return content;
    }
    protected void onPostExecute(String result){
     }
    //실제 전송하는 부분
    public String executeClient(){
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("email", userEmailCache));
        HttpClient client = new DefaultHttpClient();
        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);
        HttpPost httpPost = new HttpPost("http://52.74.13.142/smartPHP/removeAll.php");
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