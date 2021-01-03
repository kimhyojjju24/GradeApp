package com.example.pojangapp.main.map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pojangapp.R;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.search.SearchActivity;
import com.example.pojangapp.storedetail.StoreDetailActivity;
import com.example.pojangapp.storelist.StoreListAdapter;

import net.daum.android.map.coord.MapCoord;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;


public class MapFragment extends Fragment {

    MapView mapView;

    String myJSON;
    RecyclerView recyclerView;
//    LinearLayoutManager layoutManager;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUM = "store_num";
    private static final String TAG_NAME = "store_name";
    private static final String TAG_ADD = "store_loc";
    private static final String TAG_OT = "store_opentime";
    private static final String TAG_CT = "store_closetime";
    private static final String TAG_TIME = "store_time";
    private static final String TAG_ISOPEN = "store_isOpen";
    private static final String TAG_LAT = "store_lat";
    private static final String TAG_LNG = "store_lng";
    private static final String TAG_IMG = "store_photo";

    JSONArray stores = null;
    ArrayList<HashMap<String, String>> storelist;
    ViewGroup mapContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        mapContainer = (ViewGroup) view.findViewById(R.id.map_view);
//        recyclerView = view.findViewById(R.id.recyclerViewMap);
        storelist = new ArrayList<HashMap<String, String>>();
//        layoutManager = new LinearLayoutManager(getContext());
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView = new MapView(getContext());
        mapContainer.addView(mapView);
        if (Store.getLat() != 0.0 || Store.getLng() != 0.0){
            MapPoint mapPoint = mapPointWithGeoCoord(Store.getLat(), Store.getLng());
            MapPOIItem poiItem = setLocationMarker(Store.getStoreName(),0, mapPoint);
            mapView.addPOIItem(poiItem);
            mapView.setMapCenterPoint(mapPoint,false);
            mapView.selectPOIItem(poiItem, false);
        }else {
            GetDataJSON getDataJSON = new GetDataJSON();
            getDataJSON.execute();

            mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
                @Override
                public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                }

                @Override
                public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

//                    int selectIdx = mapPOIItem.getTag();
//                    Store.setStoreNum(storelist.get(selectIdx).get("store_num"));
//                    Store.setStoreName(storelist.get(selectIdx).get("store_name"));
//                    Store.setLat(Double.parseDouble((storelist.get(selectIdx).get("store_lat"))));
//                    Store.setLng(Double.parseDouble((storelist.get(selectIdx).get("store_lng"))));
//
//                    Intent intent = new Intent(getContext(), StoreDetailActivity.class);
//                    startActivity(intent);
                }

                @Override
                public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

                }

                @Override
                public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

                }
            });
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


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            stores = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < stores.length(); i++) {
                JSONObject c = stores.getJSONObject(i);
                String num = c.getString(TAG_NUM);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);
                String opentime = c.getString(TAG_OT);
                String closetime = c.getString(TAG_CT);
                String time = opentime.substring(0, 5) + " ~ " + closetime.substring(0, 5);
                String isOpen = c.getString(TAG_ISOPEN);
                String lat = c.getString(TAG_LAT);
                String lng = c.getString(TAG_LNG);
                String img = c.getString(TAG_IMG);

                HashMap<String, String> stores = new HashMap<String, String>();

                stores.put(TAG_NUM, num);
                stores.put(TAG_NAME, name);
                stores.put(TAG_ADD, address);
                stores.put(TAG_TIME, time);
                stores.put(TAG_ISOPEN, isOpen);
                stores.put(TAG_LAT, lat);
                stores.put(TAG_LNG, lng);
                stores.put(TAG_IMG, img);

                storelist.add(stores);

                MapPoint mapPoint = mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng));
                mapView.addPOIItem(setLocationMarker(name, i, mapPoint));
            }

//            MapStoreListAdapter adapter = new MapStoreListAdapter(
//                    getContext(), storelist, R.layout.store_list_item,
//                    new String[]{ TAG_NAME, TAG_ADD, TAG_TIME},
//                    new int[]{R.id.store_name, R.id.store_loc, R.id.store_time}
//            );
//            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String link = "http://118.67.131.210/php/PHP_storelist.php";
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

    @Override
    public void onStop() {
        super.onStop();
        mapContainer.removeAllViews();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
