package com.example.pojangapp.search;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.mypage.MypageHistoryAdapter;
import com.example.pojangapp.storelist.StoreListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    String user_id, myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_ADD = "store_loc";
    private static final String TAG_OT = "store_opentime";
    private static final String TAG_CT = "store_closetime";
    private static final String TAG_TIME = "store_time";
    private static final String TAG_ISOPEN = "store_isOpen";
    private static final String TAG_LAT = "store_lat";
    private static final String TAG_LNG = "store_lng";
    private static final String TAG_IMG = "store_photo";

    JSONArray stores = null;
    ArrayList<HashMap<String, String>> storelist;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        user_id = Account.getUser_id();

        searchView = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recyclerViewSearch);

        storelist = new ArrayList<HashMap<String, String>>();

        searchView.onActionViewExpanded();
        searchView.setFocusedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GetDataJSON getDataJSON = new GetDataJSON();
                getDataJSON.execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TextView textSearch = findViewById(R.id.textSearch);
                textSearch.setVisibility(View.GONE);
                if(newText.equals("")){
                    textSearch.setVisibility(View.VISIBLE);
                }else {
                    textSearch.setVisibility(View.GONE);
                }
                return false;
            }
        });

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }



    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            stores = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < stores.length(); i++) {
                JSONObject c = stores.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);
                String opentime = c.getString(TAG_OT);
                String closetime = c.getString(TAG_CT);
                String time = opentime.substring(0, 5) + " ~ " + closetime.substring(0, 5);
                String isOpen = c.getString(TAG_ISOPEN);
                String lat = c.getString(TAG_LAT);
                String lng = c.getString(TAG_LNG);
                String img = c.getString(TAG_IMG);

                HashMap<String, String> stores = new HashMap<String, String>();

                stores.put(TAG_NUM, num);
                stores.put(TAG_NAME, name);
                stores.put(TAG_ADD, address);
                stores.put(TAG_TIME, time);
                stores.put(TAG_ISOPEN, isOpen);
                stores.put(TAG_LAT, lat);
                stores.put(TAG_LNG, lng);
                stores.put(TAG_IMG, img);

                storelist.add(stores);
            }

            StoreListAdapter adapter = new StoreListAdapter(
                    this, storelist, R.layout.store_list_item,
                    new String[]{ TAG_NAME, TAG_ADD, TAG_TIME},
                    new int[]{R.id.store_name, R.id.store_loc, R.id.store_time}
            );

            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GetDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String query = params[0];

            String link = "http://118.67.131.210/php/PHP_search.php?query=" + query;

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(link);
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

}
