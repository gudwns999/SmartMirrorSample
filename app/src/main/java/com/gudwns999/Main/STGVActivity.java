package com.gudwns999.Main;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gudwns999.ImageGridView.StaggeredGridView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class STGVActivity extends Activity {
    StaggeredGridView stgv;
    STGVAdapter mAdapter;
    //XML 파싱해오기.
    String str1;
    String[] skt;
    Document doc = null;
    LinearLayout layout = null;
    Vector<String> titlevec = new Vector<String>();
    Vector<String> descvec = new Vector<String>();
    GetXMLTask task = new GetXMLTask();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bulletnoid.android.gudwns999.Main.R.layout.ac_stgv);
        //액션바
        ActionBar actionBar = null;
        actionBar = getActionBar();
        View layout = (View) View.inflate(this, com.bulletnoid.android.gudwns999.Main.R.layout.customactionbar1_layout, null);
        ImageButton button = (ImageButton) layout.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.imageButton1);
        actionBar.setCustomView(layout);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        //액션바 셋팅 누르면 넘어가기.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(STGVActivity.this, SetModify.class));
                overridePendingTransition(com.bulletnoid.android.gudwns999.Main.R.anim.slide_in_left, com.bulletnoid.android.gudwns999.Main.R.anim.slide_out_left);
            }
        });
        //XML로 URL가져오기.
        task.execute(null, null, null);
        int i=0, j=0;
        while (true) {
            try {
                Thread.sleep(1000); //1.0초마다 실행
                if (task.flag == true) {
                    titlevec = task.titlevec;
                    descvec = task.descvec;
                    break; //반복문 종료
                }
            } catch (Exception e) {
            }
        }
        str1 = descvec.toString();
        str1 = str1.replace("[","").replace("]", "").replace(",", "");
        StringTokenizer stok = new StringTokenizer(str1);
        skt = new String[stok.countTokens()];

        int cnt=0;
        while(stok.hasMoreTokens()){
            skt[cnt] = stok.nextToken();            cnt++;
        }
        stgv = (StaggeredGridView) findViewById(com.bulletnoid.android.gudwns999.Main.R.id.stgv);
        int margin = getResources().getDimensionPixelSize(com.bulletnoid.android.gudwns999.Main.R.dimen.stgv_margin);
        stgv.setItemMargin(margin);
        stgv.setPadding(margin, 0, margin, 0);
   //     stgv.setHeaderView(new Button(this));
        View footerView;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(com.bulletnoid.android.gudwns999.Main.R.layout.layout_loading_footer, null);
        stgv.setFooterView(footerView);
        mAdapter = new STGVAdapter(this, getApplication());
        stgv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        stgv.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
                new LoadMoreTask().execute();
            }
        });
    }
    public class LoadMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.getMoreItem();
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }
    private class STGVAdapter extends BaseAdapter {
        private Context mContext;
        private Application mAppContext;
        private DataSet mData = new DataSet();
        private ArrayList<Item> mItems = new ArrayList<Item>();

        private int newPos = 19;

        public STGVAdapter(Context context, Application app) {
            mContext = context;
            mAppContext = app;
            getMoreItem();
        }

        public void getMoreItem() {
            /*
            for (int i = 0; i < 20; i++) {
                Item item = new Item();
                item.url = mData.url[i];
                item.width = mData.width[i];
                item.height = mData.height[i];
                mItems.add(item);
            }*/
            for(int i=0;i<skt.length;i++){
                Item item = new Item();
                item.url = skt[i];
                item.width = mData.width[i];
                item.height = mData.height[i];
                mItems.add(item);
            }
        }

        public void getNewItem() {
            Item item = new Item();
            item.url = mData.url[newPos];
            item.width = mData.width[newPos];
            item.height = mData.height[newPos];
            mItems.add(0, item);
            newPos = (newPos - 1) % 19;
        }

        @Override
        public int getCount() {
            return mItems == null ? 0 : mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            final Item item = mItems.get(position);

            String url = item.url;

            if (convertView == null) {
                Holder holder = new Holder();
                view = View.inflate(mContext, com.bulletnoid.android.gudwns999.Main.R.layout.cell_stgv, null);
                holder.img_content = (STGVImageView) view.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.img_content);
                holder.tv_info = (TextView) view.findViewById(com.bulletnoid.android.gudwns999.Main.R.id.tv_info);

                view.setTag(holder);
            } else {
                view = convertView;
            }

            Holder holder = (Holder) view.getTag();

            /**
             * StaggeredGridView has bugs dealing with child TouchEvent
             * You must deal TouchEvent in the child view itself
             **/
            holder.img_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            holder.tv_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            holder.tv_info.setText(position + " : " + item.width + "/" + item.height);

            holder.img_content.mHeight = item.height;
            holder.img_content.mWidth = item.width;

            Picasso.with(mAppContext).load(url).into(holder.img_content);
            return view;
        }
        class Holder {
            STGVImageView img_content;
            TextView tv_info;
        }
    }
    //XML파싱.
    private class GetXMLTask extends AsyncTask<Void, Void, Void> {
        Vector<String> titlevec = new Vector<String>();
        Vector<String> descvec = new Vector<String>();
        URL url;
        String uri = "http://52.74.13.142/photo/userphoto.xml";
        String tagname = "", title = "", description = "";
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
                        if (tagname.equals("node")) {
                            isInItemTag = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (tagname.equals("title_name") && isInItemTag) {
                            title += xpp.getText();
                        } else if (tagname.equals("description_name") && isInItemTag) {
                            description += xpp.getText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagname = xpp.getName();
                       if (tagname.equals("node")) {
                            titlevec.add(title);
                            descvec.add(description);
                            title = "";
                            description = "";
                            isInItemTag = false;
                        }
                    }eventType = xpp.next();
                } flag = true;
            } catch (Exception e) {
            }            return null;        }
    }
}