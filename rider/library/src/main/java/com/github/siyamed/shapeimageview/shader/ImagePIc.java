package com.github.siyamed.shapeimageview.shader;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.R;
import com.github.siyamed.shapeimageview.ShaderImageView;

/**
 * Created by hlink44 on 28/6/16.
 */
public class ImagePIc extends ShaderImageView {

    public ImagePIc(Context context) {
        super(context);
    }

    public ImagePIc(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePIc(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.img_profile_bg);
    }
}

