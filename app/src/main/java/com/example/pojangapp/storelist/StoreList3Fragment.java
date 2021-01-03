package com.example.pojangapp.storelist;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class StoreList3Fragment extends Fragment{

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Integer> count;

    SwipeRefreshLayout swipeRefreshLayout;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_ADD = "store_loc";
    private static final String TAG_OT = "store_opentime";
    private static final String TAG_CT = "store_closetime";
    private static final String TAG_TIME = "store_time";
    private static final String TAG_ISOPEN = "store_isOpen";
    private static final String TAG_CATEGORY = "store_category";


    JSONArray stores = null;
    ArrayList<HashMap<String, String>> storelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_store_list3, container, false);
        recyclerView = viewGroup.findViewById(R.id.storeListView);
        swipeRefreshLayout = viewGroup.findViewById(R.id.swipe_layout);

        storelist = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_storelist.php");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storelist.clear();
                getData("http://118.67.131.210/php/PHP_storelist.php");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());

        count = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        return viewGroup;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            stores = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < stores.length(); i++) {
                JSONObject c = stores.getJSONObject(i);
                if (c.getInt(TAG_CATEGORY) == 3) {
                    String num = c.getString(TAG_NUM);
                    String name = c.getString(TAG_NAME);
                    String address = c.getString(TAG_ADD);
                    String opentime = c.getString(TAG_OT);
                    String closetime = c.getString(TAG_CT);
                    String time = opentime.substring(0, 5) + " ~ " + closetime.substring(0, 5);
                    String isOpen = c.getString(TAG_ISOPEN);

                    HashMap<String, String> stores = new HashMap<String, String>();

                    stores.put(TAG_NUM, num);
                    stores.put(TAG_NAME, name);
                    stores.put(TAG_ADD, address);
                    stores.put(TAG_TIME, time);
                    stores.put(TAG_ISOPEN, isOpen);

                    storelist.add(stores);
                } else continue;
            }

            StoreListAdapter adapter = new StoreListAdapter(
                    getContext(), storelist, R.layout.store_list_item,
                    new String[]{ TAG_NAME, TAG_ADD, TAG_TIME},
                    new int[]{R.id.store_name, R.id.store_loc, R.id.store_time}
            );

            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
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



}
