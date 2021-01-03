package com.example.pojangapp.mypage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.account.Account;
import com.example.pojangapp.R;
import com.example.pojangapp.account.LoginActivity;
import com.example.pojangapp.main.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MypageInfoFragment extends Fragment implements View.OnClickListener {

    TextView textId;
    TextView textEmail;
    TextView textBirth;
    TextView textPhone;
    TextView textDib;

    String myJSON;
    String user_id;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "user_id";
    private static final String TAG_EMAIL = "user_email";
    private static final String TAG_BIRTH = "user_birth";
    private static final String TAG_PHONE = "user_phone";
    private static final String TAG_DIBCOUNT = "dib_count";

    JSONArray users = null;
    ArrayList<HashMap<String, String>> mypageinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_info, container, false);

        user_id = Account.getUser_id();

        mypageinfo = new ArrayList<HashMap<String, String>>();
        getData("http://118.67.131.210/php/PHP_mypage_info.php");

        textId = viewGroup.findViewById(R.id.textId);
        textEmail = viewGroup.findViewById(R.id.textEmail);
        textBirth = viewGroup.findViewById(R.id.textBirth);
        textPhone = viewGroup.findViewById(R.id.textPhone);
        textDib = viewGroup.findViewById(R.id.textDib);



        //밑줄 설정
        SpannableString content = new SpannableString(textDib.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(),0);
        textDib.setText(content);

        textDib.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        intent.putExtra("titleName","찜한매장");
        intent.putExtra("pageIndex",0);
        startActivity(intent);
    }

    private void getData(String url) {
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

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            users = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < users.length(); i++) {
                JSONObject c = users.getJSONObject(i);
                String id = c.getString(TAG_ID);
                if (id.equals(user_id)) {
                    String email = c.getString(TAG_EMAIL);
                    String birth = c.getString(TAG_BIRTH);
                    String phone = c.getString(TAG_PHONE);
                    String dibcount = c.getString(TAG_DIBCOUNT);

                    HashMap<String, String> userlist = new HashMap<String, String>();

                    userlist.put(TAG_EMAIL, email);
                    userlist.put(TAG_BIRTH, birth);
                    userlist.put(TAG_PHONE, phone);
                    userlist.put(TAG_DIBCOUNT, dibcount);

                    mypageinfo.add(userlist);

                    setTextMypageInfo(mypageinfo, 0);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTextMypageInfo(ArrayList<HashMap<String, String>> arrayList, int position){

        String pn = "0" + arrayList.get(position).get(TAG_PHONE);
        String phonenum = pn.substring(0,3) + "-"
                + pn.substring(3,7) + "-"
                + pn.substring(7,11);

        textId.setText(user_id);
        textEmail.setText(arrayList.get(position).get(TAG_EMAIL));
        textBirth.setText(arrayList.get(position).get(TAG_BIRTH));
        textPhone.setText(phonenum);
        textDib.setText(arrayList.get(position).get(TAG_DIBCOUNT) + " 개");


    }
}