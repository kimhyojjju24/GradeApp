package com.example.pojangapp.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.pojangapp.R;
import java.util.ArrayList;


public class SignupActivity extends AppCompatActivity{

    Spinner spinnerYear, spinnerMonth, spinnerDay;
    String year, month, day, imgPath="";
    TextView textViewlogin;
    Button submit;
    Button buttonPhoto;
    TextView textPhoto;


    private final int GET_GALLERY_IMAGE = 200;

    ArrayList<String> arr = new ArrayList<>();

    EditText data1, data2, data3, data4, data5, data6, data7, data8;
    SimpleMultiPartRequest smpr;
//    String imageUrl;

//    byte [] bytes;
//    String image;
//    String temp, imgPath;
    //Base64.encodeToString(arr, Base64.DEFAULT);
    //temp = URLEncoder.encode(image,"utf-8");
    //imageUrl = temp;

//    String postData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerDay = findViewById(R.id.spinnerDay);
        textViewlogin = findViewById(R.id.textLogin);
        submit = findViewById(R.id.buttonSubmit);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        textPhoto = findViewById(R.id.textPhoto);

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(permissionResult== PackageManager.PERMISSION_DENIED){
                        String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,10);
                    }
                }

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        ArrayAdapter<?> arrayAdapterYear = ArrayAdapter.createFromResource(
                this, R.array.year, R.layout.spinner_item);
        spinnerYear.setAdapter(arrayAdapterYear);

        data1 = findViewById(R.id.editId);
        data2 = findViewById(R.id.editPassword);
        data3 = findViewById(R.id.editEmail);
        data4 = findViewById(R.id.editName);
        data5 = findViewById(R.id.editNickName);
        data6 = findViewById(R.id.editPhone1);
        data7 = findViewById(R.id.editPhone2);
        data8 = findViewById(R.id.editPhone3);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<?> arrayAdapterMonth = ArrayAdapter.createFromResource(
                this, R.array.month, R.layout.spinner_item);
        spinnerMonth.setAdapter(arrayAdapterMonth);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<?> arrayAdapterDay = ArrayAdapter.createFromResource(
                this, R.array.day, R.layout.spinner_item);
        spinnerDay.setAdapter(arrayAdapterDay);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrl="http://118.67.131.210/php/insert/insertuser_home_signin.php";

                smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            startActivity(intent);
                        }else {
                            new AlertDialog.Builder(SignupActivity.this).setMessage("실패:"+response).create().show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                String d1 = data1.getText().toString();
                String d2 = data2.getText().toString();
                String d3 = data3.getText().toString();
                String d4 = data4.getText().toString();
                String d5 = data5.getText().toString();
                String d6 = data6.getText().toString();
                String d7 = data7.getText().toString();
                String d8 = data8.getText().toString();

                smpr.addStringParam("Data0", d1);
                smpr.addStringParam("Data1", d2);
                smpr.addStringParam("Data2", d3);
                smpr.addStringParam("Data3", d4);
                smpr.addStringParam("Data4", d5);
                smpr.addStringParam("Data5", year+"-"+month+"-"+day);
                smpr.addStringParam("Data6", d6 + d7 + d8);
                if (!(imgPath.equals(""))){
                    smpr.addFile("Data7", imgPath);
                }

                RequestQueue requestQueue= Volley.newRequestQueue(v.getContext());
                requestQueue.add(smpr);
                requestQueue.start();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GET_GALLERY_IMAGE :
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    textPhoto.setText("");
                    buttonPhoto.setText("사진 선택 완료");
                    buttonPhoto.setClickable(false);
                    Uri uri= data.getData();
                    imgPath= getPathFromURI(this,uri);
                }
                break;
        }

    }

    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

