package com.rider.navigator;

import android.view.View;

/**
 * Created by hitesh on 28/4/16.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}

