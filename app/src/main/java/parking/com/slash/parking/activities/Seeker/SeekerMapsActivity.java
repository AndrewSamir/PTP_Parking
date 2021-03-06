package parking.com.slash.parking.activities.Seeker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.warkiz.widget.IndicatorSeekBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import developer.mokadim.projectmate.dialog.IndicatorStyle;
import developer.mokadim.projectmate.dialog.ProgressDialog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelConfirmRequest.ModelConfirmRequest;
import parking.com.slash.parking.model.ModelGetNearBy.Model;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByRequest;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByResponse;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.HelpMe;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;

import static parking.com.slash.parking.utlities.HelpMe.showMessage;

public class SeekerMapsActivity extends AppCompatActivity implements OnMapReadyCallback, HandleRetrofitResp, GoogleMap.OnMarkerClickListener {

    //region fields

    Disposable disposable_location;
    Dialog progressDialog;
    private GoogleMap mMap;
    private String selectedRequestId;
    List<Marker> markerList;
    private String bookedID;
    private String leaverMobile;
    LatLng currentLatLng;
    private int status = 1;// 1-> Now , 2->Later
    private int priceFrom = 0;
    private int priceTo = 1000;
    private int time = 24;
    private String raduisInMeter = "5000";
    private String lat, lng;
    private Double selectedLat, selectedLng;
    private boolean fromLeaver = false;
    Model model;
    //endregion

    //region views
    @BindView(R.id.layoutDetailsCard)
    RelativeLayout rlSeekerMapsDetailsCard;
    @BindView(R.id.layoutBookedCard)
    RelativeLayout rlSeekerMapsDetailsCardBooked;

    @BindView(R.id.tvSeekerMapsTime)
    TextView tvSeekerMapsTime;
    @BindView(R.id.tvSeekerMapsStreet)
    TextView tvSeekerMapsStreet;
    @BindView(R.id.tvSeekerMapsArea)
    TextView tvSeekerMapsArea;
    @BindView(R.id.tvSeekerMapsPrice)
    TextView tvSeekerMapsPrice;
    @BindView(R.id.tvSeekerMapsPriceBooked)
    TextView tvSeekerMapsPriceBooked;
    @BindView(R.id.tvSeekerMapsTimeBooked)
    TextView tvSeekerMapsTimeBooked;
    @BindView(R.id.tvSeekerMapsStreetBooked)
    TextView tvSeekerMapsStreetBooked;
    @BindView(R.id.tvSeekerMapsAreaBooked)
    TextView tvSeekerMapsAreaBooked;
    @BindView(R.id.tvSeekMapCarBrandBooked)
    TextView tvSeekMapCarBrandBooked;
    @BindView(R.id.imgCarBooked)
    RoundedImageView imgCarBooked;
    @BindView(R.id.tvSeekerMapCarPlateBooked)
    TextView tvSeekerMapCarPlateBooked;
    @BindView(R.id.tvSeekerMapNameBooked)
    TextView tvSeekerMapNameBooked;
    @BindView(R.id.btnSeekerMapsConfirmBooked)
    Button btnSeekerMapsConfirmBooked;
    @BindView(R.id.seekBarDialogSearchStatus)
    IndicatorSeekBar seekBarDialogSearchStatus;
    @BindView(R.id.seekBarDialogSearchRange)
    IndicatorSeekBar seekBarDialogSearchRange;
    @BindView(R.id.layoutStatus)
    View layoutStatus;
    @BindView(R.id.layoutRange)
    View layoutRange;
    @BindView(R.id.layoutPrice)
    View layoutPrice;
    @BindView(R.id.layoutSearchFilter)
    View layoutSearchFilter;
    @BindView(R.id.imgStatusDialogNow)
    View imgStatusDialogNow;
    @BindView(R.id.indicatorStatus)
    View indicatorStatus;
    @BindView(R.id.imgStatusDialogLater)
    View imgStatusDialogLater;
    @BindView(R.id.llSearchFilterStatus)
    LinearLayout llSearchFilterStatus;
    @BindView(R.id.llSearchFilterRange)
    LinearLayout llSearchFilterRange;
    @BindView(R.id.llSearchFilterPrice)
    LinearLayout llSearchFilterPrice;
    @BindView(R.id.layoutNoParkingLots)
    View layoutNoParkingLots;
    @BindView(R.id.llStatusDialogTitles)
    View llStatusDialogTitles;
    @BindView(R.id.tvSearchFilterStatus)
    TextView tvSearchFilterStatus;
    @BindView(R.id.tvSearchFilterRange)
    TextView tvSearchFilterRange;

    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_maps);
        getSupportActionBar().hide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progressDialog = new ProgressDialog(this, IndicatorStyle.BallBeat).show();

     /*   showLoading(true);
//        progressDialog.show();

        hideToolBar();*/
        ButterKnife.bind(this);


        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }


        Intent intent = getIntent();
        if (intent.hasExtra("fromLeaver")) {
            fromLeaver = true;
            model = (Model) intent.getSerializableExtra("model");
        }
        markerList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        HandleCalls.getInstance(getParent()).setonRespnseSucess(this);

        seekBarDialogSearchStatus.setIndicatorTextFormat(" ${PROGRESS} Hours ");
        seekBarDialogSearchRange.setIndicatorTextFormat(" ${PROGRESS}K ");

