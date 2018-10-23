package parking.com.slash.parking.activities.Leaver;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import developer.mokadim.projectmate.dialog.IndicatorStyle;
import developer.mokadim.projectmate.dialog.ProgressDialog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.Seeker.ConfirmRequestActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelConfirmRequest.ModelConfirmRequest;
import parking.com.slash.parking.model.ModelGetAddressFromMap.ModelGetAddressFromMap;
import parking.com.slash.parking.model.ModelLeaverBookRequest.ModelLeaverBookRequest;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;

public class LeaverMapsActivity extends AppCompatActivity implements OnMapReadyCallback, HandleRetrofitResp {

    //region fields

    Disposable disposable_location;
    Dialog progressDialog;
    private GoogleMap mMap;
    private String street;
    private String address;
    private int status = 1;// 1-> Now , 2->Later
    private String lat, lng;
    LatLng currentLatLng;
    //endregion

    //region views

    @BindView(R.id.layoutLeaverMapLeaveOptions)
    View layoutLeaverMapLeaveOptions;
    @BindView(R.id.layoutLeaverMapStartLeave)
    View layoutLeaverMapStartLeave;

    @BindView(R.id.tvLeaverMapsStartLeavingStreet)
    TextView tvLeaverMapsStartLeavingStreet;

    @BindView(R.id.tvLeaverMapsStartLeavingAddress)
    TextView tvLeaverMapsStartLeavingAddress;

    @BindView(R.id.edtLeaverMapsStartLeavingPrice)
    EditText edtLeaverMapsStartLeavingPrice;

    @BindView(R.id.tvLeaverMapsStartLeavingTime)
    TextView tvLeaverMapsStartLeavingTime;

    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaver_maps);
        getSupportActionBar().hide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progressDialog = new ProgressDialog(this, IndicatorStyle.BallBeat).show();

        ButterKnife.bind(this);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLeaver);
        mapFragment.getMapAsync(this);

        HandleCalls.getInstance(getParent()).setonRespnseSucess(this);

//        callGetNearby();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //endregion

    //region map

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestUserCurrentLocation();
    }

    private void requestUserCurrentLocation() {
        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isNetworkEnabled && !isGpsEnabled) {
            progressDialog.dismiss();
            showSettingsAlert();
            return;
        }


        if (mMap != null) {
            LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1)
                    .setInterval(100);

            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
            disposable_location = locationProvider.getUpdatedLocation(request).subscribe(new Consumer<Location>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Location location) throws Exception {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    lat = location.getLatitude() + "";
                    lng = location.getLongitude() + "";
                    adjustMapLatLng(currentLatLng);

                    if (disposable_location != null) {
                        disposable_location.dispose();
                    }
                }
            });
        }

    }

    private void requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            requestUserCurrentLocation();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
//                            showMessage(R.string.please_grant_permissions);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        showPermissionRationaleMessage(token);

                    }

                }).check();
    }

    private void showPermissionRationaleMessage(final PermissionToken token) {/*
        showMessage(this, getResources().getString(R.string.permissions_needed), new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                token.continuePermissionRequest();
                dialog.dismiss();
            }
        }, new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                token.cancelPermissionRequest();
                dialog.dismiss();
            }
        });*/
    }


    private void adjustMapLatLng(@NonNull LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_avaliable_selected)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 8));
            progressDialog.dismiss();
            callGetAddressFromMap();
