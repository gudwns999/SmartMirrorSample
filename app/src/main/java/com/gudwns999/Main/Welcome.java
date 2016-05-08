package com.gudwns999.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by HyungJun on 2015-08-10.
 */
public class Welcome extends Activity {
    static String Id,ps,mi,AddressName,SubwayName,subwayCode,subwayDirection;
    //지역코드로 바꾸기 위해 필요한 국가 / 도시 / 구 / 동.
    String localNum;
    String[] localCdoe;
    ImageButton welcomeButton;
    private static final String MY_DB = "my_db";
    //이 페이지에서 한꺼번에 서버로 전송할 꺼임.
    //JSON을 통해 지역코드로 바꿔주기.
    URL RSSurl = null;
    rcvJson RJ;
    JSONObject JStoken;
    String getJSON;
    ProgressDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.welcome_layout);
        welcomeButton = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.welcome_btn);
        //버튼 최상위
        welcomeButton.bringToFront();
        //생성한 비디오뷰 선언
        final VideoView videoView = (VideoView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.videoView1);
        //비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
        MediaController mediaController = new MediaController(this);
        //비디오뷰에 연결
        mediaController.setAnchorView(videoView);
        //안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣는다.
        Uri video = Uri.parse("android.resource://"+getPackageName()+"/raw/player");
        //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용
        videoView.setMediaController(mediaController);
        //비디오뷰에 재생할 동영상주소를 연결
        videoView.setVideoURI(video);
        //비디오뷰를 포커스하도록 지정
        videoView.requestFocus();
        //동영상 재생
        videoView.start();
        //자동재생
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp){
                videoView.start();
            }
        });


        //정보 다 받기
        Intent intent = getIntent();
        Id = intent.getExtras().getString("IDDATA");
        ps = intent.getExtras().getString("PASSWORDDATA");
        mi = intent.getExtras().getString("MIRRORDATA");
        AddressName = intent.getExtras().getString("AddressData");
        SubwayName = intent.getExtras().getString("SubwayStation");
        subwayCode = intent.getExtras().getString("SubwayStation1");
        subwayDirection = intent.getExtras().getString("SubwayStation2");

        //Address를 잘게 쪼개기. localCode[0] = 나라, [1] = 도시, [2] = 구, [3] = 동 [4] = 상세주소.
        StringTokenizer stringTokenizer = new StringTokenizer(AddressName);
        localCdoe = new String[stringTokenizer.countTokens()];
        int cnt=0;
        while(stringTokenizer.hasMoreTokens()){
            localCdoe[cnt]=stringTokenizer.nextToken();
            cnt++;
        }
        //다음 버튼 눌렀을 시 GPS와 지하철역 세팅이 시작된다.
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Welcome.this, "",
                        "서버에 입력중 입니다..", true);
                createCode();

                new SendPost().execute();
                Intent intent = new Intent(Welcome.this, STGVActivity.class);
                startActivity(intent);

                SharedPreferences sp = getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
                //자동 로그인
                SharedPreferences.Editor e1 = sp.edit();
                e1.putString("userNameCache", Id);
                e1.putString("userPwdCache", ps);
                e1.putString("userMirrorCache", mi);
                e1.putString("userAddressCache", AddressName);
                e1.putString("userSubwayCache", SubwayName);
                e1.putBoolean("loginCheck", true);
                e1.commit();

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
            //모든 작업을 마치고 실행할 일.
        }
        //실제 전송하는 부분
        public String executeClient(){
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            String tmp = "No";
            post.add(new BasicNameValuePair("email", Id));
            post.add(new BasicNameValuePair("password", ps));
            post.add(new BasicNameValuePair("autherti_code", mi));
            post.add(new BasicNameValuePair("camera_url", tmp));
            post.add(new BasicNameValuePair("local_code", localNum));
            post.add(new BasicNameValuePair("subway_station", SubwayName));
            post.add(new BasicNameValuePair("subway_station_code", subwayCode));
            post.add(new BasicNameValuePair("subway_direction", subwayDirection));

            HttpClient client = new DefaultHttpClient();
            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            // Post객체 생성
            //HttpPost httpPost = new HttpPost("http://10.30.34.238/test.php");
            HttpPost httpPost = new HttpPost("http://52.74.13.142/smartPHP/insertDB.php");
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

    public void createCode(){

        try{
            String temp;
            JSONArray JSA;
            RSSurl = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt");
            RJ = new rcvJson();
            RJ.start();
            RJ.join();
            JSA = new JSONArray(getJSON);
            temp="";
            for(int i=0; i < JSA.length(); i++){
                JStoken = JSA.getJSONObject(i);
                if(JStoken.get(JStoken.names().getString(0)).equals(this.localCdoe[1])){
                    temp += JStoken.get(JStoken.names().getString(1));
                    break;
                }
            }
            RSSurl = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+temp+".json.txt");
            RJ = new rcvJson();
            RJ.start();
            RJ.join();
            JSA = new JSONArray(getJSON);
            temp="";
            for(int i=0; i < JSA.length(); i++){
                JStoken = JSA.getJSONObject(i);
                if(JStoken.get(JStoken.names().getString(0)).equals(this.localCdoe[2])){
                    temp += JStoken.get(JStoken.names().getString(1));
                    break;
                }
            }
            RSSurl = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+temp+".json.txt");
            RJ = new rcvJson();
            RJ.start();
            RJ.join();
            JSA = new JSONArray(getJSON);
            temp="";
            for(int i=0; i < JSA.length(); i++){
                JStoken = JSA.getJSONObject(i);
                //             x = JStoken.get(JStoken.names().getString(2)).toString();
                //             y = JStoken.get(JStoken.names().getString(1)).toString();
                if(JStoken.get(JStoken.names().getString(0)).equals(this.localCdoe[3])) {
                    temp += JStoken.get(JStoken.names().getString(3));
                    break;
                }
            }
            Toast.makeText(getApplicationContext(),"지역코드:"+temp,Toast.LENGTH_SHORT).show();
            localNum = temp;
        }catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"에러만 뜸!",Toast.LENGTH_SHORT).show();
        }
    }
    private class rcvJson extends Thread{
        public void run(){
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(RSSurl.openStream(),"UTF-8"));
                getJSON = in.readLine();
            }catch (Exception e){
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
