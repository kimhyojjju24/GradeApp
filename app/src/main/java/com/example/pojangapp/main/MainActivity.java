package com.example.pojangapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.pojangapp.R;
import com.example.pojangapp.main.banner.AutoScrollPagerAdapter;
import com.example.pojangapp.main.banner.AutoScrollViewPager;
import com.example.pojangapp.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton buttonEmoney;
    ImageButton buttonMypage;
    ImageButton buttonNearbyMap;
    ImageButton buttonCart;

    ImageButton btnStore1;
    ImageButton btnStore2;
    ImageButton btnStore3;
    ImageButton btnStore4;
    ImageButton btnStore5;
    ImageButton btnStore6;
    ImageButton btnStore7;
    ImageButton btnStore8;

    ImageButton buttonRefresh;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    SearchView searchView;

    String myJSON;

    //전송 받는 JSON데이터의 고유 키
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_OT = "store_opentime";
    private static final String TAG_CT = "store_closetime";
    private static final String TAG_TIME = "store_time";
    private static final String TAG_LAT = "store_lat";
    private static final String TAG_LNG = "store_lng";
    private static final String TAG_IMG = "store_photo";

    JSONArray stores = null;
    ArrayList<HashMap<String, String>> storelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonEmoney = findViewById(R.id.buttonEmoney);
        buttonMypage = findViewById(R.id.buttonMypage);
        buttonNearbyMap = findViewById(R.id.buttonMap);
        buttonCart = findViewById(R.id.buttonCart);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        btnStore1 = findViewById(R.id.btnStore1);
        btnStore2 = findViewById(R.id.btnStore2);
        btnStore3 = findViewById(R.id.btnStore3);
        btnStore4 = findViewById(R.id.btnStore4);
        btnStore5 = findViewById(R.id.btnStore5);
        btnStore6 = findViewById(R.id.btnStore6);
        btnStore7 = findViewById(R.id.btnStore7);
        btnStore8 = findViewById(R.id.btnStore8);

        buttonEmoney.setOnClickListener(this);
        buttonMypage.setOnClickListener(this);
        buttonNearbyMap.setOnClickListener(this);
        buttonCart.setOnClickListener(this);

        btnStore1.setOnClickListener(this);
        btnStore2.setOnClickListener(this);
        btnStore3.setOnClickListener(this);
        btnStore4.setOnClickListener(this);
        btnStore5.setOnClickListener(this);
        btnStore6.setOnClickListener(this);
        btnStore7.setOnClickListener(this);
        btnStore8.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);

        storelist = new ArrayList<HashMap<String, String>>();

        getData("http://118.67.131.210/php/PHP_homelist.php");

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        searchView = findViewById(R.id.search_bar);

        //검색 화면으로 이동하는 이벤트
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //신규 매장 새로고침 버튼
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storelist.clear();
                getData("http://118.67.131.210/php/PHP_storelist.php");
            }
        });

        //광고 배너 자동 스크롤 설정
        AutoScrollPagerAdapter autoScrollPagerAdapter = new AutoScrollPagerAdapter(getSupportFragmentManager());
        AutoScrollViewPager viewPager = findViewById(R.id.autoViewPager);
        viewPager.setAdapter(autoScrollPagerAdapter);

        viewPager.startAutoScroll();
        viewPager.setInterval(5000);
        viewPager.setCycle(true);


    }


    //메인 화면에서 버튼 선택시 MenuActivity로 이동하여 화면을 표시해주는 기능
    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        //버튼 이름 (화면 이름)
        String titlename;
        //매장 카테고리 선택 시 선택한 탭의 인덱스
        int pageIndex;

        switch (btnId){
            case R.id.btnStore1:
                pageIndex = 0;
                titlename = "분식";
                break;

            case R.id.btnStore2:
                pageIndex = 1;
                titlename = "붕어빵";
                break;

            case R.id.btnStore3:
                pageIndex = 2;
                titlename = "호떡";
                break;

            case R.id.btnStore4:
                pageIndex = 3;
                titlename = "탕후루";
                break;

            case R.id.btnStore5:
                pageIndex = 4;
                titlename = "꼬꼬닭";
                break;

            case R.id.btnStore6:
                pageIndex = 5;
                titlename = "타코야끼";
                break;

            case R.id.btnStore7:
                pageIndex = 6;
                titlename = "토스트";
                break;

            case R.id.btnStore8:
                pageIndex = 7;
                titlename = "군고구밤";
                break;

            case R.id.buttonEmoney:
                pageIndex = 0;
                titlename = "포차머니";
                break;

            case R.id.buttonMypage:
                pageIndex = 0;
                titlename = "My Page";
                break;

            case R.id.buttonMap:
                pageIndex = 0;
                titlename = "주변매장";
                Store.setLng(0.0);
                Store.setLat(0.0);
                break;

            case R.id.buttonCart:
                pageIndex = 0;
                titlename = "장바구니";
                break;

            default:
                pageIndex = 0;
                titlename = "";
                break;
        }

        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("titleName",titlename);
        intent.putExtra("pageIndex",pageIndex);
        startActivity(intent);

    }


    // URL을 이용하여 JSON형태의 데이터를 제공하는 php파일에 접근하는 메서드

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

    // getData()를 통해 전달받은 php파일을 JSON Object로 생성
    // JSON Object를 배열로 나눠 읽은 후 HashMap 객체에 저장
    // 생성된 HashMap 객체를 표시할 Adapter에 전송
    // recyclerview에 Adapter 적용
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            stores = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < stores.length(); i++) {
                JSONObject c = stores.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                String name = c.getString(TAG_NAME);
                String opentime = c.getString(TAG_OT);
                String closetime = c.getString(TAG_CT);
                String time = opentime.substring(0, 5) + " ~ " + closetime.substring(0, 5);
                String lat = c.getString(TAG_LAT);
                String lng = c.getString(TAG_LNG);
                String img = c.getString(TAG_IMG);

                HashMap<String, String> stores = new HashMap<String, String>();

                stores.put(TAG_NUM, num);
                stores.put(TAG_NAME, name);
                stores.put(TAG_TIME, time);
                stores.put(TAG_LAT, lat);
                stores.put(TAG_LNG, lng);
                stores.put(TAG_IMG, img);

                storelist.add(stores);
            }

            StoreCardAdapter adapter = new StoreCardAdapter(
                    this, storelist, R.layout.store_card_item,
                    new String[]{TAG_NUM, TAG_NAME, TAG_TIME},
                    new int[]{R.id.store_name, R.id.store_time}
            );

            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
