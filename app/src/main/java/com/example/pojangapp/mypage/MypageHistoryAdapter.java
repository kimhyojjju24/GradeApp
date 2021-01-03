package com.example.pojangapp.mypage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.create.BuyReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MypageHistoryAdapter extends RecyclerView.Adapter<MypageHistoryAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "buy_num";
    private static final String TAG_MENU_NAME = "menu_name";
    private static final String TAG_CNT = "buy_menu_cnt";
    private static final String TAG_TOTAL = "total_price";

    JSONArray menu = null;
    ArrayList<BuyItem> historyMenu = new ArrayList<>();

    ArrayList<RecyclerView> recyclerViewList = new ArrayList<>();
    ArrayList<String> buyNum = new ArrayList<>();
    ArrayList<Integer> pos = new ArrayList<>();

    String name, cnt, str, num;

    public MypageHistoryAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }


    @NonNull
    @Override
    public MypageHistoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypage_buy_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MypageHistoryAdapter.CustomViewHolder holder, final int position) {
        for (int i = 0; i < arrayList.size(); i++){
            if (holder.getLayoutPosition() == i){
                pos.add(position);
                buyNum.add(arrayList.get(i).get(string[0]).toString());
                String buyName = arrayList.get(i).get(string[1]).toString();
                String buyDate = arrayList.get(i).get(string[2]).toString();
                String buyHasReview = arrayList.get(i).get(string[3]).toString();
                String buyPrice = arrayList.get(i).get(string[4]).toString();

                holder.textTitle.setText(buyName);
                holder.textDate.setText(buyDate.substring(0,11));
                if (buyHasReview.equals("1")){
                    holder.textisReview.setText("리뷰 남기기 완료");
                    holder.textisReview.setTextColor(0xff999999);
                }else {
                    holder.textisReview.setText("리뷰 남기러 가기 >");
                    holder.textisReview.setTextColor(0xffffaa5f);
                    holder.textisReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Integer.parseInt(cnt) > 1){
                                str = name + "외" + (Integer.parseInt(cnt) - 1);
                            } else
                                str = name;

                            Intent intent = new Intent(fragment, MenuActivity.class);
                            intent.putExtra("titleName", "리뷰작성");
                            Store.setStoreName(buyName);
                            BuyReview.setBuyNum(arrayList.get(position).get(string[0]).toString());
                            BuyReview.setBuyMenu(str);
                            v.getContext().startActivity(intent);
                        }
                    });
                }
                holder.textTotal.setText(buyPrice +" 원");

                String bnIdx = buyNum.get(position);
                GetDataJSON getDataJSON = new GetDataJSON();
                getDataJSON.execute(bnIdx);

                recyclerViewList.add(holder.recyclerView);

                holder.recyclerView.setVisibility(View.GONE);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int vis = holder.recyclerView.getVisibility();
                        if (vis == View.GONE){
                            holder.recyclerView.setVisibility(View.VISIBLE);
                        }else if(vis == View.VISIBLE)
                            holder.recyclerView.setVisibility(View.GONE);
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        TextView textDate;
        TextView textisReview;
        TextView textTotal;
        CardView cardView;
        RecyclerView recyclerView;

        public CustomViewHolder(View view) {
            super(view);

            textTitle = view.findViewById(R.id.textBuyTitle);
            textDate = view.findViewById(R.id.textBuyDate);
            textisReview = view.findViewById(R.id.textisReview);
            textTotal = view.findViewById(R.id.textTotalPrice);
            cardView = view.findViewById(R.id.cardView);
            recyclerView = view.findViewById(R.id.menuRecycler);
        }
    }


    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            menu = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < menu.length(); i++){
                JSONObject c = menu.getJSONObject(i);

                num = c.getString(TAG_NUM);
                name = c.getString(TAG_MENU_NAME);
                cnt = c.getString(TAG_CNT);
                String total = c.getString(TAG_TOTAL);

                BuyItem buyItem = new BuyItem();

                buyItem.setBuyNum(num);
                buyItem.setMenuName(name);
                buyItem.setMenuCnt(cnt);
                buyItem.setMenuPrice(total);

                historyMenu.add(buyItem);

                setMenuRecyclerView(recyclerViewList, buyNum, historyMenu, pos);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class GetDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];

            String link = "http://118.67.131.210/php/PHP_mypage_history_under.php?num=" + id;

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


    public void setMenuRecyclerView(ArrayList<RecyclerView> recyclerView, ArrayList<String> bn, ArrayList<BuyItem> items, ArrayList<Integer> pos){
        for (int i = 0; i < pos.size(); i++) {
            int position = pos.get(i);

            RecyclerView rv = recyclerView.get(position);
            rv.setLayoutManager(new LinearLayoutManager(fragment));

            ArrayList<BuyItem> bi = new ArrayList<>();

            for (int j = 0; j < items.size(); j++){
                if (items.get(j).getBuyNum().equals(bn.get(position))){
                    bi.add(items.get(j));
                }
            }

            BuyItemAdapter buyItemAdapter = new BuyItemAdapter(fragment, bi, R.layout.buy_detail_item);
            rv.setAdapter(buyItemAdapter);
        }

    }

}



