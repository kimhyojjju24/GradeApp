package com.example.pojangapp.storedetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.emoney.timerecycler.MyData;
import com.example.pojangapp.emoney.timerecycler.TimeItem;
import com.example.pojangapp.storedetail.bus.AdapterItem;
import com.example.pojangapp.storedetail.bus.BusItem;
import com.example.pojangapp.storedetail.bus.BusStopItem;

import java.util.ArrayList;

public class BusStopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AdapterItem> arrayList;

    public BusStopAdapter(ArrayList<BusItem> array) {
        arrayList = initItemList(castingBusItem(array));
    }

    private ArrayList<BusItem> castingBusItem(ArrayList<BusItem> busItems){
        ArrayList<BusItem> items = busItems;
        return items;
    }


    private ArrayList<AdapterItem> initItemList(ArrayList<BusItem> busItems) {
        ArrayList<AdapterItem> result = new ArrayList<>();
        String busStopName = "";
        String busStopNum = "";
        for(BusItem bus:busItems) {
            if(busStopName != bus.getBusStopName()) {
                busStopName = bus.getBusStopName();
                busStopNum = bus.getBusStopNum();
                result.add(new BusStopItem(busStopName, busStopNum));
            }
            result.add(bus);
        }
        return result;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AdapterItem.TYPE_BUS_STOP)
            return new BusStopHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.bus_stop_item, parent, false));
        else
            return new BusHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.bus_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BusStopHolder) {
            BusStopHolder stopHolder = (BusStopHolder) holder;
            stopHolder.textBusStop.setText(arrayList.get(position).getBusStopName());
        } else {
            BusHolder busHolder = (BusHolder) holder;
            busHolder.textBus.setText(arrayList.get(position).getBusName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class BusStopHolder extends RecyclerView.ViewHolder {
        public TextView textBusStop;
        public BusStopHolder(View itemView) {
            super(itemView);
            textBusStop = itemView.findViewById(R.id.textBusStop);
        }
    }
    public static class BusHolder extends RecyclerView.ViewHolder {
        public TextView textBus;
        public BusHolder(View itemView) {
            super(itemView);
            textBus=itemView.findViewById(R.id.textBus);
        }
    }
}
