package com.example.pojangapp.storedetail;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.pay.ElegantNumberButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreDetailMenuAdapter extends RecyclerView.Adapter<StoreDetailMenuAdapter.MyViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;

    String user_id, store_num, postData;

    public StoreDetailMenuAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_menu_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        for (int i = 0; i < arrayList.size(); i++){
            int isSoldout = Integer.parseInt(arrayList.get(i).get("menu_isSoldout").toString());
            int isMain = Integer.parseInt(arrayList.get(i).get("menu_isMain").toString());
            int notZero = Integer.parseInt(arrayList.get(i).get("notZero").toString());

            if (holder.getLayoutPosition() == i){
                holder.textMenuName.setText(arrayList.get(i).get(string[0]).toString());
                holder.textMenuPrice.setText(arrayList.get(i).get(string[1]).toString());
                holder.numberButton.setRange(1,20);

                if (isSoldout == 1 | notZero == 0){
                    SpannableString spannableString = (SpannableString) holder.textMenuPrice.getText();
                    spannableString.setSpan(new ForegroundColorSpan(Color.argb(80,0,0,0)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StrikethroughSpan(),0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (isMain == 1){
                    String text = holder.textMenuName.getText() + "   대표 ";
                    holder.textMenuName.setText(text);
                    SpannableString spannableString = (SpannableString) holder.textMenuName.getText();
                    int start = spannableString.length()-4;
                    int end = spannableString.length();
                    spannableString.setSpan(new BackgroundColorSpan(Color.argb(255, 255, 170, 95)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                holder.buttonCartAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        store_num = arrayList.get(position).get("store_num").toString();
                        user_id = arrayList.get(position).get("user_id").toString();
                        String num = arrayList.get(position).get("menu_num").toString();
                        String cnt = holder.numberButton.getNumber();

                        if (isSoldout == 1 | notZero == 0){
                            Toast.makeText(fragment, "품절 메뉴로 장바구니에 추가 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            postData = "Data2=" + user_id + "&Data3=" + num + "&Data4=" + cnt + "&store_num=" + store_num +"&toggle=";
                            setData("http://118.67.131.210/php/insert/insertuser_store_cart_menu.php", postData);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textMenuName;
        TextView textMenuPrice;
        ImageButton buttonCartAdd;
        ElegantNumberButton numberButton;

        public MyViewHolder(View view){
            super(view);
            textMenuName = (TextView) view.findViewById(R.id.textMenuName);
            textMenuPrice = (TextView) view.findViewById(R.id.textMenuPrice);
            buttonCartAdd = (ImageButton) view.findViewById(R.id.buttonCartAdd);
            numberButton = (ElegantNumberButton) view.findViewById(R.id.number_button);
        }
    }


    public void setData(String url, String postData) {
        class SetData extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(postData.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    String result = readStream(conn.getInputStream());
                    conn.disconnect();

                    return result;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String result = s;
                if(result.equals("1")){
//                    Toast.makeText(fragment,"처리 성공",Toast.LENGTH_SHORT).show();
                }
                else{
//                    Toast.makeText(fragment,s,Toast.LENGTH_SHORT).show();
                    show();
                }
            }

            private String readStream(InputStream in) throws IOException {
                StringBuilder jsonHtml = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = null;

                while ((line = reader.readLine()) != null)
                    jsonHtml.append(line);

                reader.close();
                return jsonHtml.toString();
            }
        }
        SetData s = new SetData();
        s.execute(url, postData);
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment);
        builder.setTitle("알림");
        builder.setMessage("다른 매장의 메뉴는 장바구니에 함께 담을 수 없습니다.\n장바구니를 비우겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        postData += "ok";
                        setData("http://118.67.131.210/php/insert/insertuser_store_cart_menu.php", postData);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}