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
import com.example.pojangapp.storedetail.StoreDetailReviewAdapter;

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


public class MypageReviewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String user_id;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_ID = "user_id";
    private static final String TAG_STAR = "review_star";
    private static final String TAG_MAIN_MENU = "main_menu";
    private static final String TAG_MENU_CNT = "menu_count";
    private static final String TAG_DATE = "review_date";
    private static final String TAG_DETAIL = "review_detail";

    JSONArray detailReview = null;
    ArrayList<HashMap<String, String>> mypageReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_review, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerViewReview);
        layoutManager = new LinearLayoutManager(getContext());

        user_id = Account.getUser_id();

        recyclerView.setLayoutManager(layoutManager);;
        recyclerView.setHasFixedSize(true);

        mypageReview = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_mypage_review.php");

        return viewGroup;
    }


    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            detailReview = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < detailReview.length(); i++){
                JSONObject c = detailReview.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)) {
                    String num = c.getString(TAG_NUM);
                    String name = c.getString(TAG_NAME);
                    String star = c.getString(TAG_STAR);
                    String main = c.getString(TAG_MAIN_MENU);
                    String cnt = c.getString(TAG_MENU_CNT);
                    String date = c.getString(TAG_DATE);
                    String detail = c.getString(TAG_DETAIL);

                    HashMap<String, String> reviewlist = new HashMap<String, String>();

                    reviewlist.put(TAG_NUM, num);
                    reviewlist.put(TAG_NAME, name);
                    reviewlist.put(TAG_STAR, star);
                    reviewlist.put(TAG_MAIN_MENU, main);
                    reviewlist.put(TAG_MENU_CNT, cnt);
                    reviewlist.put(TAG_DATE, date);
                    reviewlist.put(TAG_DETAIL, detail);

                    mypageReview.add(reviewlist);
                }else continue;
            }

            MypageReviewAdapter adapter = new MypageReviewAdapter(
                    getContext(), mypageReview, R.layout.review_card,
                    new String[]{TAG_NUM, TAG_STAR, TAG_MAIN_MENU, TAG_MENU_CNT, TAG_DATE, TAG_DETAIL},
                    new int[]{R.id.textReviewTitle, R.id.textReviewBuy, R.id.ratingBar, R.id.textContents, R.id.textReviewDate}
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
