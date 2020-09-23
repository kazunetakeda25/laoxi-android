package com.driver.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mahuateng on 1/4/18.
 */

public class FLogManager {

    private static FLogManager mFLogManager = new FLogManager();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("log_level").child("driver");
    public int logging_level;


    public static FLogManager getInstance() {
        return mFLogManager;
    }

    public void get_loglevel() {

        String driver_id = AppHelper.getInstance().getLoginUser().getDriverId();
//        String driver_id = LAOXI.getInstance().getLoginUser().getDriverId();
        Log.d("Get Log Level", driver_id);

        databaseReference.child(driver_id).child("log_level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String log_level = dataSnapshot.getValue().toString();
                    logging_level = Integer.parseInt(log_level);
                    Log.d("Log_level", log_level);
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

    public void Log() {

    }
}
