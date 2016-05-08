package com.gudwns999.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HyungJun on 2015-08-10.
 */
public class ModifyGPS extends Activity implements TextWatcher {
    //JSON을 통해 지역코드로 바꿔주기.
    URL RSSurl = null;
    rcvJson RJ;
    JSONObject JStoken;
    String getJSON;
    String localNum;
    String localCdoe[];
    String si;
    String gu;
    String dong;
    //자동로긴 관련
    private static final String MY_DB = "my_db";
    SharedPreferences sp;
    String userEmailCache;

    //자동완성
    AutoCompleteTextView si_auto;
    AutoCompleteTextView gu_auto;
    AutoCompleteTextView dong_auto;
    //자동완성
    String siList[] = {"서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주특별자치도"};
    String guList[] = {
            //서울특별시
            "종로구","중구","용산구","성동구","광진구","동대문구","중랑구","성북구","강북구","도봉구","노원구","은평구","서대문구","마포구","양천구","강서구","구로구","금천구","영등포구","동작구","관악구","서초구","강남구","송파구","강동구",
            //부산광역시
            "중구","서구","동구","영도구","부산진구","동래구","남구","북구","해운대구","사하구","금정구","강서구","연제구","수영구","사상구","기장군",
            //대구광역시
            "중구","동구","서구","남구","북구","수성구","달서구","달성군",
            //인천광역시
            "중구","동구","남구","연수구","남동구","부평구","계양구","서구","강화군","옹진군",
            //광주광역시
            "동구","서구","남구","북구","광산구",
            //대전광역시
            "동구","중구","서구","유성구","대덕구",
            //울산광역시
            "중구","남구","동구","북구","울주군",
            //경기도
            "수원시장안구","수원시권선구","수원시팔달구","수원시영통구","성남시수정구","성남시중원구","성남시분당구","의정부시","안양시만안구","안양시동안구",
            "부천시원미구","부천시소사구","부천시오정구","광명시","평택시","동두천시","안산시상록구","안산시단원구","고양시덕양구","고양시일산동구","고양시일산서구","과천시"
            ,"구리시","남양주시","오산시","시흥시","군포시","의왕시","하남시","용인시처인구","용인시기흥구","용인시수지구","파주시","이천시","안성시","김포시","화성시","광주시","양주시","포천시"
            ,"여주군","연천군","가평군","양평군",
            //강원도
            "춘천시","원주시","강릉시","동해시","태백시","속초시","삼척시","홍천군","횡성군","영월군","평창군","정선군","철원군","화천군","양구군","인제군","고성군","양양군",
            //충청북도
            "청주시상당구","청주시흥덕구","충주시","제천시","청원군","보은군","옥천군","영동군","증평군","진천군","괴산군","음성군","단양군",
            //충청남도
            "천안시동남구","천안시서북구","공주시","보령시","아산시","서산시","논산시","계룡시","금산군","연기군","부여군","서천군","청양군","홍성군","예산군","태안군","당진군",
            //전라북도
            "전주시완산구","전주시덕진구","군산시","익산시","정읍시","남원시","김제시","완주군","진안군","무주군","장수군","임실군","순창군","고창군","부안군",
            //전라남도
            "목포시","여수시","순천시","나주시","광양시","담양군","곡성군","구례군","고흥군","보성군","화순군","장흥군","강진군","해남군","영암군","무안군","함평군","영광군","장성군","완도군","진도군","신안군",
            //경상북도
            "포항시남구","포항시북구","경주시","김천시","안동시","구미시","영주시","영천시","상주시","문경시","경산시","군위군","의성군","청송군","영양군","영덕군","청도군","고령군","성주군","칠곡군","예천군","봉화군","울진군","울릉군",
            //경상남도
            "창원시","마산시","진주시","진해시","통영시","사천시","김해시","밀양시","거제시","양산시","의령군","함안군","창녕군","고성군","남해군","하동군","산청군","함양군","거창군","합천군",
            //제주특별자치도
            "제주시","서귀포시"
    };
    String dongList[] = {
            //서울 특별시 종로구
            "청운동","효자동","사직동","삼청동","부암동","평창동","무악동","교남동","가회동","종로1.2.3.4가동","종로5.6가동","이화동","혜화동","명륜3가동","창신1동","창신2동","창신3동","숭인1동","숭인2동",
            //서울 특별시 중구
            "소공동","회현동","명동","필동","장충동","광희동","을지로동","신당1동","신당2동","신당3동","신당4동","신당5동","신당6동","황학동","중림동",
            //서울 특별시 용산구
            "후암동","용산2가동","남영동","청파동","원효로1동","원효로2동","효창동","용문동","한강로동","이촌1동","이촌2동","이태원1동","이태원2동","한남동","서빙고동","보광동",
            //서울 특별시 성동구
            "왕십리1동","왕십리2동","도선동","마장동","사근동","행당1동","행당2동","응봉동","금호1가동","금호2가동","금호3가동","금호4가동","옥수1동","옥수2동","성수1가1동","성수1가2동","성수2가1동","성수2가3동", "송정동","용답동",
            //서울 특별시 광진구
            "화양동","군자동","중곡1동","중곡2동","중곡3동","중곡4동","능동","광장동","자양1동","자양2동","자양3동","자양4동","구의1동","구의2동","구의3동",
            //서울 특별시 동대문구
            "신설동","용두1동","용두2동","제기1동","제기2동","전농1동","전농2동","전농3동","전농4동","답십리1동","답십리2동","답십리3동","답십리4동","답십리5동","장안1동","장안2동","장안3동","장안4동","청량리1동","청량리2동","회기동","휘경1동","휘경2동","이문1동","이문2동","이문3동",
            //서울 특별시 중랑구
            "면목2동","면목4동","면목5동","면목본동","면목7동","면목3.8동","상봉1동","상봉2동","중화1동","중화2동","묵1동","묵2동","망우본동","망우3동","신내1동","신내2동",
            //서울 특별시 성북구
            "성북동","삼선동","동선동","돈암1동","돈암2동","안암동","보문동","정릉1동","정릉2동","정릉3동","정릉4동","길음1동","길음2동","종암동","월곡1동","월곡2동","장위1동","장위2동","장위3동","석관동",
            //서울 특별시 강북구
            "삼양동","미아동","송중동","송천동","삼각산동","번1동","번2동","번3동","수유1동","수유2동","수유3동","우이동","인수동",
            //서울 특별시 도봉구
            "창1동","창2동","창3동","창4동","창5동","도봉1동","도봉2동","쌍문1동","쌍문2동","쌍문3동","쌍문4동","방학1동","방학2동","방학3동",
            //서울 특별시 노원구
            "월계1동","월계2동","월계3동","월계4동","공릉2동","공릉1.3동","하계1동","하계2동","중계본동","중계1동","중계2동","중계3동","중계4동","상계1동","상계2동","상계3.4동","상계5동","상계6.7동","상계8동","상계9동","상계10동",
            //서울 특별시 은평구
            "녹번동","불광1동","불광2동","갈현1동","갈현2동","구산동","대조동","응암1동","응암2동","응암3동","역촌동","신사1동","신사2동","증산동","수색동","진관동",
            //서울 특별시 서대문구
            "천연동","북아현동","충현동","신촌동","연희동","홍제1동","홍제3동","홍제2동","홍은1동","홍은2동","남가좌1동","남가좌2동","북가좌1동","북가좌2동",
            //서울 특별시 마포구
            "아현동","공덕동","도화동","용강동","대흥동","염리동","신수동","서강동","서교동","합정동","망원1동","망원2동","연남동","성산1동","성산2동","상암동",
            //서울 특별시 양천구
            "목1동","목2동","목3동","목4동","목5동","신월1동","신월2동","신월3동","신월4동","신월5동","신월6동","신월7동","신정1동","신정2동","신정3동","신정4동","신정6동","신정7동",
            //서울 특별시 강서구
            "염창동","등촌1동","등촌2동","등촌3동","화곡1동","화곡2동","화곡3동","화곡4동","화곡5동","화곡본동","화곡6동","화곡7동","화곡8동","가양1동","가양2동","가양3동","발산1동","발산2동","공항동","방화1동","방화2동","방화3동",
            //서울 특별시 구로구
            "신도림동","구로1동","구로2동","구로3동","구로4동","구로5동","구로본동","가리봉동","고척1동","고척2동","개봉1동","개봉2동","개봉3동","개봉본동","오류1동","오류2동","수궁동",
            //서울 특별시 금천구
            "가산동","독산1동","독산2동","독산3동","독산4동","시흥1동","시흥2동","시흥3동","시흥4동","시흥5동",
            //서울 특별시 영등포구
            "영등포1동","영등포2동","영등포3동","여의동","당산1동","당산2동","도림1동","도림2동","문래1동","문래2동","양평1동","양평2동","신길1동","신길2동","신길3동","신길4동","신길5동","신길6동","신길7동","대림1동","대림2동","대림3동",
            //서울 특별시 동작구
            "노량진1동","노량진2동","상도1동","상도2동","상도3동","상도4동","본동","흑석동","동작동","사당1동","사당2동","사당3동","사당4동","사당5동","대방동","신대방1동","신대방2동",
            //서울 특별시 관악구
            "봉천본동","봉천1동","봉천2동","봉천3동","봉천4동","봉천5동","봉천6동","봉천7동","봉천8동","봉천9동","봉천10동","봉천11동","남현동","신림본동","신림1동","신림2동","신림3동","신림4동","신림5동","신림6동","신림7동","신림8동","신림9동","신림10동","신림11동","신림12동","신림13동",
            //서울 특별시 서초구
            "서초1동","서초2동","서초3동","서초4동","잠원동","반포본동","반포1동","반포2동","반포3동","반포4동","방배본동","방배1동","방배2동","방배3동","방배4동","양재1동","양재2동","내곡동",
            //서울 특별시 강남구
            "신사동","논현1동","논현2동","압구정1동","압구정2동","청담1동","청담2동","삼성1동","삼성2동","대치1동","대치2동","대치3동","대치4동","역삼1동","역삼2동","도곡1동","도곡2동","개포1동","개포2동","개포3동","개포4동","세곡동","일원본동","일원1동","일원2동","수서동",
            //서울 특별시 송파구
            "풍납1동","풍납2동","거여1동","거여2동","마천1동","마천2동","방이1동","방이2동","오륜동","오금동","송파1동","송파2동","석촌동","삼전동","가락본동","가락1동","가락2동","문정1동","문정2동","장지동","잠실본동","잠실2동","잠실3동","잠실4동","잠실6동","잠실7동",
            //서울 특별시 강동구
            "강일동","상일동","명일1동","명일2동","고덕1동","고덕2동","암사1동","암사2동","암사3동","암사4동","천호1동","천호2동","천호3동","천호4동","성내1동","성내2동","성내3동","길1동","길2동","둔촌1동","둔촌2동"
    };
    //버튼
    ImageButton transGPS_btn, back_btn;
    TextView back;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //자동 저장된거 불러오기.
        sp= getSharedPreferences(MY_DB, Context.MODE_PRIVATE);
        final SharedPreferences.Editor e2 = sp.edit();
        userEmailCache = sp.getString("userNameCache","입력된 이메일 없음");
        //커스텀 액션바로 갈아타기
        ActionBar actionBar = null;
        actionBar = getActionBar();
        View layout = (View) View.inflate(this, com.bulletnoid.android.gudwns999.Main.R.layout.customactionbar3_layout, null);
        ImageButton button = (ImageButton) layout.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.imageButton12);
        actionBar.setCustomView(layout);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //시 자동 완성
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.modifygps_layout);
        si_auto = (AutoCompleteTextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.si_auto);
        si_auto.addTextChangedListener(this);
        si_auto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, siList));
        si_auto.setTextColor(Color.RED);
        //구 자동 완성
        gu_auto = (AutoCompleteTextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.gu_auto);
        gu_auto.addTextChangedListener(this);
        gu_auto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, guList));
        gu_auto.setTextColor(Color.BLUE);
        //동 자동 완성
        dong_auto = (AutoCompleteTextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.dong_auto);
        dong_auto.addTextChangedListener(this);
        dong_auto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dongList));
        dong_auto.setTextColor(Color.YELLOW);
        back = (TextView)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.title_text1);
        transGPS_btn = (ImageButton)findViewById(com.bulletnoid.android.gudwns999.Main.R.id.transGps_btn);
        transGPS_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                si=si_auto.getText().toString();
                gu=gu_auto.getText().toString();
                dong=dong_auto.getText().toString();
                createCode();
                new SendPost().execute();
                e2.putString("userAddressCache", si + " " + gu + " " + dong);
                e2.commit();
                startActivity(new Intent(ModifyGPS.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModifyGPS.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModifyGPS.this, SetModify.class));
                finish();
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_right, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_right);
            }
        });

    }

    public void afterTextChanged(Editable arg0){
        //TODO Auto-generated method stub
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after){
        //TODO Auto-generatedc method stub
    }
    public void onTextChanged(CharSequence s, int start, int before, int count){
        //TODO Auto-generated method stub
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
                if(JStoken.get(JStoken.names().getString(0)).equals(this.si)){
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
                if(JStoken.get(JStoken.names().getString(0)).equals(this.gu)){
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
                if(JStoken.get(JStoken.names().getString(0)).equals(this.dong)) {
                    temp += JStoken.get(JStoken.names().getString(3));
                    break;
                }
            }
            Toast.makeText(getApplicationContext(), "지역코드:" + temp, Toast.LENGTH_SHORT).show();
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
            post.add(new BasicNameValuePair("local_code",localNum));

            HttpClient client = new DefaultHttpClient();
            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            // Post객체 생성
            //HttpPost httpPost = new HttpPost("http://10.30.34.238/test.php");
            HttpPost httpPost = new HttpPost("http://52.74.13.142/smartPHP/modifyAddress.php");
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
