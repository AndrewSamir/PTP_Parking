package parking.com.slash.parking.activities.Seeker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import developer.mokadim.projectmate.dialog.IndicatorStyle;
import developer.mokadim.projectmate.dialog.ProgressDialog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelGetNearBy.Model;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByRequest;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByResponse;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;

public class SeekerMapsActivity extends BaseActivity implements OnMapReadyCallback, HandleRetrofitResp, GoogleMap.OnMarkerClickListener
{

    //region fields
    Disposable disposable_location;
    Dialog progressDialog;
    private GoogleMap mMap;
    private String selectedRequestId;
    List<Marker> markerList;
    //endregion

    //region views
    @BindView(R.id.rlSeekerMapsDetailsCard)
    RelativeLayout rlSeekerMapsDetailsCard;
    @BindView(R.id.rlSeekerMapsDetailsCardBooked)
    RelativeLayout rlSeekerMapsDetailsCardBooked;

    @BindView(R.id.tvSeekerMapsTime)
    TextView tvSeekerMapsTime;
    @BindView(R.id.tvSeekerMapsStreet)
    TextView tvSeekerMapsStreet;
    @BindView(R.id.tvSeekerMapsArea)
    TextView tvSeekerMapsArea;
    @BindView(R.id.tvSeekerMapsPrice)
    TextView tvSeekerMapsPrice;

    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_maps);
//        getActionBar().hide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ButterKnife.bind(this);
        showLoading(true);
//        progressDialog = new ProgressDialog(this, IndicatorStyle.BallBeat).show();
//        progressDialog.show();

        hideToolBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }

        markerList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        HandleCalls.getInstance(getParent()).setonRespnseSucess(this);

//        callGetNearby();
    }

    @Override
    public void onResume()
    {
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
//        requestUserCurrentLocation();
        googleMap.setOnMarkerClickListener(this);

drawaRoutr();
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker("24$", true))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private Bitmap getBitmapMarker(String mText, boolean isSelected)
    {
        try
        {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            Resources resources = getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = null;
            if (!isSelected)
            {
                paint.setColor(Color.WHITE);
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin_avaliable_selected); // pin_with_price
            } else
            {
                paint.setColor(Color.BLACK);
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin_now);
            }

            // draw text to the Canvas center
            Rect bounds = new Rect();
//            FontAdapter.getInstance(context.getApplicationContext()).applyAndalusFont(paint);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
            paint.getTextBounds(mText, 0, mText.length(), bounds);

