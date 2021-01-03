package com.example.pojangapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.map.MapFragment;
import com.example.pojangapp.pay.PayFragment;
import com.example.pojangapp.mypage.DibFragment;
import com.example.pojangapp.pay.CartListFragment;
import com.example.pojangapp.R;
import com.example.pojangapp.emoney.EmoneyChargeFragment;
import com.example.pojangapp.emoney.EmoneyHistoryFragment;
import com.example.pojangapp.mypage.MypageHistoryFragment;
import com.example.pojangapp.mypage.MypageInfoFragment;
import com.example.pojangapp.mypage.MypageReviewFragment;
import com.example.pojangapp.pay.PayLoaderFragment;
import com.example.pojangapp.pay.PaySuccessFragment;
import com.example.pojangapp.storedetail.create.StoreCreateComplainFragment;
import com.example.pojangapp.storedetail.create.StoreCreateReviewFragment;
import com.example.pojangapp.storelist.StoreList1Fragment;
import com.example.pojangapp.storelist.StoreList2Fragment;
import com.example.pojangapp.storelist.StoreList3Fragment;
import com.example.pojangapp.storelist.StoreList4Fragment;
import com.example.pojangapp.storelist.StoreList5Fragment;
import com.example.pojangapp.storelist.StoreList6Fragment;
import com.example.pojangapp.storelist.StoreList7Fragment;
import com.example.pojangapp.storelist.StoreList8Fragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity implements Serializable {

    ImageView userPhoto;
    TextView userName;
    TextView userNick;

    TabLayout tabLayout;
    TextView textView;
    ViewPager pager;
    String[] tabName;
    int tabCount;
    Toolbar toolbar;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_NAME = "user_name";
    private static final String TAG_NICKNAME = "user_nickname";
    private static final String TAG_PHOTO = "user_photo";

    JSONArray users = null;
    ArrayList<HashMap<String, String>> userinfo;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textView = findViewById(R.id.textView);
        // 탭이 요구되는 화면에서 표시됨
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);

        userPhoto = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        userNick = findViewById(R.id.userNick);

        toolbar = findViewById(R.id.toolBar);

        String getTitleName = getIntent().getStringExtra("titleName");
        int getPageIndex = getIntent().getIntExtra("pageIndex",0);

        //My page 화면의 ActionBar에서 표시되는 사용자의 정보 : 이름, 닉네임, 사진
        //다른 화면에서는 공간까지 비활성화 시켜줌
        userPhoto.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        userNick.setVisibility(View.GONE);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());


        // 다른 class에서 전달 받은 화면 이름과, (매장카테고리의 경우)탭 페이지 인덱스 번호를
        // 적용시켜 viewpager에 관련된 fragment파일을 연결 시켜줌
        // 여러 페이지에서 액션바, 탭의 코드 중복을 방지하기위해 작성함
        switch (getTitleName){
            case "포차머니" :
                textView.setText("포차머니");
                tabName = getResources().getStringArray(R.array.포차머니);
                tabCount = tabName.length;
                pager.setOffscreenPageLimit(tabCount);

                for (String s : tabName){
                    tabLayout.addTab(tabLayout.newTab().setText(s));
                }

                EmoneyChargeFragment emoneyChargeFragment = new EmoneyChargeFragment();
                adapter.addItem(emoneyChargeFragment);

                EmoneyHistoryFragment emoneyHistoryFragment = new EmoneyHistoryFragment();
                adapter.addItem(emoneyHistoryFragment);

                break;

            case "My Page" :
                textView.setText("My Page");
                tabName = getResources().getStringArray(R.array.MyPage);
                tabCount = tabName.length;
                pager.setOffscreenPageLimit(tabCount);

                // 로그인된 계정의 아이디를 대입시킴
                userId = Account.userId;

                userinfo = new ArrayList<HashMap<String, String>>();
                getData("http://118.67.131.210/php/PHP_mypage_user.php");

                // actionbar의 사용자 정보에 관한 뷰를 표시함
                userPhoto.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
                userNick.setVisibility(View.VISIBLE);

                for (String s : tabName){
                    tabLayout.addTab(tabLayout.newTab().setText(s));
                }

                MypageInfoFragment mypageInfoFragment = new MypageInfoFragment();
                adapter.addItem(mypageInfoFragment);

                MypageHistoryFragment mypageHistoryFragment = new MypageHistoryFragment();
                adapter.addItem(mypageHistoryFragment);

                MypageReviewFragment mypageReviewFragment = new MypageReviewFragment();
                adapter.addItem(mypageReviewFragment);

                break;

                // 매장 카테고리 선택 시 카테고리 별 매장 리스트 생성
            case "분식":
            case "붕어빵":
            case "호떡":
            case "탕후루":
            case "꼬꼬닭":
            case "타코야끼":
            case "토스트":
            case "군고구밤":

                textView.setText("매장리스트");
                tabName = getResources().getStringArray(R.array.매장리스트);
                tabCount = tabName.length;
                pager.setOffscreenPageLimit(tabCount);

                for (String s : tabName){
                    tabLayout.addTab(tabLayout.newTab().setText(s));
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }

                StoreList1Fragment storeList1Fragment = new StoreList1Fragment();
                adapter.addItem(storeList1Fragment);
                StoreList2Fragment storeList2Fragment = new StoreList2Fragment();
                adapter.addItem(storeList2Fragment);
                StoreList3Fragment storeList3Fragment = new StoreList3Fragment();
                adapter.addItem(storeList3Fragment);
                StoreList4Fragment storeList4Fragment = new StoreList4Fragment();
                adapter.addItem(storeList4Fragment);
                StoreList5Fragment storeList5Fragment = new StoreList5Fragment();
                adapter.addItem(storeList5Fragment);
                StoreList6Fragment storeList6Fragment = new StoreList6Fragment();
                adapter.addItem(storeList6Fragment);
                StoreList7Fragment storeList7Fragment = new StoreList7Fragment();
                adapter.addItem(storeList7Fragment);
                StoreList8Fragment storeList8Fragment = new StoreList8Fragment();
                adapter.addItem(storeList8Fragment);


                break;

            case "주변매장" :
                textView.setText("주변매장");
                tabLayout.setVisibility(View.GONE);

                //메인 화면에서 주변매장에 접근 할 때 위도,경도 값을 초기화 해줌
                Store.setLat(0.0);
                Store.setLng(0.0);
                MapFragment mapFragment = new MapFragment();
                adapter.addItem(mapFragment);

                break;

            case "장바구니" :
                textView.setText("장바구니");
                tabLayout.setVisibility(View.GONE);

                CartListFragment cartListFragment = new CartListFragment();
                adapter.addItem(cartListFragment);

                break;


            case "찜한매장" :
                textView.setText("찜한 매장");
                tabLayout.setVisibility(View.GONE);

                DibFragment dibFragment = new DibFragment();
                adapter.addItem(dibFragment);

                break;

            case "결제 화면" :
                textView.setText("결제 화면");
                tabLayout.setVisibility(View.GONE);

                PayFragment payFragment = new PayFragment();
                adapter.addItem(payFragment);

                break;

            case "결제 완료" :
                textView.setText("결제 완료");
                tabLayout.setVisibility(View.GONE);

                PaySuccessFragment paySuccessFragment = new PaySuccessFragment();
                adapter.addItem(paySuccessFragment);

                break;

            case "결제중" :
                textView.setText("결제 중");
                tabLayout.setVisibility(View.GONE);

                PayLoaderFragment payLoaderFragment = new PayLoaderFragment();
                adapter.addItem(payLoaderFragment);

                break;

            case "신고하기" :
                textView.setText("신고하기");
                tabLayout.setVisibility(View.GONE);

                StoreCreateComplainFragment storeCreateComplainFragment = new StoreCreateComplainFragment();
                adapter.addItem(storeCreateComplainFragment);

                break;

            case "리뷰작성" :
                textView.setText("리뷰 작성");
                tabLayout.setVisibility(View.GONE);

                StoreCreateReviewFragment storeCreateReviewFragment = new StoreCreateReviewFragment();
                adapter.addItem(storeCreateReviewFragment);

                break;

            case "매장 상세 위치" :
                textView.setText("매장 상세 위");
                tabLayout.setVisibility(View.GONE);

                MapFragment detailMapFragment = new MapFragment();
                adapter.addItem(detailMapFragment);

                break;
        } //switch end

        // 생성된 MyPagerAdapter를 viewpager에 연결
        pager.setAdapter(adapter);
        //viewpager의 위치를 선택된 탭으로 초기화 시킴
        pager.setCurrentItem(getPageIndex);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    private void getData(String url) {
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

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            users = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < users.length(); i++) {
                JSONObject c = users.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(userId)) {
                    String name = c.getString(TAG_NAME);
                    String nickname = c.getString(TAG_NICKNAME);
                    String photo = c.getString(TAG_PHOTO);

                    HashMap<String, String> userlist = new HashMap<String, String>();

                    userlist.put(TAG_ID, id);
                    userlist.put(TAG_NAME, name);
                    userlist.put(TAG_NICKNAME, nickname);
                    userlist.put(TAG_PHOTO, photo);

                    userinfo.add(userlist);

                    setTextUserInfo(userinfo, 0);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // mysql에서 불러온 사용자 정보를 화면에 표시함
    public void setTextUserInfo(ArrayList<HashMap<String, String>> arrayList, int position){
        userName.setText(arrayList.get(position).get(TAG_NAME));
        userNick.setText(arrayList.get(position).get(TAG_NICKNAME));
        userPhoto.setImageResource(R.drawable.img);

        // Glide API를 이용하여 사진 표시
        Glide.with(this).load(arrayList.get(position).get(TAG_PHOTO)).error(R.drawable.img).fallback(R.drawable.img).into(userPhoto);
    }
}
