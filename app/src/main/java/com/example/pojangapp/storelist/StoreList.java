package com.example.pojangapp.storelist;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class StoreList {

    CardView card;
    ImageView img;
    TextView title;
    TextView add;

    public StoreList(){

    }

    public StoreList(CardView card, ImageView img, TextView title, TextView add) {
        this.card = card;
        this.img = img;
        this.title = title;
        this.add = add;
    }

    public CardView getCard() {
        return card;
    }

    public void setCard(CardView card) {
        this.card = card;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getAdd() {
        return add;
    }

    public void setAdd(TextView add) {
        this.add = add;
    }
}
