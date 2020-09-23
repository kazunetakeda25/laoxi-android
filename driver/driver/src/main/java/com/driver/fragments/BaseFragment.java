package com.driver.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.driver.R;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.TrackRidePojo.TrackRidePojo;

/**
 * Created by hlink54 on 20/7/16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    public abstract void getUpdateLatLongWsResponseFromService(TrackRide pojo);

    public void addFragment(Fragment fragment, Boolean backStack, String tag) {
        FragmentManager manager = getFragmentManager();
        //  FragmentTransaction fragmentTransaction = manager.get().beginTransaction();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.placeHolder, fragment);
        if (backStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void showSnackBar(String message, final EditText editText, View view) {

        if(view!=null)
        {
            Snackbar snackbar = Snackbar
                    .make(view, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText != null)
                            {
                                editText.requestFocus();
                                editText.setSelection(editText.getText().length());
                            }


                        }
                    });
            snackbar.show();
        }
    }


    public abstract void drawerLock();

    public boolean isValidEmail(EditText edtText) {
        if (edtText.getText().toString().trim() == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(edtText.getText().toString().trim()).matches();
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty())
            return true;
        return false;
    }

}
