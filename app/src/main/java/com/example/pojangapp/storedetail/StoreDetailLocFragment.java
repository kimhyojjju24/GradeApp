package com.example.pojangapp.storedetail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pojangapp.R;
import com.example.pojangapp.main.MenuActivity;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.bus.BusItem;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;

public class StoreDetailLocFragment extends Fragment {

    TextView textLoc, textLocDetail, textBusStop, textBusNotice;
//    MapPoint mapPoint;
    LinearLayout location;

    BusStopAdapter busStopAdapter;
    LinearLayoutManager layoutManager;

    RecyclerView recyclerBus;

    String store_num;
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_LOC = "store_loc";
    private static final String TAG_LOC_DETAIL = "store_loc_detail";

    JSONArray detailLoc = null;
    ArrayList<HashMap<String, String>> storeDetailLoc;

//    ViewGroup mapContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_store_detail_loc, container, false);

        textLoc = view.findViewById(R.id.textLoc);
        textLocDetail = view.findViewById(R.id.textLocDetail);
        store_num = Store.getStoreNum();
        recyclerBus = view.findViewById(R.id.recyclerBus);
        textBusStop = view.findViewById(R.id.textBusStop);
        textBusNotice = view.findViewById(R.id.textBusNotice);
        textBusNotice.setVisibility(View.GONE);
        location = view.findViewById(R.id.textLocation);
//        MapView mapView = new MapView(getActivity());
//        mapContainer = (ViewGroup) view.findViewById(R.id.map_view);
//        mapContainer.addView(mapView);

        layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        storeDetailLoc = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_storedetail_loc.php");

        recyclerBus.setLayoutManager(layoutManager);
        recyclerBus.setHasFixedSize(true);
        double lat = Store.getLat();
        double lng = Store.getLng();

        textLocDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MenuActivity.class);
                intent.putExtra("titleName","매장 상세 위치");
                startActivity(intent);
            }
        });
//        mapPoint = mapPointWithGeoCoord(lat, lng);
//        mapView.setMapCenterPoint(mapPoint, false);
//        mapView.addPOIItem(setLocationMarker(Store.getStoreName(), 1, mapPoint));

//        String baseUrl = "http://ws.bus.go.kr";
        String key = "JOGyiKgtiO/P5OSXCEqcYa2maMUHAIgQBhpG0RZIswcCLQzvDcjRKAk63FaLboAzgdP1x7aom6I5PQNATA0amA==";
//        String radius = "1000";
        ArrayList<BusItem> arrayList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DocumentBuilderFactory builderFactoryBusStop = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builderBusStop = builderFactoryBusStop.newDocumentBuilder();
                    Document busStopDoc = builderBusStop.parse("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?serviceKey=" +key+ "&tmX=" +lng+ "&tmY=" +lat+ "&radius=150");

                    busStopDoc.getDocumentElement().normalize();
                    NodeList busStopNodeList = busStopDoc.getElementsByTagName("itemList");

                    Node busStopNode;
                    Element busStopEmt;
                    ArrayList<String> busNameList = new ArrayList<>();
                    ArrayList<String> busNumList = new ArrayList<>();
                    String nowBusStopName = null;
                    int j = 0;
                    if (busStopNodeList.getLength() > 0){
                        for (int i=0; i < busStopNodeList.getLength(); i++){
                            busStopNode = busStopNodeList.item(i);
                            busStopEmt = (Element) busStopNode;
                            String busStopName = getTagValue("stationNm", busStopEmt);
                            String busStopId = getTagValue("arsId", busStopEmt);
                            busNameList.add(busStopName);
                            busNumList.add(busStopId);
                        }
                        while(true){
                            DocumentBuilderFactory builderFactoryBus = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builderBus = builderFactoryBus.newDocumentBuilder();
                            Document busDoc = builderBus.parse("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=" +key+ "&arsId=" + busNumList.get(j));
                            busDoc.getDocumentElement().normalize();
                            NodeList busNodeList = busDoc.getElementsByTagName("itemList");
                            int busNodeListLength = busNodeList.getLength();
                            for (int k = 0; k < busNodeListLength; k++){
                                Node busNode = busNodeList.item(k);
                                Element busEmt = (Element) busNode;
                                String busName = getTagValue("rtNm", busEmt);
                                String busStopName = busNameList.get(j);
                                String busStopNum = busNumList.get(j);
                                BusItem busItem = new BusItem(busStopName, busStopNum, busName);
                                arrayList.add(busItem);
                            }
                            j++;
                            if (j >= busNumList.size()){
                                break;
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                busStopAdapter = new BusStopAdapter(arrayList);
                                recyclerBus.setAdapter(busStopAdapter);
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textBusNotice.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            detailLoc = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < detailLoc.length(); i++){
                JSONObject c = detailLoc.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                if (num.equals(store_num)) {
                    String loc = c.getString(TAG_LOC);
                    String loc_detail = c.getString(TAG_LOC_DETAIL);

                    HashMap<String, String> detailLoclist = new HashMap<String, String>();

                    detailLoclist.put(TAG_NUM, num);
                    detailLoclist.put(TAG_LOC, loc);
                    detailLoclist.put(TAG_LOC_DETAIL, loc_detail);

                    storeDetailLoc.add(detailLoclist);

                    setTextStoreDetail(storeDetailLoc, 0);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MapPOIItem setLocationMarker(String name, int position, MapPoint mapPoint){
        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName(name);
        poiItem.setTag(position);
        poiItem.setMapPoint(mapPoint);
        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomImageResourceId(R.drawable.marker);
        poiItem.setCustomImageAutoscale(true);
        poiItem.setCustomImageAnchor(0.5f, 1.0f);
        return poiItem;
    }

    private void setTextStoreDetail(ArrayList<HashMap<String, String>> arrayList, int position) {
        textLoc.setText(arrayList.get(position).get(TAG_LOC));
        textLocDetail.setText(arrayList.get(position).get(TAG_LOC_DETAIL));
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
