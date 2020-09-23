package com.github.siyamed.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;

/**
 * Created by hlink44 on 28/6/16.
 */
public class ImageViewPastRide extends ShaderImageView {

    public ImageViewPastRide(Context context) {
        super(context);
    }

    public ImageViewPastRide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewPastRide(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.img_past_ride_bg);
    }
}
