package com.asa.findmyvolunteer;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class VictimFragment extends Fragment {
    private EditText inputName, inputPhone, inputSit,inputReq;
    private TextInputLayout inputLayoutName, inputLayoutPhone, inputLayoutSit,inputLayoutReq;
    private Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View view = inflater.inflate(R.layout.fragment_victim, container, false);
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) view.findViewById(R.id.input_layout_phone);
        inputLayoutSit = (TextInputLayout) view.findViewById(R.id.input_layout_sit);
        inputLayoutReq = (TextInputLayout) view.findViewById(R.id.input_layout_req);
        inputName = (EditText) view.findViewById(R.id.input_name);
        inputPhone = (EditText) view.findViewById(R.id.input_phone);
        inputSit = (EditText) view.findViewById(R.id.input_sit);
        inputReq = (EditText) view.findViewById(R.id.input_req);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();

            }
        });

        return view;
    }
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validateSit()) {
            return;
        }
        if (!validateReq()) {
            return;
        }
        Intent intent=new Intent(getActivity(),MapsActivity.class);
        intent.putExtra("name",inputName.getText().toString());
        intent.putExtra("phone",inputPhone.getText().toString());
        intent.putExtra("sit",inputSit.getText().toString());
        intent.putExtra("req",inputReq.getText().toString());
        startActivity(intent);

    }
    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setErrorEnabled(true);
            inputName.setError("Required");
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePhone() {
        if (inputPhone.getText().toString().trim().isEmpty()) {
            inputLayoutPhone.setErrorEnabled(true);
            inputPhone.setError("Required");
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateSit() {
        if (inputSit.getText().toString().trim().isEmpty()) {
            inputLayoutSit.setErrorEnabled(true);
            inputSit.setError("Required");
            return false;
        } else {
            inputLayoutSit.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateReq() {
        if (inputReq.getText().toString().trim().isEmpty()) {
            inputLayoutReq.setErrorEnabled(true);
            inputReq.setError("Required");
            return false;
        } else {
            inputLayoutReq.setErrorEnabled(false);
        }

        return true;
    }

}
