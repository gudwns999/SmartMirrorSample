package com.gudwns999.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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
public class SetSubway extends Activity {
    RelativeLayout mainLayout;
    AutoCompleteTextView AutoSubway;
    ImageButton subwayButton;

    Vector<String> codevec = new Vector<String>();
    Vector<String> stationvec = new Vector<String>();

    GetLineOneXMLTask taskOne = new GetLineOneXMLTask();
    GetLineTwoXMLTask taskTwo = new GetLineTwoXMLTask();
    GetLineThreeXMLTask taskThree = new GetLineThreeXMLTask();
//1호선 2호선 3호선 4호선 5호선 6호선 7호선 8호선 9호선
    String Codetmp, Stationtmp;
    String[] oneCode,oneStation,twoCode,twoStation,threeCode, threeStation, fourCode, fourStation, fiveCode, fiveStation;

    int[] line = {com.bulletnoid.android.gudwns999.Main.R.drawable.traina, com.bulletnoid.android.gudwns999.Main.R.drawable.trainb, com.bulletnoid.android.gudwns999.Main.R.drawable.trainc, com.bulletnoid.android.gudwns999.Main.R.drawable.traind, com.bulletnoid.android.gudwns999.Main.R.drawable.traine,
            com.bulletnoid.android.gudwns999.Main.R.drawable.trainf, com.bulletnoid.android.gudwns999.Main.R.drawable.traing, com.bulletnoid.android.gudwns999.Main.R.drawable.trainh, com.bulletnoid.android.gudwns999.Main.R.drawable.traini};

    String subwayStation,subwayCode, subwayDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.setsubway_layout);
        subwayButton = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.subway_btn);
        subwayButton.setEnabled(false);
        //배경 누르면 키보드 사라지게
        mainLayout = (RelativeLayout)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.setsubway_layout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(AutoSubway.getWindowToken(), 0);
            }
        });

        //정보받기
        Intent intent = getIntent();
        final String Id = intent.getExtras().getString("IDDATA");
        final String ps = intent.getExtras().getString("PASSWORDDATA");
        final String mi = intent.getExtras().getString("MIRRORDATA");
        final String AdderessName = intent.getExtras().getString("AddressData");

        //XML읽기
        taskOne.execute(null, null, null);
        LineOne(taskOne);
        taskTwo.execute(null, null, null);
        LineTwo(taskTwo);
        taskThree.execute(null,null,null);
        LineThree(taskThree);



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
        String[] from = { "호선","역","코드"};

        int[] to = { com.bulletnoid.android.gudwns999.Main.R.id.flag, com.bulletnoid.android.gudwns999.Main.R.id.txt};
        SimpleAdapter adapter = new SimpleAdapter(this, aList, com.bulletnoid.android.gudwns999.Main.R.layout.customautocomplete_layout, from, to);
        AutoSubway = (AutoCompleteTextView) findViewById(com.bulletnoid.android.gudwns999.Main.R.id.subwayText);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                AutoSubway.setText(hm.get("역"));
                subwayStation = hm.get("역");
                subwayCode = hm.get("코드");
                subwayDirection = hm.get("상/하위");
                subwayButton.setBackgroundResource(com.bulletnoid.android.gudwns999.Main.R.drawable.next);
                subwayButton.setEnabled(true);
            }
        };
        AutoSubway.setOnItemClickListener(itemClickListener);
        AutoSubway.setAdapter(adapter);
        //다음 버튼 눌렀을 시 GPS와 지하철역 세팅이 시작된다.
        subwayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetSubway.this, Welcome.class);
         //       Toast.makeText(getApplicationContext(), "" + Id + " " + ps + " " + mi + " " + AdderessName, Toast.LENGTH_SHORT).show();
                //정보전달
                intent.putExtra("IDDATA", Id);
                intent.putExtra("PASSWORDDATA", ps);
                intent.putExtra("MIRRORDATA", mi);
                intent.putExtra("AddressData", AdderessName);
                intent.putExtra("SubwayStation", subwayStation);
                intent.putExtra("SubwayStation1", subwayCode);
                intent.putExtra("SubwayStation2", subwayDirection);

                startActivity(intent);
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_left, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_left);
                finish();
            }
        });
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
}
