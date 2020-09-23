package com.rider.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rider.pojo.User;

/**
 * Created by mahuateng on 1/4/18.
 */

public class FLogManager {

    private static FLogManager mFLogManager = new FLogManager();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("log_level").child("user");
    public int logging_level;


    public static FLogManager getInstance() {
        return mFLogManager;
    }

    public void get_loglevel(Context context) {

        User user;

        String strUserData = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);
//        user = Laoxi.getInstance().getLoginUser();

        databaseReference.child(user.getId()).child("log_level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String log_level = dataSnapshot.getValue().toString();
                    logging_level = Integer.parseInt(log_level);
                    Log.d("Log_level", String.valueOf(logging_level));
                } else {
                    logging_level = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.d("Log_level", databaseError.toString());
            }
        });
    }

}

