package com.github.siyamed.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;

/**
 * Created by hlink44 on 28/6/16.
 */
public class ImageViewProfile extends ShaderImageView {

    public ImageViewProfile(Context context) {
        super(context);
    }

    public ImageViewProfile(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewProfile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.img_profile_bg);
    }
}
