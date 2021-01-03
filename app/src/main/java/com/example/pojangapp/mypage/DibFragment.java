package com.example.pojangapp.mypage;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


public class DibFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    String user_id;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_PHOTO = "store_photo";
    private static final String TAG_LOC = "store_loc";
    private static final String TAG_OT = "store_opentime";
    private static final String TAG_CT = "store_closetime";
    private static final String TAG_TIME = "";
    private static final String TAG_ISOPEN = "store_isOpen";

    JSONArray stores = null;
    ArrayList<HashMap<String, String>> diblist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_dib, container, false);
        recyclerView = viewGroup.findViewById(R.id.recyclerDibList);
        layoutManager = new LinearLayoutManager(getContext());
        swipeRefreshLayout = viewGroup.findViewById(R.id.swipe_layout);

        user_id = Account.getUser_id();

        diblist = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_diblist.php");


        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                diblist.clear();
                getData("http://118.67.131.210/php/PHP_diblist.php");
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return viewGroup;

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
            stores = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < stores.length(); i++) {
                JSONObject c = stores.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)) {
                    String num = c.getString(TAG_NUM);
                    String name = c.getString(TAG_NAME);
                    String photo = c.getString(TAG_PHOTO);
                    String loc = c.getString(TAG_LOC);
                    String ot = c.getString(TAG_OT);
                    String ct = c.getString(TAG_CT);
                    String time = ot.substring(0, 5) + " ~ " + ct.substring(0, 5);
                    String isOpen = c.getString(TAG_ISOPEN);

                    HashMap<String, String> storelist = new HashMap<String, String>();

                    storelist.put(TAG_NUM, num);
                    storelist.put(TAG_NAME, name);
                    storelist.put(TAG_PHOTO, photo);
                    storelist.put(TAG_LOC, loc);
                    storelist.put(TAG_TIME, time);
                    storelist.put(TAG_ISOPEN, isOpen);

                    diblist.add(storelist);

                }

            }

            StoreDibAdapter adapter = new StoreDibAdapter(
                    getContext(), diblist, R.layout.store_list_item,
                    new String[]{TAG_NAME, TAG_LOC, TAG_TIME},
                    new int[]{R.id.store_name, R.id.store_loc, R.id.store_time}
            );

            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}