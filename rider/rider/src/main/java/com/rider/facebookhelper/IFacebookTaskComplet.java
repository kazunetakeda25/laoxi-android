package com.rider.facebookhelper;

import android.support.annotation.Nullable;

/**
 * Created by Chirag on 02/03/2016.
 */
public abstract class IFacebookTaskComplet<T>{
    public abstract void onTaskComplete(boolean success,@Nullable T data);
}
