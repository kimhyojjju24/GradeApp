package com.example.pojangapp.mypage;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pojangapp.R;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.StoreDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MypageReviewAdapter extends RecyclerView.Adapter<MypageReviewAdapter.CustomViewHolder> {

    Context fragment;
    ArrayList<? extends HashMap<String, ?>> arrayList;
    int layout;
    String[] string;
    int[] ints;

    public MypageReviewAdapter(Context context, ArrayList<? extends HashMap<String, ?>> data, int resource, String[] from, int[] to){
        this.fragment = context;
        this.arrayList = data;
        this.layout = resource;
        this.string = from;
        this.ints = to;
    }


    @NonNull
    @Override
    public MypageReviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MypageReviewAdapter.CustomViewHolder holder, final int position) {
        for (int i = 0; i < arrayList.size(); i++){
            String store_name = arrayList.get(i).get("store_name").toString();
            int cnt = Integer.parseInt(arrayList.get(i).get(string[3]).toString())-1;
            if (holder.getLayoutPosition() == i){

                holder.textTitle.setText(store_name);
                holder.textTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(fragment, StoreDetailActivity.class);
                        Store.setStoreNum(arrayList.get(position).get(string[0]).toString());
                        v.getContext().startActivity(intent);
                    }
                });

                if (cnt > 0){
                    holder.textBuy.setText(arrayList.get(i).get(string[2]).toString() + " 외 " + cnt + "개 구입");
                } else
                    holder.textBuy.setText(arrayList.get(i).get(string[2]).toString() + " 구입");

                float rat = Float.valueOf(arrayList.get(i).get(string[1]).toString());
                holder.ratingBar.setRating(rat);

                holder.textContents.setText(arrayList.get(i).get(string[5]).toString());

                holder.textContents.post(new Runnable() {
                    @Override
                    public void run() {
                        int line = holder.textContents.getLineCount();

                        if (line > 2){
                            makeTextViewResizable(holder.textContents, 2, "더보기",true);
                        }
                    }
                });

                String date = arrayList.get(i).get(string[4]).toString();
                holder.textDate.setText(date.substring(2, 10));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        TextView textBuy;
        RatingBar ratingBar;
        TextView textContents;
        LinearLayout imageLayout;
        TextView textDate;

        public CustomViewHolder(View view) {
            super(view);

            textTitle = view.findViewById(R.id.textReviewTitle);
            textBuy = view.findViewById(R.id.textReviewBuy);
            ratingBar = view.findViewById(R.id.ratingBar);
            textContents = view.findViewById(R.id.textContents);
            imageLayout = view.findViewById(R.id.reviewLayout);
            textDate = view.findViewById(R.id.textReviewDate);

        }

    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, " 줄이기 >", false);
                    } else {
                        makeTextViewResizable(tv, 2, " 더보기 >", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


}



