package com.example.pojangapp.pay;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.bootpay.model.Item;

import static java.lang.Math.round;

public class CartListFragment extends Fragment {

    Intent intent;

    TextView totalTextView, textEmpty;
    Button buttonOrder;
    LinearLayout layoutInfo;

    int totalCnt;
    int totalPrice;
    String mainMenu;
    ArrayList<Item> menuList;
    String storeName;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CartAdapter adapter;

    String user_id;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_NUM = "menu_num";
    private static final String TAG_NAME = "menu_name";
    private static final String TAG_CNT = "menu_cnt";
    private static final String TAG_PRICE = "menu_price";
    private static final String TAG_TOTAL_PRICE = "total_price";
    private static final String TAG_STORE_NAME = "store_name";
    private static final String TAG_STORE_CATE = "store_category";


    JSONArray cartMenu = null;
    ArrayList<HashMap<String, String>> cartList = new ArrayList<>();

    setFragmentDatailAdapter listener = new setFragmentDatailAdapter() {
        @Override
        public void setData(int price, ArrayList<Item> arrayList, String title, String sub) {
            totalTextView.setText(price + " 원");
            PayList.setMenuList(arrayList);
            PayList.setTotalPrice(Double.parseDouble(String.valueOf(price)));
            PayList.setMainTitle(title);
            PayList.setSubTitle(sub);
        }
    };

    setPriceData dataset = new setPriceData() {
        @Override
        public void setPriceData(View v, int position, int current, int changed) {
            cartList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, cartList.size());
            PayList.getMenuList().remove(position);
            PayList.setTotalPrice(changed);
            if (cartList.size() == 0){
                textEmpty.setVisibility(View.VISIBLE);
                totalTextView.setText("0 원");
                buttonOrder.setClickable(false);
            }else {
                totalTextView.setText(changed + " 원");
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_cart_list, container, false);
        recyclerView = viewGroup.findViewById(R.id.recyclerCartList);

        totalTextView = viewGroup.findViewById(R.id.totalPrice);
        buttonOrder = viewGroup.findViewById(R.id.buttonOrder);
        textEmpty = viewGroup.findViewById(R.id.textEmpty);
        layoutInfo = viewGroup.findViewById(R.id.layoutInfo);

        user_id = Account.getUser_id();

        getData("http://118.67.131.210/php/PHP_cartlist.php");

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        textEmpty.setVisibility(View.GONE);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MenuActivity.class);
                intent.putExtra("titleName", "결제 화면");
                startActivity(intent);
            }
        });



        return viewGroup;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            cartMenu = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < cartMenu.length(); i++) {
                JSONObject c = cartMenu.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)) {
                    String num = c.getString(TAG_NUM);
                    String name = c.getString(TAG_NAME);
                    String cnt = c.getString(TAG_CNT);
                    String price = c.getString(TAG_PRICE);
                    String totalPrice = c.getString(TAG_TOTAL_PRICE);
                    String storeName = c.getString(TAG_STORE_NAME);
                    String storeCate = c.getString(TAG_STORE_CATE);

                    HashMap<String, String> carts = new HashMap<String, String>();

                    carts.put(TAG_NUM, num);
                    carts.put(TAG_NAME, name);
                    carts.put(TAG_CNT, cnt);
                    carts.put(TAG_PRICE, price);
                    carts.put(TAG_TOTAL_PRICE, totalPrice);
                    carts.put(TAG_STORE_NAME, storeName);
                    carts.put(TAG_STORE_CATE, storeCate);

                    cartList.add(carts);
                } else continue;
            }
            if (cartList.size() == 0){
                textEmpty.setVisibility(View.VISIBLE);
                layoutInfo.setVisibility(View.GONE);
            }else {
                adapter = new CartAdapter(
                        getContext(), cartList, R.layout.cart_list_item,
                        new String[]{ TAG_NUM, TAG_NAME, TAG_CNT, TAG_PRICE, TAG_TOTAL_PRICE, TAG_STORE_NAME, TAG_STORE_CATE},
                        new int[]{R.id.cartMenu, R.id.cartPrice, R.id.cartPriceTotal}, listener, dataset
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
}