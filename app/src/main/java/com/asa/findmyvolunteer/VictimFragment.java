package com.asa.findmyvolunteer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class VictimFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private EditText inputName, inputPhone, inputSit, inputReq;
    private TextInputLayout inputLayoutName, inputLayoutPhone, inputLayoutSit, inputLayoutReq;
    private Button btnSubmit;
    FloatingActionButton fab;
    private Location loc;
    public Location mLastLocation;
    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    CoordinatorLayout coordinatorLayout;
    Double lat, lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View view = inflater.inflate(R.layout.fragment_victim, container, false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        buildGoogleApiClient();
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) view.findViewById(R.id.input_layout_phone);
        inputLayoutSit = (TextInputLayout) view.findViewById(R.id.input_layout_sit);
        inputLayoutReq = (TextInputLayout) view.findViewById(R.id.input_layout_req);
        inputName = (EditText) view.findViewById(R.id.input_name);
        inputPhone = (EditText) view.findViewById(R.id.input_phone);
        inputSit = (EditText) view.findViewById(R.id.input_sit);
        inputReq = (EditText) view.findViewById(R.id.input_req);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        coordinatorLayout= (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VictimData victimData=new VictimData();
                GeoPoint victimloc = new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                victimloc.addCategory("victims");
                victimData.setLocation(victimloc);
                victimData.setSos("true");
                Backendless.Persistence.of(VictimData.class).save(victimData, new AsyncCallback<VictimData>() {
                    @Override
                    public void handleResponse(VictimData v) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "SOS Request Sent!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.i("Fault", "Fail");

                    }
                });
            }
        });

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
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("name", inputName.getText().toString());
        intent.putExtra("phone", inputPhone.getText().toString());
        intent.putExtra("sit", inputSit.getText().toString());
        intent.putExtra("req", inputReq.getText().toString());
        startActivity(intent);
        getActivity().finish();

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

    @Override
    public void onLocationChanged(final Location location) {
//        Log.i("Latitude",""+location.getLatitude());
//        Log.i("Longitude",""+location.getLongitude());
        Log.i("onLocation","Reached");
        lat=location.getLatitude();
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        Log.i("onConnected", "Called");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mLocationRequest = LocationRequest.create();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    mLocationRequest.setInterval(1000);
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if(mLastLocation!=null)
                        Log.i("MLOCATION", "" + mLastLocation.getLatitude());

                }
        else
        {
            onConnected(bundle);
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission","Granted");

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mLocationRequest = LocationRequest.create();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    mLocationRequest.setInterval(1000); // Update location every second
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mLastLocation != null) {
                            Log.i("MLOCATION", "" + mLastLocation.getLatitude());
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();

    }
}
