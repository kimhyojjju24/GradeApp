package com.example.pojangapp.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojangapp.R;
import com.example.pojangapp.main.MainActivity;

public class LoginActivity extends AppCompatActivity {


    private Button parsingBtn;
    private EditText et_id;
    private EditText et_pw;
    private CheckBox id_save;
    private CheckBox auto_save;

    private Context mContext;

    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        parsingBtn = (Button) findViewById(R.id.login);
        et_id = (EditText) findViewById(R.id.editId);
        et_pw = (EditText) findViewById(R.id.editPassword);
        id_save = (CheckBox) findViewById(R.id.id_save);
        auto_save = (CheckBox) findViewById(R.id.auto_login);
        signup = findViewById(R.id.textSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        boolean boo = PreferenceManager.getBoolean(mContext, "check");
        if (boo) {
            et_id.setText(PreferenceManager.getString(mContext, "id"));
            et_pw.setText(PreferenceManager.getString(mContext, "pw"));
            id_save.setChecked(true);
        }


        parsingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreferenceManager.setString(mContext, "id", et_id.getText().toString());
                PreferenceManager.setString(mContext, "pw", et_pw.getText().toString());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String checkId = PreferenceManager.getString(mContext, "id");
                String checkPw = PreferenceManager.getString(mContext, "pw");
                if (TextUtils.isEmpty(checkId) || TextUtils.isEmpty(checkPw)) {
                    Toast.makeText(LoginActivity.this, "아이디/패스워드 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Account.setUser_id(checkId);
                    startActivity(intent);
                }
            }
        });
        id_save.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    PreferenceManager.setString(mContext, "id", et_id.getText().toString());
                    PreferenceManager.setBoolean(mContext, "check", id_save.isChecked());

                }else {
                    PreferenceManager.setBoolean(mContext, "check", id_save.isChecked());
                    PreferenceManager.clear(mContext);
                }

            }
        });

        auto_save.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    PreferenceManager.setString(mContext, "id", et_id.getText().toString());
                    PreferenceManager.setString(mContext, "pw", et_pw.getText().toString());
                    PreferenceManager.setBoolean(mContext, "check", id_save.isChecked());

                }else {
                    PreferenceManager.setBoolean(mContext, "check", id_save.isChecked());
                    PreferenceManager.clear(mContext);
                }

            }
        });
    }

}
