package com.example.pojangapp.storedetail;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreDetailMenuFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String store_num, user_id;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_MNUM = "menu_num";
    private static final String TAG_MENU = "menu_name";
    private static final String TAG_PRICE = "menu_price";
    private static final String TAG_ISMAIN = "menu_isMain";
    private static final String TAG_ISSOLDOUT = "menu_isSoldout";
    private static final String TAG_NOT_ZERO = "notZero";

    JSONArray detailMenu = null;
    ArrayList<HashMap<String, String>> storeDetailMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_store_detail_menu, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerViewMenu);
        layoutManager = new LinearLayoutManager(getContext());

        store_num = Store.getStoreNum();
        user_id = Account.getUser_id();

        storeDetailMenu = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_storedetail_menu.php");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return viewGroup;
    }

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            detailMenu = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < detailMenu.length(); i++){
                JSONObject c = detailMenu.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                if (num.equals(store_num)) {
                    String menu_num = c.getString(TAG_MNUM);
                    String menu = c.getString(TAG_MENU);
                    String price = c.getString(TAG_PRICE);
                    String isSoldout = c.getString(TAG_ISSOLDOUT);
                    String isMain = c.getString(TAG_ISMAIN);
                    String notZero = c.getString(TAG_NOT_ZERO);

                    HashMap<String, String> detailMenulist = new HashMap<String, String>();

                    detailMenulist.put(TAG_NUM, num);
                    detailMenulist.put(TAG_ID, user_id);
                    detailMenulist.put(TAG_MNUM, menu_num);
                    detailMenulist.put(TAG_MENU, menu);
                    detailMenulist.put(TAG_PRICE, price);
                    detailMenulist.put(TAG_ISSOLDOUT, isSoldout);
                    detailMenulist.put(TAG_ISMAIN, isMain);
                    detailMenulist.put(TAG_NOT_ZERO, notZero);

                    storeDetailMenu.add(detailMenulist);
                }else continue;
            }

            StoreDetailMenuAdapter adapter = new StoreDetailMenuAdapter(
                    getContext(), storeDetailMenu, R.layout.store_menu_item,
                    new String[]{TAG_MENU, TAG_PRICE},
                    new int[]{R.id.textMenuName, R.id.textMenuPrice}
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
