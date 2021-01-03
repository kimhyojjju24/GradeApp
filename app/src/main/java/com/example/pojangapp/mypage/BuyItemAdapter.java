package com.example.pojangapp.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BuyItemAdapter extends RecyclerView.Adapter<BuyItemAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<BuyItem> arrayList;
    int layout;

    public BuyItemAdapter(Context context, ArrayList<BuyItem> data, int resource){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
    }

    @NonNull
    @Override
    public BuyItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_detail_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BuyItemAdapter.CustomViewHolder holder, final int position) {

        BuyItem buyItem = arrayList.get(position);
        holder.textViewTitle.setText(buyItem.getMenuName());
        holder.textViewCnt.setText(buyItem.getMenuCnt() + "개");
        holder.textViewPrice.setText(buyItem.getMenuPrice() + "원");
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewCnt;
        TextView textViewPrice;

        public CustomViewHolder(View view) {
            super(view);

            textViewTitle = view.findViewById(R.id.title);
            textViewCnt = view.findViewById(R.id.cnt);
            textViewPrice = view.findViewById(R.id.price);

        }

    }


}