//        callGetNearby();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mNotificationReceiver, new IntentFilter("KEY"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNotificationReceiver);
//        countDownTimer.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    requestUserCurrentLocation();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (rlSeekerMapsDetailsCardBooked.getVisibility() == View.GONE)
            super.onBackPressed();
        else
            startActivity(new Intent(this, CancelRequestActivity.class)
                    .putExtra(DataEnum.intentRequestId.name(), bookedID));

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
        googleMap.setOnMarkerClickListener(this);

//drawaRoutr();
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker("24$", true))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private Bitmap getBitmapMarker(String mText, boolean isSelected) {
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            Resources resources = getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = null;
            if (!isSelected) {
                paint.setColor(Color.WHITE);
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin_avaliable_selected); // pin_with_price
            } else {
                paint.setColor(Color.BLACK);
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin_now);
            }

            // draw text to the Canvas center
            Rect bounds = new Rect();
//            FontAdapter.getInstance(context.getApplicationContext()).applyAndalusFont(paint);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
            paint.getTextBounds(mText, 0, mText.length(), bounds);

//            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            bitmap = Bitmap.createScaledBitmap(bitmap, Math.abs(bounds.width()) + 20, Math.abs(bounds.height()) + 70, false);
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);


            int x = (bitmap.getWidth() - bounds.width()) / 2;
            int y = (bitmap.getHeight() + bounds.height()) / 2;

            canvas.drawText(mText, x, y - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()), paint);
            return bitmap;

        } catch (Exception e) {
            return null;
        }
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
                            HelpMe.getInstance(SeekerMapsActivity.this).showMessage(R.string.please_grant_permissions);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        showPermissionRationaleMessage(token);
                    }
                }).check();
    }

    private void showPermissionRationaleMessage(final PermissionToken token) {
        showMessage(this, getResources().getString(R.string.permissions_needed), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                token.continuePermissionRequest();
                dialog.dismiss();
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                token.cancelPermissionRequest();
                dialog.dismiss();
            }
        });
    }


    private void adjustMapLatLng(@android.support.annotation.NonNull LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16));
            progressDialog.dismiss();
