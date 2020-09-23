package com.driver.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.driver.R;


/**
 * Created by darshan on 8/6/15.
 */
public class CustomCheckBox extends CheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(attrs);
    }


    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(attrs);
    }


    public CustomCheckBox(Context context) {
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
