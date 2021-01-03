package com.example.pojangapp.mypage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.StoreDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StoreDibAdapter extends RecyclerView.Adapter<StoreDibAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;

    Intent intent;

    public StoreDibAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_item, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        for (int i = 0; i < arrayList.size(); i++){
            int open = Integer.parseInt(arrayList.get(i).get("store_isOpen").toString());
            if (holder.getLayoutPosition() == i){
                holder.img.setImageResource(R.drawable.img);
                holder.title.setText(arrayList.get(i).get(string[0]).toString());
                holder.add.setText(arrayList.get(i).get(string[1]).toString());
                holder.time.setText(arrayList.get(i).get(string[2]).toString());

                if (open == 0){
                    holder.img.setAlpha(0.7f);
                    holder.title.setTextColor(R.color.colorGray);
                    holder.add.setTextColor(R.color.colorGray);
                    holder.time.setText("영업 종료");
                    holder.time.setTextColor(R.color.colorGray);
                }
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), StoreDetailActivity.class);
                Store.setStoreNum(arrayList.get(position).get("store_num").toString());
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
