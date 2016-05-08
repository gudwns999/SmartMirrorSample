package com.gudwns999.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginForm extends Activity {
    LinearLayout mainLayout;
    ImageButton loginButton;
    TextView tv;
    TextView registerButton;
    TextView userEmail;
    TextView userPass;
    int patientCnt=0;

    //로그인 php
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    //영문과 숫자만 입력받기 위해.
    protected InputFilter filterAlpha = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {            Pattern ps = Pattern.compile("^[a-zA-Z0-9~!@#$%^&*()_+-=;.,?/]+$");            if(!ps.matcher(source).matches()){                return "";            }            return null;        }
    };

    @Override
    protected void onCreate(final Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.loginform_layout);
        //화면 누를시 소프트키보드 사라지게 하기
        mainLayout = (LinearLayout)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.mainLayout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userEmail.getWindowToken(), 0);imm.hideSoftInputFromWindow(userPass.getWindowToken(), 0);
            }
        });
        //E-Mail이기 때문에 영문과 숫자만 허용.

        loginButton = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.login_btn);
        registerButton = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.register_btn);

        userEmail = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.userEmail_text);
        userEmail.setFilters(new InputFilter[]{filterAlpha});

        userPass = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.userPass_text);
        tv = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.textView);
        //초기화
        userEmail.setText(null);
        userPass.setText(null);
        userPass.addTextChangedListener(pwdWatcher);

        loginButton.setEnabled(false);

        //로그인 버튼을 눌렀을 시.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 관련 함수 정의 후 사진 화면을 뿌려주는 MainView로 넘어감.
                if (userEmail.getText().toString().length() == 0 && userPass.getText().toString().length() == 0) {
                    patientCnt = patientCnt + 1;
                    onCreate(savedInstancedState);
                } else if (userEmail.getText().toString().length() == 0) {
                    patientCnt = patientCnt + 1;
                    onCreate(savedInstancedState);
                } else if (userPass.getText().toString().length() == 0) {
                    patientCnt = patientCnt + 1;
                    onCreate(savedInstancedState);
                } else if (userEmail.getText().toString().length() != 0 && userEmail.getText().toString().length() != 0) {
                    dialog = ProgressDialog.show(LoginForm.this, "",
                            "확인중입니다. 기다려주세요...", true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    }).start();
                }
            }
        });
        //회원 가입 버튼을 눌렀을 시.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 가입 화면으로 넘어감.
                Intent intent = new Intent(LoginForm.this, RegisterForm.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private TextWatcher pwdWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            loginButton.setEnabled(true);
            loginButton.setImageResource(com.bulletnoid.android.gudwns999.Main.R.drawable.login);
        }
    };
    void login(){
        try{
            httpclient=new DefaultHttpClient();
//            httppost= new HttpPost("http://10.30.38.74/SmartMirror/check.php"); // make sure the url is correct. Test
            httppost= new HttpPost("http://52.74.13.142/smartPHP/checkID.php"); // make sure the url is correct. Test

            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("userid",userEmail.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("userpass",userPass.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginForm.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(LoginForm.this, STGVActivity.class));
                finish();
            }else{
                showAlert();
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        LoginForm.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginForm.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
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