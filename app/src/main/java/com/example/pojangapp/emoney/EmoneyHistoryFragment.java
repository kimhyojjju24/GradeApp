package com.example.pojangapp.emoney;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.emoney.timerecycler.MyData;
import com.example.pojangapp.emoney.timerecycler.TimeRecyclerAdapter;
import com.example.pojangapp.main.StoreCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class EmoneyHistoryFragment extends Fragment{

    RecyclerView mTimeRecyclerView;

    String myJSON;
    String user_id;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_CURRENT = "pay_current";
    private static final String TAG_ISCHARGE = "pay_isCharge";
    private static final String TAG_PRICE = "pay_price";
    private static final String TAG_DETAIL = "pay_detail";
    private static final String TAG_DATE = "pay_date";

    JSONArray pay = null;
    ArrayList<MyData> eMoneyHistory = new ArrayList<>();

    int use = 99999999;

    TextView textBalance, textName, textEmpty;

    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_emoney_history, container, false);

        user_id = Account.getUser_id();

        textName = viewGroup.findViewById(R.id.textName);
        textBalance = viewGroup.findViewById(R.id.textBalance);
        textEmpty = viewGroup.findViewById(R.id.isEmptyEmoney);
        linearLayout = viewGroup.findViewById(R.id.header);
        textName.setText(user_id + "님의");
        textEmpty.setText(user_id + "님의 포차머니 내역이 비었습니다.");

        mTimeRecyclerView = (RecyclerView) viewGroup.findViewById(R.id.mTimeRecyclerView);
        mTimeRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTimeRecyclerView.setLayoutManager(layoutManager);

        getData("http://118.67.131.210/php/PHP_emoneylist.php");

        return viewGroup;
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


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            pay = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < pay.length(); i++) {
                JSONObject c = pay.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)){
                    String detail = c.getString(TAG_DETAIL);
                    String current = c.getString(TAG_CURRENT);
                    int isCharge = Integer.parseInt(c.getString(TAG_ISCHARGE));
                    String price = c.getString(TAG_PRICE);
                    String date = c.getString(TAG_DATE);

                    int year = Integer.parseInt(date.substring(0,4));
                    int month = Integer.parseInt(date.substring(5,7));
                    int day = Integer.parseInt(date.substring(8,10));

                    MyData data = new MyData(year, month, day, price, current, isCharge, detail);

                    if (use == 99999999){
                        use = Integer.parseInt(current);
                    }
                    eMoneyHistory.add(data);
                }
            }
            if(eMoneyHistory.size() == 0){
                linearLayout.setVisibility(View.GONE);
                mTimeRecyclerView.setVisibility(View.GONE);
                textEmpty.setVisibility(View.VISIBLE);
            }else {
                textBalance.setText("잔액  " + use +"원");
                TimeRecyclerAdapter adapter = new TimeRecyclerAdapter(eMoneyHistory);
                mTimeRecyclerView.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
