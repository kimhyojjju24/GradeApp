package com.example.pojangapp.storedetail;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pojangapp.R;
import com.example.pojangapp.main.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreDetailReviewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RatingBar ratingArg;
    TextView textReviewNone;

    String store_num;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_ID = "user_id";
    private static final String TAG_STAR = "review_star";
    private static final String TAG_MAIN_MENU = "main_menu";
    private static final String TAG_MENU_CNT = "menu_count";
    private static final String TAG_DATE = "review_date";
    private static final String TAG_DETAIL = "review_detail";

    JSONArray detailReview = null;
    ArrayList<HashMap<String, String>> storeDetailReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_store_detail_review, container, false);

        recyclerView = viewGroup.findViewById(R.id.recyclerViewReview);
        layoutManager = new LinearLayoutManager(getContext());
        ratingArg = viewGroup.findViewById(R.id.ratingArg);
        textReviewNone = viewGroup.findViewById(R.id.textReviewNone);
        textReviewNone.setVisibility(View.GONE);

        store_num = Store.getStoreNum();

        storeDetailReview = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_storedetail_review.php");


        recyclerView.setLayoutManager(layoutManager);;
        recyclerView.setHasFixedSize(true);

        return viewGroup;
    }


    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            detailReview = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < detailReview.length(); i++){
                JSONObject c = detailReview.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                if (num.equals(store_num)) {
                    String id = c.getString(TAG_ID);
                    String star = c.getString(TAG_STAR);
                    String main = c.getString(TAG_MAIN_MENU);
                    String cnt = c.getString(TAG_MENU_CNT);
                    String date = c.getString(TAG_DATE);
                    String detail = c.getString(TAG_DETAIL);

                    HashMap<String, String> reviewlist = new HashMap<String, String>();

                    reviewlist.put(TAG_NUM, num);
                    reviewlist.put(TAG_ID, id);
                    reviewlist.put(TAG_STAR, star);
                    reviewlist.put(TAG_MAIN_MENU, main);
                    reviewlist.put(TAG_MENU_CNT, cnt);
                    reviewlist.put(TAG_DATE, date);
                    reviewlist.put(TAG_DETAIL, detail);

                    storeDetailReview.add(reviewlist);
                }else continue;
            }
            if (storeDetailReview.size() == 0){
                ratingArg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                textReviewNone.setVisibility(View.VISIBLE);
            }else {
                setReviewArg(storeDetailReview);

                StoreDetailReviewAdapter adapter = new StoreDetailReviewAdapter(
                        getContext(), storeDetailReview, R.layout.review_card,
                        new String[]{TAG_NUM, TAG_ID, TAG_STAR, TAG_MAIN_MENU, TAG_MENU_CNT, TAG_DATE, TAG_DETAIL},
                        new int[]{R.id.textReviewTitle, R.id.textReviewBuy, R.id.ratingBar, R.id.textContents, R.id.textReviewDate}
                );
                recyclerView.setAdapter(adapter);


            }


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

    public void setReviewArg(ArrayList<HashMap<String, String>> arr){
        double arg;
        double sum = 0.0;
        for (int i = 0; i < arr.size(); i++){
            String rating = arr.get(i).get("review_star");
            sum += Double.parseDouble(rating);
        }
        arg = sum/arr.size();
        ratingArg.setRating((float) arg);
    }

}