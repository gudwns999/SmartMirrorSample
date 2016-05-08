package com.gudwns999.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterForm extends Activity {
    ImageButton nextButton;
    EditText eMailText;
    EditText passwordText;
    EditText mirroText;
    int btn=0;
    //아이디 중복 확인을 위한 변수
    //로그인 php
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    TextView tv;

    //영문과 숫자만 입력받기 위해.
    protected InputFilter filterAlpha = new InputFilter() {        @Override        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {            Pattern ps = Pattern.compile("^[a-zA-Z0-9~!@#$%^&*()_+-=;.,?/]+$");if(!ps.matcher(source).matches()){return "";}return null;}};
    //숫자만 입력
    protected InputFilter filterNum = new InputFilter() {       @Override   public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {            Pattern ps = Pattern.compile("^[0-9]+$");            if(!ps.matcher(source).matches()){                return "";            }return null;}};
    private static final int DIALOG_MESSAGE=1;
    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case DIALOG_MESSAGE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("회원가입완료").setMessage("다음 정보를 입력해주세요.\n주소, 지하철").setCancelable(false).setNeutralButton("NEXT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterForm.this, SetGPS.class);
                                intent.putExtra("IDDATA",eMailText.getText().toString());
                                intent.putExtra("PASSWORDDATA",passwordText.getText().toString());
                                intent.putExtra("MIRRORDATA",mirroText.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
        }
        return null;
    }
    /*------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.registerform_layout);
        nextButton = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.next_btn);
        //기본 버튼 비활성화
        nextButton.setEnabled(false);
        eMailText = (EditText)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.editText1);
        //영어 및 숫자 특수문자만 입력받게.
        eMailText.setFilters(new InputFilter[]{filterAlpha});
        //입력이 있으면 버튼 활성화로 바꾸게.
        eMailText.addTextChangedListener(emailWatcher);
        passwordText = (EditText)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.editText2);
        passwordText.setFilters(new InputFilter[]{filterAlpha});
        passwordText.addTextChangedListener(pwdWatcher);
        mirroText = (EditText)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.editText3);
        mirroText.setFilters(new InputFilter[]{filterNum});
        mirroText.addTextChangedListener(mirrorWatcher);
        tv = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.register_txt);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(RegisterForm.this, "",
                        "기다려주세요...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkDuplication();
                    }
                }).start();
            }
        });
    }
    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private TextWatcher pwdWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            btn++;
        }
    };
    private TextWatcher mirrorWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
    //        nextButton.setBackgroundResource(R.drawable.background);
            if(eMailText.getText().toString().length()!=0 && passwordText.getText().toString().length()!=0 && mirroText.getText().toString().length()!=0) {
                nextButton.setEnabled(true);
                nextButton.setImageResource(com.bulletnoid.android.gudwns999.Main.R.drawable.joinbutton);
            }
        }
    };
    //eamil과 password와 mirror 값이 null이 아닐때만 서버 DB에 전송해 중복값이 있는지 확인한다.
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
            post.add(new BasicNameValuePair("email", eMailText.getText().toString()));
            post.add(new BasicNameValuePair("password", passwordText.getText().toString()));
            post.add(new BasicNameValuePair("autherti_code", mirroText.getText().toString()));

            HttpClient client = new DefaultHttpClient();
            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // Post객체 생성
            HttpPost httpPost = new HttpPost("http://52.74.13.142/duplicationCheck.php");

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
    //아이디와 거울 동시 중복값 찾아내기 위한 기능.
    void checkDuplication(){
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://52.74.13.142/smartPHP/duplicationCheck.php"); // make sure the url is correct. Test
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email",eMailText.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("autherti_code",mirroText.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
            //        tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        showAlert();
                    }
                });
            }else if(response.equalsIgnoreCase("No Such User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        showDialog(DIALOG_MESSAGE);
                    }
                });
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        RegisterForm.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterForm.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}