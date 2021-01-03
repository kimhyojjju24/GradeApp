package com.example.pojangapp.emoney.timerecycler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;

import java.util.ArrayList;
import java.util.Calendar;


public class TimeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AdapterItem> itemList;
//    private ArrayList<MyData> itemList;

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView timeItemView;

        public TimeViewHolder(View v) {
            super(v);
            timeItemView = (TextView) v.findViewById(R.id.timeItemView);
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView textDetail, textPrice, textInOut, textCurrentPrice;

        public DataViewHolder(View v) {
            super(v);
            textDetail = (TextView) v.findViewById(R.id.textDetail);
            textPrice = (TextView) v.findViewById(R.id.textPrice);
            textInOut = v.findViewById(R.id.textInOut);
            textCurrentPrice = v.findViewById(R.id.textCurrentPrice);
        }
    }

    public TimeRecyclerAdapter(ArrayList<MyData> dataset) {
        itemList = initItemList(orderByTimeDesc(dataset));
    }


    private ArrayList<AdapterItem> initItemList(ArrayList<MyData> dataset) {
        ArrayList<AdapterItem> result = new ArrayList<>();

        int year = 0, month = 0, dayOfMonth = 0;
        for(MyData data:dataset) {
            if(year != data.getYear() || month != data.getMonth() || dayOfMonth != data.getDayOfMonth()) {
                year = data.getYear();
                month = data.getMonth();
                dayOfMonth = data.getDayOfMonth();
                result.add(new TimeItem(year, month, dayOfMonth));
            }
            result.add(data);
        }
        return result;
    }

    private ArrayList<MyData> orderByTimeDesc(ArrayList<MyData> dataset) {
        ArrayList<MyData> result = dataset;
        for(int i=0; i<result.size()-1; i++) {
            for(int j=0; j<result.size()-i-1; j++) {
                if(result.get(j).getTime() < result.get(j+1).getTime()) {
                    MyData temp2 = result.remove(j+1);
                    MyData temp1 = result.remove(j);
                    result.add(j, temp2);
                    result.add(j+1, temp1);
                }
            }
        }
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == AdapterItem.TYPE_TIME)
            return new TimeViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_item_time, parent, false));
        else
            return new DataViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_item_data, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TimeViewHolder) {
            TimeViewHolder tHolder = (TimeViewHolder) holder;
            tHolder.timeItemView.setText(itemList.get(position).getTimeToString());
        } else {
            DataViewHolder dHolder = (DataViewHolder) holder;
            dHolder.textDetail.setText(itemList.get(position).getDetail());
            dHolder.textCurrentPrice.setText("잔액 " + itemList.get(position).getCurrentPrice() + "원");
            dHolder.textPrice.setText(itemList.get(position).getPrice() + "원");

            if (dHolder.textDetail.getText().equals("")){
                dHolder.textDetail.setText("포차머니 충전");
            } else {
                dHolder.textDetail.setText(itemList.get(position).getDetail());
            }

            if (itemList.get(position).getIsInOut() == 0){
                dHolder.textInOut.setText("사용");
                dHolder.textInOut.setTextColor(0xFFFA7468);
            }else{
                dHolder.textInOut.setText("충전");
                dHolder.textInOut.setTextColor(0xFF80C1DF);

            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public MyData getItem(int position) {
        return (MyData)itemList.get(position);
    }

}