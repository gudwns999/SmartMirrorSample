package com.gudwns999.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by HyungJun on 2015-08-10.
 */
public class ModifySubway extends Activity {
    //자동로긴 관련
    private static final String MY_DB = "my_db";
    SharedPreferences sp;
    String userEmailCache;

    RelativeLayout mainLayout;
    AutoCompleteTextView AutoSubway;
    ImageButton subwayButton;

    Vector<String> codevec = new Vector<String>();
    Vector<String> stationvec = new Vector<String>();

    GetLineOneXMLTask taskOne = new GetLineOneXMLTask();
    GetLineTwoXMLTask taskTwo = new GetLineTwoXMLTask();
    GetLineThreeXMLTask taskThree = new GetLineThreeXMLTask();
    GetLineFourXMLTask taskFour = new GetLineFourXMLTask();
    GetLineFiveXMLTask taskFive = new GetLineFiveXMLTask();
    GetLineSixXMLTask taskSix = new GetLineSixXMLTask();
    GetLineSevenXMLTask taskSeven = new GetLineSevenXMLTask();
    GetLineEightXMLTask taskEight = new GetLineEightXMLTask();
    GetLineNineXMLTask taskNine = new GetLineNineXMLTask();

    //1호선 2호선 3호선 4호선 5호선 6호선 7호선 8호선 9호선
    String Codetmp, Stationtmp;
    String[] oneCode,oneStation,twoCode,twoStation,threeCode, threeStation, fourCode, fourStation, fiveCode, fiveStation, sixCode, sixStation, sevenCode, sevenStation, eightCode, eightStation, nineCode, nineStation;

    int[] line = {com.bulletnoid.android.gudwns999.Main.R.drawable.traina, com.bulletnoid.android.gudwns999.Main.R.drawable.trainb, com.bulletnoid.android.gudwns999.Main.R.drawable.trainc, com.bulletnoid.android.gudwns999.Main.R.drawable.traind, com.bulletnoid.android.gudwns999.Main.R.drawable.traine,
            com.bulletnoid.android.gudwns999.Main.R.drawable.trainf, com.bulletnoid.android.gudwns999.Main.R.drawable.traing, com.bulletnoid.android.gudwns999.Main.R.drawable.trainh, com.bulletnoid.android.gudwns999.Main.R.drawable.traini};

    String subwayStation,subwayCode, subwayDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.modifysubway_layout);
        //자동 저장된거 불러오기.
        sp= getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
        final SharedPreferences.Editor e2 = sp.edit();
        userEmailCache = sp.getString("userNameCache","입력된 이메일 없음");
