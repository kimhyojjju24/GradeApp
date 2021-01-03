package com.example.pojangapp.storedetail.create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.Store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class StoreCreateReviewFragment extends Fragment {

    TextView textStoreName, textMenuName, textByte;
    EditText editDetail;
    Button buttonSave;
    RatingBar ratingBar;

    OnBackPressedCallback callback;

    String postData, store_name, buyMenu, buyNum;

    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_store_create_review, container, false);

        textStoreName = viewGroup.findViewById(R.id.textStoreName);
        textMenuName = viewGroup.findViewById(R.id.textMenuName);
        textByte = viewGroup.findViewById(R.id.textByte);
        editDetail = viewGroup.findViewById(R.id.editDetail);
        buttonSave = viewGroup.findViewById(R.id.buttonSave);
        ratingBar = viewGroup.findViewById(R.id.ratingBar);

        user_id = Account.getUser_id();
        store_name = Store.getStoreName();
        buyNum = BuyReview.getBuyNum();
        buyMenu = BuyReview.getBuyMenu();

        textStoreName.setText(store_name);
        textMenuName.setText(buyMenu);
        editDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editDetail.isFocusable()){
                    try {
                        byte[] bytetext = editDetail.getText().toString().getBytes("KSC5601");
                        textByte.setText(String.valueOf(bytetext.length));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String after_text = s.toString();
                try {
                    byte[] getbyte = after_text.getBytes("KSC5601");
                    if (getbyte.length > 100){
                        s.delete(s.length()-2, s.length()-1);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detail = editDetail.getText().toString();
                float star = ratingBar.getRating();
                String numbering = buyNum.substring(buyNum.length()-2, buyNum.length());

                if (star == 0){
                    Toast.makeText(getContext(), "최소 별점은 0.5점 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    postData = "Data1="+ user_id + "r" + numbering +"&Data2="+ buyNum +"&Data3="+ star +"&Data4="+ detail + "&Data5=" + 1;
                    setData("http://118.67.131.210/php/insert/insertuser_myreview.php", postData);
                }

            }
        });

        return viewGroup;
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
                    Toast.makeText(getContext(),"처리 성공",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"처리 실패",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (editDetail.getText().length() > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("알림");
                    builder.setMessage("이전 화면으로 돌아가면 작성 중 이던 리뷰는 삭제됩니다.\n뒤로 가겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }else
                    getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}