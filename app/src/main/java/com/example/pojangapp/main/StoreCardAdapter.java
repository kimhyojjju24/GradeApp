package com.example.pojangapp.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.storedetail.StoreDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreCardAdapter extends RecyclerView.Adapter<StoreCardAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;

    private Intent intent;


    public StoreCardAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }


    @NonNull
    @Override
    public StoreCardAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_card_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StoreCardAdapter.CustomViewHolder holder, final int position) {

        for (int i = 0; i < arrayList.size(); i++){
            if (holder.getLayoutPosition() == i){
                holder.title.setText(arrayList.get(i).get(string[1]).toString());
                holder.time.setText(arrayList.get(i).get(string[2]).toString());

                String imageUrl = arrayList.get(i).get("store_photo").toString();
                Glide.with(fragment).load(imageUrl).error(R.drawable.img).fallback(R.drawable.img).into(holder.img);
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), StoreDetailActivity.class);
                Store.setStoreNum(arrayList.get(position).get("store_num").toString());
                Store.setStoreName(arrayList.get(position).get("store_name").toString());
                Store.setLat(Double.parseDouble((arrayList.get(position).get("store_lat")).toString()));
                Store.setLng(Double.parseDouble((arrayList.get(position).get("store_lng")).toString()));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title;
        TextView time;
        CardView cardView;

        public CustomViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.store_photo);
            title = view.findViewById(R.id.store_name);
            time = view.findViewById(R.id.store_time);
            cardView = view.findViewById(R.id.cardView);


        }

    }


}