//            showLoading(false);
//            callGetNearby();
        }
    }

    //endregion

    //region functions

    private void showSettingsAlert() {
        Intent locationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(locationSettingsIntent);

        /*MaterialDialog().content("gps_network_not_enabled")
                .positiveText("open_location_settings")
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support
                            .annotation.NonNull DialogAction which)
                    {
                        Intent locationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(locationSettingsIntent);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull
                    DialogAction which)
            {
                dialog.dismiss();
            }
        }).negativeText(this.getString(R.string.cancel)).show();*/
    }

    private void showBookDetailsDialog(boolean showTime) {
        layoutLeaverMapLeaveOptions.setVisibility(View.GONE);
        layoutLeaverMapStartLeave.setVisibility(View.VISIBLE);
        tvLeaverMapsStartLeavingStreet.setText("");
        tvLeaverMapsStartLeavingAddress.setText("");

        if (showTime)
            tvLeaverMapsStartLeavingTime.setVisibility(View.VISIBLE);
        else
            tvLeaverMapsStartLeavingTime.setVisibility(View.GONE);
    }
    //endregion

    //region calls


    private void callLeaverBook() {
        ModelLeaverBookRequest modelLeaverBookRequest = new ModelLeaverBookRequest();
        modelLeaverBookRequest.setFees(Integer.parseInt(edtLeaverMapsStartLeavingPrice.getText().toString()));
        modelLeaverBookRequest.setAddress(address);
        modelLeaverBookRequest.setLatitude(currentLatLng.latitude + "");
        modelLeaverBookRequest.setLongitude(currentLatLng.longitude + "");
        modelLeaverBookRequest.setLeavingtime(tvLeaverMapsStartLeavingTime.getText().toString());
        modelLeaverBookRequest.setType(status);
        modelLeaverBookRequest.setArea("strinmg");

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + SharedPrefHelper.getInstance(this).getAccessToken());
        stringStringMap.put("Content-Type", "application/json");
        Call call = HandleCalls.restParki.getClientService().callLeaverBook(modelLeaverBookRequest, stringStringMap);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callLeaverBook.name(), true);
    }

    private void callGetAddressFromMap() {

        String toSend = currentLatLng.latitude + "," + currentLatLng.longitude;
        Call call = HandleCalls.restParki.getClientService().callGetAddressFromMap(toSend,
                getString(R.string.google_maps_key), "en");
        HandleCalls.getInstance(this).callRetrofitGoogleAPi(call, DataEnum.callGetAddressFromMap.name(), true);
    }


    private void callConfirmRequest() {
        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
//        modelCommonRequest.setRequestID(bookedID);
        modelCommonRequest.setSeeker(true);
        modelCommonRequest.setPaymentMethod(0);
        Call call = HandleCalls.restParki.getClientService().callConfirmRequest(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callConfirmRequest.name(), true);
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o) {
        if (flag.equals(DataEnum.callLeaverBook.name())) {

            Intent intent = new Intent(LeaverMapsActivity.this, WaitingSeekerActivity.class);
            String time = "600";
            intent.putExtra(DataEnum.intentLeaveTime.name(), time);
            intent.putExtra(DataEnum.intentLeaveLocationLat.name(), lat);
            intent.putExtra(DataEnum.intentLeaveLocationLng.name(), lng);
            startActivity(intent);
            finish();

        } else if (flag.equals(DataEnum.callGetAddressFromMap.name())) {
            ModelGetAddressFromMap modelGetAddressFromMap = (ModelGetAddressFromMap) o;

            address = modelGetAddressFromMap.getResults().get(0).getFormatted_address();

        }


    }

    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }


    //endregion

    //region clicks

    @OnClick(R.id.btnLeaverMapLeaveNow)
    public void onClickbtnLeaverMapLeaveNow() {
        status = 1;
        showBookDetailsDialog(false);
    }

    @OnClick(R.id.btnLeaverMapLater)
    public void onClickbtnLeaverMapLater() {
        status = 2;
        showBookDetailsDialog(true);
    }

    @OnClick(R.id.btnLeaverMapsStartLeaving)
    public void onClickbtnLeaverMapsStartLeaving() {
        if (edtLeaverMapsStartLeavingPrice.getText().toString().length() > 0) {
            if (status == 1)
                callLeaverBook();
            else if (tvLeaverMapsStartLeavingTime.getText().toString().length() > 0) {
                callLeaverBook();
            } else {
                tvLeaverMapsStartLeavingTime.setError(getString(R.string.required));
            }
        } else {
            edtLeaverMapsStartLeavingPrice.setError(getString(R.string.required));
        }
    }

    @OnClick(R.id.btnLeaverMapsCancel)
    public void onClickbtnLeaverMapsCancel() {

        layoutLeaverMapLeaveOptions.setVisibility(View.VISIBLE);
        layoutLeaverMapStartLeave.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvLeaverMapsStartLeavingTime)
    public void onClicktvLeaverMapsStartLeavingTime() {
        // TODO submit data to server...
    }
    //endregion
}
