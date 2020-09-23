package com.rider.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataToPref {
	public static void setSharedPreferanceData(Context context,String PrefName, String key,String value) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
		try {
			Editor editor = sharedpreferences.edit();
			editor.putString(key, value);
			editor.commit();

		} catch (Exception e) {
			// TODO: handle exception		
			e.printStackTrace();
		}
	}

	public static String getSharedPreferanceData(Context context,String PrefName, String key) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
		String userObjectString = sharedpreferences.getString(key, "");
		return userObjectString;

	}
	
	public static void clearPref(Context context,String PrefName) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
		try {
			Editor editor = sharedpreferences.edit();
			editor.clear();
			editor.commit();

		} catch (Exception e) {
			// TODO: handle exception		
			e.printStackTrace();
		}
	}


}
