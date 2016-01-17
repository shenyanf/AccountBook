package com.shanshan.myaccountbook.button;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanshan.myaccountbook.R;

public class AddButton extends RelativeLayout {

    private ImageView imgView;
    private TextView textView;

    public AddButton(Context context) {
        super(context, null);
    }

    public AddButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.add_button, this, true);

        this.imgView = (ImageView) findViewById(R.id.imgview);
        this.textView = (TextView) findViewById(R.id.textview);


        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(int size) {
        this.textView.setTextSize(size, TypedValue.COMPLEX_UNIT_SP);
    }

}