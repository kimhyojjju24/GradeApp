package com.example.pojangapp.pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.data.DataSetClass;
import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.bootpay.model.Item;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CustomViewHolder>  {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;
    setFragmentDatailAdapter listener;
    setPriceData data;

    int payTotalPrice, totalPrice;
    String mainMenu, storeName, menuCnt;
    Item menuList;
    ArrayList<Item> menuArray = new ArrayList<>();

    public CartAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to, setFragmentDatailAdapter listener, setPriceData dataset){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
        this.listener = listener;
        this.data = dataset;
    }

    @NonNull
    @Override
    public CartAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CustomViewHolder holder, int position) {
        for (int i = 0; i < arrayList.size(); i++){
            if (holder.getLayoutPosition() == i){
                String menuNum = arrayList.get(i).get(string[0]).toString();
                String menuName = arrayList.get(i).get(string[1]).toString();
                menuCnt = arrayList.get(i).get(string[2]).toString();
                String menuPrice = arrayList.get(i).get(string[3]).toString();
                totalPrice = Integer.parseInt(arrayList.get(i).get(string[4]).toString());
                String storeCate = arrayList.get(i).get(string[6]).toString();
                storeName = arrayList.get(i).get(string[5]).toString();

                holder.title.setText(menuName);
                holder.cnt.setText(menuCnt);
                holder.price.setText(menuPrice +"원");
                holder.priceTotal.setText(String.valueOf(totalPrice));

                if (mainMenu == null){
                    mainMenu = menuName;
                }

                payTotalPrice += totalPrice;

                menuList = new Item(menuName, Integer.parseInt(menuCnt), menuNum, Double.parseDouble(menuPrice), storeCate, storeName,"");

                menuArray.add(menuList);

            }
        }

        if (Integer.parseInt(menuCnt) > 1){
            listener.setData(payTotalPrice, menuArray, storeName,  mainMenu + "외 " + (Integer.parseInt(menuCnt)-1)+"개");
        }else {
            listener.setData(payTotalPrice, menuArray, storeName,  mainMenu );
        }

        holder.buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postData = "Data1=" + Account.getUser_id() + "&Data2=" + arrayList.get(position).get("menu_num");
                DataSetClass.setData("http://118.67.131.210/php/insert/deleteuser_store_cart_menu.php", postData);
                int current;
                current = payTotalPrice - Integer.parseInt(arrayList.get(position).get(string[4]).toString());
                data.setPriceData(v, position, payTotalPrice, current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        TextView cnt;
        TextView priceTotal;
        Button buttonOrder, buttonCancle;


        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.cartMenu);
            price = view.findViewById(R.id.cartPrice);
            cnt = view.findViewById(R.id.textNumber);
            priceTotal = view.findViewById(R.id.cartPriceTotal);
            buttonOrder = view.findViewById(R.id.buttonOrder);
            buttonCancle = view.findViewById(R.id.cartCancel);

        }
    }


    public void setHolderClickListener(setFragmentDatailAdapter listener) {
        this.listener = listener ;
    }

}