//커스텀 액션바로 갈아타기
        ActionBar actionBar = null;
        actionBar = getActionBar();
        View layout = (View) View.inflate(this, com.bulletnoid.android.gudwns999.Main.R.layout.customactionbar4_layout, null);
        ImageButton button = (ImageButton) layout.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.imageButton12);
        actionBar.setCustomView(layout);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        TextView back = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.title_text1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModifySubway.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModifySubway.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
            }
        });
        subwayButton = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.settingsubway_btn);

        //XML읽기
        taskOne.execute(null, null, null);
        LineOne(taskOne);
        taskTwo.execute(null, null, null);
        LineTwo(taskTwo);
        taskThree.execute(null,null,null);
        LineThree(taskThree);
        taskFour.execute(null,null,null);
        LineFour(taskFour);
        taskFive.execute(null,null,null);
        LineFive(taskFive);
        taskSix.execute(null,null,null);
        LineSix(taskSix);
        taskSeven.execute(null,null,null);
        LineSeven(taskSeven);
        taskEight.execute(null,null,null);
        LineEight(taskEight);
        taskNine.execute(null,null,null);
        LineNine(taskNine);



        List<HashMap<String, String>> aList = new ArrayList<HashMap<String,String>>();
        //1호선
        for(int i=0; i<oneStation.length;i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", oneStation[i]);
            hm.put("호선", Integer.toString(line[0]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",oneCode[i]);
            aList.add(hm);
        }
        //2호선
        for(int i=0; i<twoStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", twoStation[i]);
            hm.put("호선", Integer.toString(line[1]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",twoCode[i]);
            aList.add(hm);
        }
        //3호선
        for(int i=0; i<threeStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", threeStation[i]);
            hm.put("호선", Integer.toString(line[2]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",threeCode[i]);
            aList.add(hm);
        }
        //4호선
        for(int i=0; i<fourStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", fourStation[i]);
            hm.put("호선", Integer.toString(line[3]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",fourCode[i]);
            aList.add(hm);
        }
        //5호선
        for(int i=0; i<fiveStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", fiveStation[i]);
            hm.put("호선", Integer.toString(line[4]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",fiveCode[i]);
            aList.add(hm);
        }
        //6호선
        for(int i=0; i<sixStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", sixStation[i]);
            hm.put("호선", Integer.toString(line[5]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",sixCode[i]);
            aList.add(hm);
        }
        //7호선
        for(int i=0; i<sevenStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", sevenStation[i]);
            hm.put("호선", Integer.toString(line[6]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",sevenCode[i]);
            aList.add(hm);
        }
        //8호선
        for(int i=0; i<eightStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", eightStation[i]);
            hm.put("호선", Integer.toString(line[7]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",eightCode[i]);
            aList.add(hm);
        }
        //9호선
        for(int i=0; i<nineStation.length; i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("역", nineStation[i]);
            hm.put("호선", Integer.toString(line[8]) );
            //    hm.put("상/하위", direction[i]);
            hm.put("코드",nineCode[i]);
            aList.add(hm);
        }
        String[] from = { "호선","역","코드"};

        int[] to = { com.bulletnoid.android.gudwns999.Main.R.id.flag, com.bulletnoid.android.gudwns999.Main.R.id.txt};
        SimpleAdapter adapter = new SimpleAdapter(this, aList, com.bulletnoid.android.gudwns999.Main.R.layout.customautocomplete_layout, from, to);
        AutoSubway = (AutoCompleteTextView) findViewById(com.bulletnoid.android.gudwns999.Main.R.id.subwayText1);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                AutoSubway.setText(hm.get("역"));
                subwayStation = hm.get("역");
                subwayCode = hm.get("코드");
                subwayDirection = hm.get("상/하위");
                subwayButton.setEnabled(true);
            }
        };
        AutoSubway.setOnItemClickListener(itemClickListener);
        AutoSubway.setAdapter(adapter);

        subwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPost().execute();
                e2.putString("userSubwayCache", AutoSubway.getText().toString());
                e2.commit();
                startActivity(new Intent(ModifySubway.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
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
            post.add(new BasicNameValuePair("email", userEmailCache));
            post.add(new BasicNameValuePair("subway_station_code",subwayCode));
            post.add(new BasicNameValuePair("subway_station",subwayStation));
            post.add(new BasicNameValuePair("subway_direction",subwayDirection));

            HttpClient client = new DefaultHttpClient();
            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            // Post객체 생성
            //HttpPost httpPost = new HttpPost("http://10.30.34.238/test.php");
            HttpPost httpPost = new HttpPost("http://52.74.13.142/smartPHP/modifySubway.php");
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    private class GetLineOneXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/1/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineOne(GetLineOneXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskOne.flag==true){
                    codevec = taskOne.codevec;
                    stationvec = taskOne.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        oneCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            oneCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        oneStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            oneStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineTwoXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/2/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineTwo(GetLineTwoXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskTwo.flag==true){
                    codevec = taskTwo.codevec;
                    stationvec = taskTwo.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        twoCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            twoCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        twoStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            twoStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineThreeXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/3/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineThree(GetLineThreeXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskThree.flag==true){
                    codevec = taskThree.codevec;
                    stationvec = taskThree.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        threeCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            threeCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        threeStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            threeStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineFourXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/4/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineFour(GetLineFourXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskFour.flag==true){
                    codevec = taskFour.codevec;
                    stationvec = taskFour.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        fourCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            fourCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        fourStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            fourStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineFiveXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/5/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineFive(GetLineFiveXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskFive.flag==true){
                    codevec = taskFive.codevec;
                    stationvec = taskFive.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        fiveCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            fiveCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        fiveStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            fiveStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineSixXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/6/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineSix(GetLineSixXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskSix.flag==true){
                    codevec = taskSix.codevec;
                    stationvec = taskSix.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        sixCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            sixCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        sixStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            sixStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineSevenXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/7/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineSeven(GetLineSevenXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskSeven.flag==true){
                    codevec = taskSeven.codevec;
                    stationvec = taskSeven.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        sevenCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            sevenCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        sevenStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            sevenStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineEightXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/8/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineEight(GetLineEightXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskEight.flag==true){
                    codevec = taskEight.codevec;
                    stationvec = taskEight.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        eightCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            eightCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        eightStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            eightStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
    private class GetLineNineXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> codevec = new Vector<String>();
        Vector<String> stationvec = new Vector<String>();
        Vector<String> numvec = new Vector<String>();
        URL url;
        //1호선
        String uri = "http://openapi.seoul.go.kr:8088/516943625268733134394857784576/xml/SearchSTNBySubwayLineService/1/1000/9/";
        String tagname = "", code = "", station = "", num="";
        boolean flag = false;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                url = new URL(uri);
                InputStream in = url.openStream();
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                boolean isInItemTag = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("STATION_CD") && isInItemTag) {
                            code += xpp.getText();
                        } else if (tagname.equals("STATION_NM") && isInItemTag) {
                            station += xpp.getText();
                        } else if (tagname.equals("LINE_NUM") && isInItemTag) {
                            num += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                        if (tagname.equals("row")) {
                            codevec.add(code);
                            stationvec.add(station);
                            numvec.add(num);

                            code = "";
                            station = "";
                            num = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
    public void LineNine(GetLineNineXMLTask g){
        while(true){
            try{
                Thread.sleep(1000);
                if(taskNine.flag==true){
                    codevec = taskNine.codevec;
                    stationvec = taskNine.stationvec;
                    break;
                }
            }catch(Exception e){            }
        }
        //코드
        Codetmp = codevec.toString();
        Codetmp = Codetmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer codestok = new StringTokenizer(Codetmp);
        nineCode = new String[codestok.countTokens()];
        int cnt=0;
        while(codestok.hasMoreTokens()) {
            nineCode[cnt] = codestok.nextToken();
            cnt++;
        }
        //지하철역
        Stationtmp = stationvec.toString();
        Stationtmp = Stationtmp.replace("[","").replace("]","").replace(",", "");
        StringTokenizer stationstok = new StringTokenizer(Stationtmp);
        nineStation = new String[stationstok.countTokens()];
        int cnt1=0;
        while(stationstok.hasMoreTokens()){
            nineStation[cnt1] = stationstok.nextToken();
            cnt1++;
        }
    };
}