//            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            bitmap = Bitmap.createScaledBitmap(bitmap, Math.abs(bounds.width()) + 70, Math.abs(bounds.height()) + 70, false);
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

        } catch (Exception e)
        {
            return null;
        }
    }


    private void requestUserCurrentLocation()
    {
        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED)
        {
            requestLocationPermission();
            return;
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isNetworkEnabled && !isGpsEnabled)
        {
            showSettingsAlert();
            return;
        }


        if (mMap != null)
        {
            LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1)
                    .setInterval(100);

            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
            disposable_location = locationProvider.getUpdatedLocation(request).subscribe(new Consumer<Location>()
            {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Location location) throws Exception
                {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    adjustMapLatLng(currentLatLng);

                    if (disposable_location != null)
                    {
                        disposable_location.dispose();
                    }
                }
            });
        }

    }

    private void requestLocationPermission()
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
                            requestUserCurrentLocation();
                        } else if (report.isAnyPermissionPermanentlyDenied())
                        {
                            showMessage(R.string.please_grant_permissions);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token)
                    {
                        showPermissionRationaleMessage(token);

                    }

                }).check();
    }

    private void showPermissionRationaleMessage(final PermissionToken token)
    {
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
        });
    }


    private void adjustMapLatLng(@android.support.annotation.NonNull LatLng latLng)
    {
        if (mMap != null)
        {
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            /*mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.master_card)));*/
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 8));
            progressDialog.dismiss();
            showLoading(false);
            callGetNearby();
        }
    }

    private void setMarkers(List<Model> modelList)
    {
        if (mMap != null)
        {

            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            for (Model model : modelList)
            {
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
    public boolean onMarkerClick(Marker marker)
    {
        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        for (Marker mMarker : markerList)
        {

            Model model = (Model) mMarker.getTag();
            if (marker.getSnippet().equals(mMarker.getSnippet()))
            {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", true)));
                setDetailsCard(model);
            } else
            {
                mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", false)));
            }
        }
        return true;
    }

    private void drawaRoutr()
    {

        LatLng barcelona = new LatLng(41.385064, 2.173403);
        mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker in Barcelona"));

        LatLng madrid = new LatLng(40.416775, -3.70379);
        mMap.addMarker(new MarkerOptions().position(madrid).title("Marker in Madrid"));

        LatLng zaragoza = new LatLng(41.648823, -0.889085);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("YOUR_API_KEY")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
        try
        {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0)
            {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null)
                {
                    for (int i = 0; i < route.legs.length; i++)
                    {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null)
                        {
                            for (int j = 0; j < leg.steps.length; j++)
                            {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0)
                                {
                                    for (int k = 0; k < step.steps.length; k++)
                                    {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null)
                                        {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1)
                                            {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else
                                {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null)
                                    {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords)
                                        {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0)
        {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));
    }


    private void drawRoute(List<LatLng> routeDirection)
    {
        if (routeDirection != null)
        {
            PolylineOptions lineOptions = new PolylineOptions();

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(routeDirection);
            lineOptions.width(12);
            lineOptions.color(Color.rgb(79, 174, 175));

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);

            zoomRoute(mMap, routeDirection);
        }
    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute)
    {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 70;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    //endregion

    //region functions


    private void showSettingsAlert()
    {
        getMaterialDialogBuilder().content("gps_network_not_enabled")
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
        }).negativeText(this.getString(R.string.cancel)).show();

    }

    private void setDetailsCard(Model model)
    {

        tvSeekerMapsTime.setText(model.getLeavercarbrand());
        tvSeekerMapsStreet.setText(model.getLeavername());
        tvSeekerMapsArea.setText(model.getAddress());
        tvSeekerMapsPrice.setText(model.getFees() + " \n EGP");
        selectedRequestId = model.getRequestid();

        rlSeekerMapsDetailsCard.setVisibility(View.VISIBLE);
    }
    //endregion

    //region calls

    private void callGetNearby()
    {

        ModelGetNearByRequest modelGetNearByRequest = new ModelGetNearByRequest();
        modelGetNearByRequest.setLatitude("31.3786368");
        modelGetNearByRequest.setLongitude("30.0556288");
        modelGetNearByRequest.setPriceFrom(200);
        modelGetNearByRequest.setPriceTo(300);
        modelGetNearByRequest.setRadius("1000000");
        modelGetNearByRequest.setType(2);

        Call call = HandleCalls.restParki.getClientService().callGetNearby(modelGetNearByRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetNearby.name(), true);
    }


    private void callSeekerBook()
    {
        Call call = HandleCalls.restParki.getClientService().callSeekerBook(selectedRequestId);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callSeekerBook.name(), true);
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o)
    {
        if (flag.equals(DataEnum.callGetNearby.name()))
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelGetNearByResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelGetNearByResponse.class);
            setMarkers(modelGetNearByResponse.getModel());


        } else if (flag.equals(DataEnum.callSeekerBook.name()))
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            String ds = jsonObject.get("model").getAsString();
            Log.d("datagotten", ds);
            rlSeekerMapsDetailsCardBooked.setVisibility(View.VISIBLE);
            rlSeekerMapsDetailsCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoContent(String flag, int code)
    {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position)
    {

    }


    //endregion

    //region clicks

    @OnClick(R.id.btnSeekerMapsBook)
    void onClickbtnSeekerMapsBook(View view)
    {
        callSeekerBook();
    }

    //endregion
}
