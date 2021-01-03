package com.example.pojangapp.data;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataSetClass {
    public static String result="";
    public static void setData(String url, String postData) {
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
                result = s;
                System.out.println(result);
//                if(result.equals("1")){
//                    Toast.makeText(getApplicationContext(),"처리 성공",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"처리 실패",Toast.LENGTH_SHORT).show();
//                }
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
