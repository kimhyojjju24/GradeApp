package com.example.pojangapp.storedetail.create;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.account.LoginActivity;
import com.example.pojangapp.main.Store;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class StoreCreateComplainFragment extends Fragment {

    Spinner spinner;
    EditText editDetail;
    TextView textStoreName, textByte;
    Button buttonSave;
    String postData, store_num, store_name;

    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_store_create_complain, container, false);

        user_id = Account.getUser_id();
        store_num = Store.getStoreNum();
        store_name = Store.getStoreName();

        spinner = viewGroup.findViewById(R.id.spinner);
        editDetail = viewGroup.findViewById(R.id.editDetail);
        textStoreName = viewGroup.findViewById(R.id.textStoreName);
        buttonSave = viewGroup.findViewById(R.id.buttonSave);
        textByte = viewGroup.findViewById(R.id.textByte);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.complainArray, R.layout.spinner_item);
        spinner.setAdapter(adapter);

        textStoreName.setText(store_name);

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
                String spText = spinner.getSelectedItem().toString();
                int spNum = spinner.getSelectedItemPosition();
                String edText = editDetail.getText().toString();

                postData = "Data1=" + user_id + "c" + store_num + "&Data2=" + user_id + "&Data3=" + store_num + "&Data4=" + spNum + "&Data5=" + edText ;

                if (spText.equals("기타") && edText.equals("")){
                    Toast.makeText(getContext(), "상세 신고 내용을 적어주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    setData("http://118.67.131.210/php/insert/insertuser_mycomplain.php", postData);
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

}