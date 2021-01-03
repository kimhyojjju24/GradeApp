package com.example.pojangapp.storelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pojangapp.R;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.StoreDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;


    private Intent intent;


    public StoreListAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }


    @NonNull
    @Override
    public StoreListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StoreListAdapter.CustomViewHolder holder, int position) {
        int open = Integer.parseInt(arrayList.get(position).get("store_isOpen").toString());
        holder.title.setText(arrayList.get(position).get(string[0]).toString());
        holder.add.setText(arrayList.get(position).get(string[1]).toString());
        holder.time.setText(arrayList.get(position).get(string[2]).toString());

        String imageUrl = arrayList.get(position).get("store_photo").toString();
        Glide.with(fragment).load(imageUrl).error(R.drawable.img).fallback(R.drawable.img).into(holder.img);

        if (open == 0){
            holder.img.setAlpha(0.7f);
            holder.title.setTextColor(R.color.colorGray);
            holder.add.setTextColor(R.color.colorGray);
            holder.time.setText("영업 종료");
            holder.time.setTextColor(R.color.colorGray);
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
        TextView add;
        TextView time;
        CardView cardView;


        public CustomViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.storeImg);
            title = view.findViewById(R.id.store_name);
            add = view.findViewById(R.id.store_loc);
            time = view.findViewById(R.id.store_time);

            cardView = view.findViewById(R.id.cardView);
        }
    }


}
