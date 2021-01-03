package com.example.pojangapp.storedetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pojangapp.data.DataSetClass;
import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;
import com.example.pojangapp.main.Store;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreDetailActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView textViewNotice;
    TextView textViewTitle;
    LinearLayout noticeLayout;
    String snum, name;
    String user_id;

    ImageButton buttonCall, buttonComplain, buttonDib, buttonShare;
    String pn, dibID;

    Boolean isDib = false;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_NOTICE = "store_notice";
    private static final String TAG_PHONE = "store_phone";
    private static final String TAG_ID = "dib_id";

    JSONArray detail = null;
    ArrayList<HashMap<String, String>> storedetail;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        storedetail = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_storedetail.php");
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nestedScrollView);
        scrollView.setFillViewport (true);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        textViewNotice = findViewById(R.id.textNotice);
        textViewTitle = findViewById(R.id.textTitle);
        noticeLayout = findViewById(R.id.layoutNotice);

        snum = Store.getStoreNum();

        buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:0" + pn));
                startActivity(intent);
            }
        });

        buttonComplain = findViewById(R.id.buttonCom);
        buttonComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MenuActivity.class);
                intent.putExtra("titleName", "신고하기");
                startActivity(intent);
            }
        });


        buttonDib = findViewById(R.id.buttonDib);
        buttonDib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_id = Account.getUser_id();
                String postData;

                if (isDib == true) {
                    buttonDib.setBackgroundResource(R.drawable.unlike);
                    postData = "Data1=" + user_id + "&Data2=" + snum + "&toggle=delete" ;
                    isDib = false;
                }else {
                    buttonDib.setBackgroundResource(R.drawable.like);
                    postData = "Data1=" + user_id + "&Data2=" + snum + "&toggle=insert" ;
                    isDib = true;
                }
                DataSetClass.setData("http://118.67.131.210/php/insert/insertuser_dib.php", postData);
            }
        });

        buttonShare = findViewById(R.id.buttonShare);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String subject = "포장맛차 매장";
                String text = name +"에서 맛있는 음식을 먹고싶다면? \n지금 포장맛차 앱을 실행 해보세요!";

                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);

                Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                startActivity(chooser);

            }
        });

        textViewNotice.setSelected(true);

        tabLayout.addTab(tabLayout.newTab().setText("위치"));
        tabLayout.addTab(tabLayout.newTab().setText("메뉴"));
        tabLayout.addTab(tabLayout.newTab().setText("리뷰"));

        StoreDetailActivity.MyPagerAdapter adapter = new StoreDetailActivity.MyPagerAdapter(getSupportFragmentManager());


        StoreDetailLocFragment storeDetailLocFragment = new StoreDetailLocFragment();
        adapter.addItem(storeDetailLocFragment);
        StoreDetailMenuFragment storeDetailMenuFragment = new StoreDetailMenuFragment();
        adapter.addItem(storeDetailMenuFragment);
        StoreDetailReviewFragment storeDetailReviewFragment = new StoreDetailReviewFragment();
        adapter.addItem(storeDetailReviewFragment);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            detail = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < detail.length(); i++){
                JSONObject c = detail.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                if (num.equals(snum)) {
                    name = c.getString(TAG_NAME);
                    String notice = c.getString(TAG_NOTICE);
                    String phone = c.getString(TAG_PHONE);
                    dibID = c.getString(TAG_ID);

                    HashMap<String, String> detaillist = new HashMap<String, String>();

                    detaillist.put(TAG_NUM, num);
                    detaillist.put(TAG_NAME, name);
                    detaillist.put(TAG_NOTICE, notice);
                    detaillist.put(TAG_PHONE, phone);

                    if (notice.equals("")){
                        noticeLayout.setVisibility(View.GONE);
                    }

                    storedetail.add(detaillist);

                    setTextStoreDetail(storedetail, 0);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTextStoreDetail(ArrayList<HashMap<String, String>> arrayList, int position) {
        String storeName = arrayList.get(position).get(TAG_NAME);
        Store.setStoreName(storeName);
        textViewTitle.setText(storeName);
        textViewNotice.setText(arrayList.get(position).get(TAG_NOTICE));

        pn = arrayList.get(position).get(TAG_PHONE);

        String[] dibList = dibID.split(",");
        user_id = Account.getUser_id();

        for (int i = 0; i < dibList.length; i++){
            try {
                if (user_id.equals(dibList[i])){
                    isDib = true;
                    buttonDib.setBackgroundResource(R.drawable.like);
                }
            }catch (Exception e){
                buttonDib.setBackgroundResource(R.drawable.unlike);
            }
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }



    class MyPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}