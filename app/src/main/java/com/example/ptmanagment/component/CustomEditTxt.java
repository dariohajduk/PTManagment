package com.example.ptmanagment.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class CustomEditTxt extends androidx.appcompat.widget.AppCompatEditText {


    public CustomEditTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
   }

}