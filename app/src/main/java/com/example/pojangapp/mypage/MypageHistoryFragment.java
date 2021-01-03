package com.example.pojangapp.mypage;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MypageHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String myJSON;
    String user_id;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_NUM = "buy_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_DATE = "buy_date";
    private static final String TAG_HAS_REVIEW = "buy_hasReview";
    private static final String TAG_PRICE = "menu_price";

    JSONArray history = null;
    ArrayList<HashMap<String, String>> mypageHistory;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_history, container, false);

        user_id = Account.getUser_id();

        recyclerView = viewGroup.findViewById(R.id.recyclerViewHistory);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mypageHistory = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_mypage_history_up.php");
        return viewGroup;
    }


    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            history = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < history.length(); i++){
                JSONObject c = history.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)) {
                    String num = c.getString(TAG_NUM);
                    String name = c.getString(TAG_NAME);
                    String date = c.getString(TAG_DATE);
                    String hasReview = c.getString(TAG_HAS_REVIEW);
                    String price = c.getString(TAG_PRICE);

                    HashMap<String, String> historyList = new HashMap<String, String>();

                    historyList.put(TAG_ID, id);
                    historyList.put(TAG_NUM, num);
                    historyList.put(TAG_NAME, name);
                    historyList.put(TAG_DATE, date);
                    historyList.put(TAG_HAS_REVIEW, hasReview);
                    historyList.put(TAG_PRICE, price);

                    mypageHistory.add(historyList);

                }else continue;
            }

            MypageHistoryAdapter adapter = new MypageHistoryAdapter(
                    getContext(), mypageHistory, R.layout.mypage_buy_item,
                    new String[]{TAG_NUM, TAG_NAME, TAG_DATE, TAG_HAS_REVIEW, TAG_PRICE},
                    new int[]{R.id.textBuyTitle, R.id.textBuyDate, R.id.textTotalPrice, R.id.textisReview, R.id.menuRecycler}
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
