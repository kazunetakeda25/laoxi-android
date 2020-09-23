package com.rider.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.rider.R;


/**
 * Created by prakash on 8/6/15.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       setFont(attrs);
    }


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(attrs);

    }


    public CustomEditText(Context context) {
        super(context);

    }

    private void setFont(AttributeSet attrs)
    {

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
