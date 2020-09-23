package com.driver.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.driver.R;
import com.driver.activity.BaseActivity;
import com.driver.activity.HomeActivity;
import com.driver.customclasses.CustomRadioButton;
import com.driver.listener.ISelectedCarType;
import com.driver.pojo.loginws.LoginResponse;
import com.driver.util.AppHelper;
import com.driver.util.DebugLog;
import com.driver.util.EnumHelper;

import static com.driver.R.id.cb1;

/**
 * Created by Chirag Solanki on 15/10/2016.
 */

public class SelectCarTypeDialog extends DialogFragment {

    private HomeActivity homeActivity;

    private RadioGroup radioGroup;
    private Button btnOk;
    private String strSelectedCar;
    EnumHelper.CarType carType;
    CustomRadioButton cbOscar, cbBigOscar, cbFancyOscar;

    public void setCallback(ISelectedCarType callback, String strSelectedCar) {
        this.callback = callback;
        this.strSelectedCar = strSelectedCar;

        for (EnumHelper.CarType dir : EnumHelper.CarType.values()) {

            if (dir.getSeletedString().equalsIgnoreCase(strSelectedCar + "")) {
                carType = dir;
            }
        }
        DebugLog.e("Finally" + carType.getSeletedString());
    }


    ISelectedCarType callback;
    EnumHelper.CarType selectedCarType = setLoginDriverCartype();//=EnumHelper.CarType.OSCAR;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            homeActivity = (HomeActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_type_dilog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        uiInitialization();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(radioButtonID);
                switch (radioButton.getId()) {
                    case cb1:
                        selectedCarType = EnumHelper.CarType.OSCAR;
                        break;
                    case R.id.cb2:
                        selectedCarType = EnumHelper.CarType.BIG_OSCAR;
                        break;
                    case R.id.cb3:
                        selectedCarType = EnumHelper.CarType.FANCY_OSCAR;
                        break;
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.selectedCarType(selectedCarType);
                dismiss();
            }
        });

    }


    private void uiInitialization() {
        radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);

        cbOscar = (CustomRadioButton) getView().findViewById(R.id.cb1);
        cbBigOscar = (CustomRadioButton) getView().findViewById(R.id.cb2);
        cbFancyOscar = (CustomRadioButton) getView().findViewById(R.id.cb3);

        btnOk = (Button) getView().findViewById(R.id.btnOk);
        setDefaultSelect();

    }

    private void setDefaultSelect() {
        switch (carType) {
            case OSCAR:
                cbOscar.setChecked(true);
                break;
            case BIG_OSCAR:
                cbBigOscar.setChecked(true);
                break;
            case FANCY_OSCAR:
                cbFancyOscar.setChecked(true);
                break;
        }
    }

    private EnumHelper.CarType setLoginDriverCartype() {
        LoginResponse loginRespons = AppHelper.getInstance().getLoginUser();
        for (EnumHelper.CarType c : EnumHelper.CarType.values()) {
            if (c.getSeletedString().equalsIgnoreCase(loginRespons.getCarType())) {
                return c;
            }
        }
        return null;
    }
}
