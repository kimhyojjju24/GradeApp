package com.example.pojangapp.emoney;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;
import com.example.pojangapp.pay.BuyList;
import com.example.pojangapp.pay.PayList;

import java.util.ArrayList;

import kr.co.bootpay.model.Item;


public class EmoneyChargeFragment extends Fragment implements View.OnClickListener {

    String user_id;
    ArrayList<Item> buyLists = new ArrayList<>();

    Button button1000;
    Button button2000;
    Button button3000;
    Button button4000;
    Button button5000;
    Button button10000;
    Button button20000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_emoney_charge, container, false);

        user_id = Account.getUser_id();


        button1000 = viewGroup.findViewById(R.id.bt1000);
        button2000 = viewGroup.findViewById(R.id.bt2000);
        button3000 = viewGroup.findViewById(R.id.bt3000);
        button4000 = viewGroup.findViewById(R.id.bt4000);
        button5000 = viewGroup.findViewById(R.id.bt5000);
        button10000 = viewGroup.findViewById(R.id.bt10000);
        button20000 = viewGroup.findViewById(R.id.bt20000);


        button1000.setOnClickListener(this);
        button2000.setOnClickListener(this);
        button3000.setOnClickListener(this);
        button4000.setOnClickListener(this);
        button5000.setOnClickListener(this);
        button10000.setOnClickListener(this);
        button10000.setOnClickListener(this);


        return viewGroup;
    }


    @Override
    public void onClick(View v) {
        Button selected = (Button) v;
        String pr = selected.getText().toString();
        int len = pr.length();

        double price = Double.parseDouble(pr.substring(0,len-1));

        buyLists.add(new Item("포차머니",1,"e-money"+price, price, "포차머니", price + "원 권", "0"));

        Intent intent = new Intent(getContext(), MenuActivity.class);
        PayList.setMainTitle("포차머니");
        PayList.setSubTitle(price + "원 권");
        PayList.setTotalPrice(price);
        PayList.setMenuList(buyLists);
        intent.putExtra("titleName", "결제 화면");
        startActivity(intent);
    }
}