//            showLoading(false);

            if (!fromLeaver)
                callGetNearby();
            else
                setBookedCard(model);
        }
    }

    private void setMarkers(List<Model> modelList) {
        if (mMap != null) {

            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            for (Model model : modelList) {
                LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", false))));

        /*        MarkerOptions position = new MarkerOptions().position(latLng);
                position.icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", false)));
                Marker marker = mMap.addMarker(position);
        */
                marker.setTag(model);
                marker.setSnippet(model.getRequestid());
                markerList.add(marker);

            }

//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 8));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        layoutStatus.setVisibility(View.GONE);
        layoutRange.setVisibility(View.GONE);
        layoutPrice.setVisibility(View.GONE);

        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        for (Marker mMarker : markerList) {

            Model model = (Model) mMarker.getTag();
            if (marker.getSnippet().equals(mMarker.getSnippet())) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", true)));
                setDetailsCard(model);
            } else {
                mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", false)));
            }
        }
        return true;
    }


    //endregion

    //region functions


    private void showSettingsAlert() {


        HelpMe.getInstance(this).showMessage(SeekerMapsActivity.this, getResources().getString(R.string.gps_network_not_enabled), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent locationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(locationSettingsIntent, 1);
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
    }

    private void setDetailsCard(Model model) {
        tvSeekerMapsTime.setText(model.getLeavingtime());
        tvSeekerMapsStreet.setText(model.getLeavername());
        tvSeekerMapsArea.setText(model.getAddress());
        tvSeekerMapsPrice.setText(model.getFees() + " \n EGP");
        selectedRequestId = model.getRequestid();
        selectedLat = model.getLatitude();
        selectedLng = model.getLongitude();
        rlSeekerMapsDetailsCard.setVisibility(View.VISIBLE);
    }

    private void setBookedCard(Model model) {

        tvSeekerMapsTimeBooked.setText(model.getLeavercarbrand());
        tvSeekerMapsStreetBooked.setText(model.getLeavername());
        tvSeekerMapsAreaBooked.setText(model.getAddress());
        tvSeekerMapsPriceBooked.setText(model.getFees() + " \n EGP");
//        selectedRequestId = model.getRequestid();
        tvSeekMapCarBrandBooked.setText(model.getLeavercarmodel());
        tvSeekerMapCarPlateBooked.setText(model.getLeavercarno());
        tvSeekerMapNameBooked.setText(model.getLeavername());
        leaverMobile = model.getLeavermobile();
        Picasso.with(this)
                .load(model.getLeavercarimage());
        rlSeekerMapsDetailsCardBooked.setVisibility(View.VISIBLE);
        rlSeekerMapsDetailsCard.setVisibility(View.GONE);

        Handler handler = new Handler();

        Date newDate = new Date(), newTime = new Date();
        String[] arrTimeSt = model.getLeavingtime().split("T");
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            newDate = format.parse(arrTimeSt[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format_ = new java.text.SimpleDateFormat("kk:mm:ss.SSS", Locale.ENGLISH);
        try {
            newTime = format_.parse(arrTimeSt[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar leavingTime = Calendar.getInstance();
        leavingTime.set(newDate.getYear(), newDate.getMonth(), newDate.getDay(), newTime.getHours(), newTime.getMinutes(), newTime.getSeconds());


        Calendar dateCalendar = Calendar.getInstance();
        int animationDuration = (int) (leavingTime.getTimeInMillis() - dateCalendar.getTimeInMillis());
        Log.d("leavingTime", animationDuration + "");
        handler.postDelayed(new Runnable() {
            public void run() {
                btnSeekerMapsConfirmBooked.setVisibility(View.VISIBLE);
            }
        }, animationDuration);
    }

    private void drawRoute(LatLng latLng) {

        List<LatLng> latLngs = new ArrayList<>();
        PolylineOptions lineOptions = new PolylineOptions();
        latLngs.add(currentLatLng);
        latLngs.add(latLng);
        // Adding all the points in the route to LineOptions
        lineOptions.addAll(latLngs);
        lineOptions.width(12);
        lineOptions.color(Color.rgb(79, 174, 175));

        // Drawing polyline in the Google Map for the i-th route
        mMap.addPolyline(lineOptions);

        zoomRoute(mMap, latLngs);

    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 70;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }


    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("notificationBroadCast", "received");
            switch (intent.getIntExtra("extra", 0)) {
                case 3:
                    startActivity(new Intent(SeekerMapsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                    break;

                case 5:

                    requestUserCurrentLocation();
                    Location loc1 = new Location("");
                    loc1.setLatitude(Double.parseDouble(lat));
                    loc1.setLongitude(Double.parseDouble(lng));

                    Location loc2 = new Location("");
                    loc2.setLatitude(selectedLat);
                    loc2.setLongitude(selectedLng);
                    float distanceInMeters = loc1.distanceTo(loc2);

                    if (distanceInMeters < 200) {
                        callConfirmRequest();
                    }
                    break;
            }
        }
    };
    //endregion

    //region calls

    private void callGetNearby() {
        progressDialog.dismiss();
        layoutPrice.setVisibility(View.GONE);
        layoutStatus.setVisibility(View.GONE);
        layoutRange.setVisibility(View.GONE);
        layoutNoParkingLots.setVisibility(View.GONE);
        layoutSearchFilter.setVisibility(View.VISIBLE);
        markerList.clear();
        mMap.clear();
        ModelGetNearByRequest modelGetNearByRequest = new ModelGetNearByRequest();
        modelGetNearByRequest.setLatitude(lat);
        modelGetNearByRequest.setLongitude(lng);
        modelGetNearByRequest.setPriceFrom(priceFrom);
        modelGetNearByRequest.setPriceTo(priceTo);
        modelGetNearByRequest.setRadius(raduisInMeter);
        modelGetNearByRequest.setType(status);
        modelGetNearByRequest.setTime(time);

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + SharedPrefHelper.getInstance(this).getAccessToken());
        stringStringMap.put("Content-Type", "application/json");
        Call call = HandleCalls.restParki.getClientService().callGetNearby(modelGetNearByRequest, stringStringMap);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetNearby.name(), true);
    }


    private void callSeekerBook() {
        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
        modelCommonRequest.setRequestID(selectedRequestId);
        modelCommonRequest.setLatitude(lat);
        modelCommonRequest.setLongitude(lng);
        Call call = HandleCalls.restParki.getClientService().callSeekerBook(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callSeekerBook.name(), true);
    }


    private void callConfirmRequest() {
        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
        modelCommonRequest.setRequestID(bookedID);
        modelCommonRequest.setSeeker(true);
        modelCommonRequest.setPaymentMethod(0);
        Call call = HandleCalls.restParki.getClientService().callConfirmRequest(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callConfirmRequest.name(), true);
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o) {
        if (flag.equals(DataEnum.callGetNearby.name())) {
            layoutSearchFilter.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelGetNearByResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelGetNearByResponse.class);
            if (modelGetNearByResponse.getModel().size() == 0)
                layoutNoParkingLots.setVisibility(View.VISIBLE);
            else
                setMarkers(modelGetNearByResponse.getModel());


        } else if (flag.equals(DataEnum.callSeekerBook.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            bookedID = jsonObject.get("model").getAsString();


            for (Marker marker : markerList) {
                Model model = (Model) marker.getTag();
                if (!model.getRequestid().equals(selectedRequestId))
                    marker.setVisible(false);
                else {
                    setBookedCard(model);
                    LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());
                    drawRoute(latLng);
                }
            }

        } else if (flag.equals(DataEnum.callCancelRequest.name())) {
            mMap.clear();
            markerList.clear();
            rlSeekerMapsDetailsCardBooked.setVisibility(View.GONE);
            callGetNearby();
        } else if (flag.equals(DataEnum.callConfirmRequest.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelConfirmRequest modelConfirmRequest = gson.fromJson(jsonObject, ModelConfirmRequest.class);

            Intent intent = new Intent(SeekerMapsActivity.this, ConfirmRequestActivity.class);
//            intent.putExtra(DataEnum.intentModelConfirmRequest.name(), modelConfirmRequest);
            intent.putExtra(DataEnum.intentModel.name(), (Model) markerList.get(0).getTag());
            startActivity(intent);
            finish();
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

    @OnClick(R.id.btnSeekerMapsBook)
    void onClickbtnSeekerMapsBook(View view) {
        layoutSearchFilter.setVisibility(View.GONE);
        callSeekerBook();
    }

    @OnClick(R.id.imgSeekerCall)
    void onClickimgSeekerCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + leaverMobile));
        startActivity(intent);
    }

    @OnClick(R.id.btnSeekerMapsConfirmBooked)
    void onClickbtnSeekerMapsConfirmBooked(View view) {
        callConfirmRequest();
    }

    @OnClick(R.id.btnSeekerMapsCancelBooked)
    void onClickbtnSeekerMapsCancelBooked(View view) {
        startActivity(new Intent(this, CancelRequestActivity.class)
                .putExtra(DataEnum.intentRequestId.name(), bookedID));
    }

    @OnClick(R.id.llSearchFilterStatus)
    void onClickllSearchFilterStatus(View view) {
        if (layoutStatus.getVisibility() == View.VISIBLE)
            layoutStatus.setVisibility(View.GONE);
        else
            layoutStatus.setVisibility(View.VISIBLE);
        layoutRange.setVisibility(View.GONE);
        layoutPrice.setVisibility(View.GONE);
    }

    @OnClick(R.id.llSearchFilterRange)
    void onClickllSearchFilterRange(View view) {
        if (layoutRange.getVisibility() == View.VISIBLE)
            layoutRange.setVisibility(View.GONE);
        else
            layoutRange.setVisibility(View.VISIBLE);
        layoutStatus.setVisibility(View.GONE);
        layoutPrice.setVisibility(View.GONE);
    }

    @OnClick(R.id.llSearchFilterPrice)
    void onClickllSearchFilterPrice(View view) {
        if (layoutPrice.getVisibility() == View.VISIBLE)
            layoutPrice.setVisibility(View.GONE);
        else
            layoutPrice.setVisibility(View.VISIBLE);
        layoutStatus.setVisibility(View.GONE);
        layoutRange.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnStatus)
    void onClickbtnStatus(View view) {

        if (imgStatusDialogNow.getVisibility() == View.VISIBLE) {
            status = 1;
            llSearchFilterStatus.setBackground(getResources().getDrawable(R.drawable.background_txt_available));
            tvSearchFilterStatus.setText(getString(R.string.available_now));
        } else {
            status = 2;
            time = seekBarDialogSearchStatus.getProgress();
            llSearchFilterStatus.setBackground(getResources().getDrawable(R.drawable.background_txt_available_later));
            tvSearchFilterStatus.setText(seekBarDialogSearchStatus.getProgress() + "hours Later");
        }
        callGetNearby();
    }

    @OnClick(R.id.tvStatusDialogNow)
    void onClicktvStatusDialogNow(View view) {
        indicatorStatus.setVisibility(View.GONE);
        imgStatusDialogNow.setVisibility(View.VISIBLE);
        imgStatusDialogLater.setVisibility(View.GONE);
        llStatusDialogTitles.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.tvStatusDialogLater)
    void onClicktvStatusDialogLater(View view) {
        indicatorStatus.setVisibility(View.VISIBLE);
        imgStatusDialogNow.setVisibility(View.GONE);
        imgStatusDialogLater.setVisibility(View.VISIBLE);
        llStatusDialogTitles.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnRange)
    void onClickbtnRange(View view) {
        raduisInMeter = seekBarDialogSearchRange.getProgress() + "000";
        tvSearchFilterRange.setText(seekBarDialogSearchRange.getProgress() + "K Range");
        callGetNearby();
    }
    //endregion
}
