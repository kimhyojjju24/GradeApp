package com.example.pojangapp.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pojangapp.data.DataSetClass;
import com.example.pojangapp.R;
import com.example.pojangapp.account.Account;
import com.example.pojangapp.main.MenuActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.model.BootUser;

public class PayLoaderFragment extends Fragment {

    int stuck = 10;
    Button buttonPay;
    private FragmentActivity myContext;
    TextView textPayLoader;

    ArrayList<HashMap<String, Integer>> arrayList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_pay_loader, container, false);

        buttonPay = view.findViewById(R.id.buttonPay);
        textPayLoader = view.findViewById(R.id.textPay);

        int time = (int) new Date().getTime();
        String buyNum = Account.getUser_id() + "p" + Math.abs(time);


        for (int i = 0; i < PayList.getMenuList().size(); i++){
            HashMap<String, Integer> hashMap = new HashMap<>();
            hashMap.put(PayList.getMenuList().get(i).getUnique(), PayList.getMenuList().get(i).getQty());

            arrayList.add(hashMap);
        }


        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BootUser bootUser = new BootUser().setPhone(Account.getUserPhone());

                Bootpay.init(myContext.getFragmentManager())
                        .setApplicationId("5f6176d24f74b40025e35c0b") // 해당 프로젝트(안드로이드)의 application id 값
                        .setPG(PG.INICIS) // 결제할 PG 사
                        .setMethod(Method.KAKAO) // 결제수단
                        .setContext(getActivity())
                        .setBootUser(bootUser)
                        .setUX(UX.PG_DIALOG)
                        .setName(PayList.getSubTitle()) // 결제할 상품명
                        .setOrderId(buyNum) // 결제 고유번호expire_month
                        .setPrice(PayList.getPayPrice()) // 결제할 금액
                        .setItems(PayList.getMenuList())
                        .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            @Override
                            public void onConfirm(@Nullable String message) {
                                if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                                else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                buttonPay.setText("결제 완료");
                                textPayLoader.setText("결제 완료 버튼을 눌러주세요.");
                                buttonPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), MenuActivity.class);
                                        intent.putExtra("titleName", "결제 완료");
                                        Pay.setBuyNum(buyNum);
                                        startActivity(intent);
                                    }
                                });
                            }
                        })
                        .onDone(new DoneListener() {
                            @Override
                            public void onDone(@Nullable String message) {
//                                Log.d("done", message);
                                // update
                                String postData = "Data1=" + buyNum + "&Data2=" + Account.getUser_id();
                                DataSetClass.setData("http://118.67.131.210/php/insert/insertuser_buy.php", postData);
                                for (int i = 0; i < arrayList.size(); i++){
                                    String menuData = "Data1=" + buyNum + "&Data2=" + arrayList.get(i).get(0) + "&Data3=" + arrayList.get(i).get(1);
                                    DataSetClass.setData("http://118.67.131.210/php/insert/insertuser_buy_menu.php", menuData);
                                }
                            }
                        })
                        .onError(new ErrorListener() {
                            @Override
                            public void onError(@Nullable String message) {
//                                Log.d("error", message);
                            }
                        })
                        .onClose(
                                new CloseListener() {
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", "close");
                                    }
                                })
                        .request();

            }
        });

        return view;
    }
}