package com.driver.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.driver.R;


/**
 * Created by darshan on 8/6/15.
 */
public class CustomButton extends Button {

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(attrs);
    }


    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(attrs);
    }


    public CustomButton(Context context) {
        super(context);

    }

    private void setFont(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontStyle);
            try {


                String fontName = a.getString(R.styleable.FontStyle_fontName);
                boolean boldornot = a.getBoolean(R.styleable.FontStyle_boldornot, false);


                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                    this.setTypeface(myTypeface);

                }

            } finally {
                a.recycle();
            }

        }

    }

}
