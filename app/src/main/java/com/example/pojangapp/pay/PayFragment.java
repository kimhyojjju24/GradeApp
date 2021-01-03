package com.example.pojangapp.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;
import com.example.pojangapp.mypage.BuyItem;
import com.example.pojangapp.mypage.MypageHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.Item;

import static java.lang.Math.round;


public class PayFragment extends Fragment {

    String title;
    String subtitle;
    double totalPrice;
    ArrayList<Item> menuList;

    double priceValue;

    String user_id;

    TextView textcategory;
    TextView textprice;

    TextView textEmoney;
    TextView textPrice;
    TextView textPayPrice;

    LinearLayout layoutRadio;
    LinearLayout layoutMoney;

    Button buttonPay, buttonSubmit;
    EditText editEmoney;

    int editPrice;

    int stuck = 10;


    String myJSON;

    private static final String TAG_RESULTS = "result";

    String res, postData, menuData, phoneNumber;

    ArrayList<HashMap<String, Integer>> arrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_pay, container, false);

        user_id = Account.getUser_id();

        textcategory = viewGroup.findViewById(R.id.textCategory);
        textprice = viewGroup.findViewById(R.id.textPrice);

        textEmoney = viewGroup.findViewById(R.id.textEMoney);
        textPrice = viewGroup.findViewById(R.id.textCurrentPrice);
        textPayPrice = viewGroup.findViewById(R.id.textPay);

        layoutRadio = viewGroup.findViewById(R.id.radio);
        layoutMoney = viewGroup.findViewById(R.id.emoney);

        buttonPay = viewGroup.findViewById(R.id.buttonPay);
        buttonSubmit = viewGroup.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPrice = Integer.parseInt(editEmoney.getText().toString());
                priceValue = totalPrice - editPrice;

                textEmoney.setText(editPrice+" 원");
                textPrice.setText(round(totalPrice) + " 원");
                textPayPrice.setText(round(priceValue) + " 원");
                PayList.setPayPrice(priceValue);
            }
        });
        editEmoney = viewGroup.findViewById(R.id.editMoney);

        title = PayList.getMainTitle();
        subtitle = PayList.getSubTitle();
        totalPrice = PayList.getTotalPrice();
        priceValue = PayList.getTotalPrice();
        menuList = PayList.getMenuList();
        PayList.setPayPrice(priceValue);

        textcategory.setText(title);
        textprice.setText(subtitle);

        textEmoney.setText("0 원");
        textPrice.setText(round(totalPrice) + "원");
        textPayPrice.setText(round(priceValue) + "원");

        if (textcategory.getText().equals("포차머니")){
            layoutMoney.setVisibility(View.GONE);
        }else {
            layoutRadio.setVisibility(View.GONE);
        }

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute(user_id);

        int time = (int) new Date().getTime();
        String buyNum = user_id + "p" + Math.abs(time);
        BootpayAnalytics.init(getContext(), "5f6176d24f74b40025e35c0b");

        int lth = PayList.getMenuList().size();

        for (int i = 0; i < lth; i++){
            HashMap<String, Integer> hashMap = new HashMap<>();
            hashMap.put(PayList.getMenuList().get(i).getUnique(), PayList.getMenuList().get(i).getQty());

            arrayList.add(hashMap);
        }

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MenuActivity.class);
                intent.putExtra("titleName", "결제중");
                startActivity(intent);
            }
        });

        return viewGroup;
    }


    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            res = jsonObj.getString(TAG_RESULTS);
            Account.setUserPhone( "0"+ res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class GetDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];

            String link = "http://118.67.131.210/php/PHP_account.php?user_id=" + id;

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


    public void setData(String url, String postData) {
        class SetData extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(postData.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    String result = readStream(conn.getInputStream());
                    conn.disconnect();

                    return result;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String result = s;
                if(result.equals("1")){
                    Toast.makeText(getContext(),"처리 성공",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"처리 실패",Toast.LENGTH_SHORT).show();
                }
            }

            private String readStream(InputStream in) throws IOException {
                StringBuilder jsonHtml = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = null;

                while ((line = reader.readLine()) != null)
                    jsonHtml.append(line);

                reader.close();
                return jsonHtml.toString();
            }
        }
        SetData s = new SetData();
        s.execute(url, postData);
    }

}