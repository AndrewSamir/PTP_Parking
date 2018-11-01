package parking.com.slash.parking.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelHistory.Model;
import parking.com.slash.parking.retorfitconfig.HandleCalls;

public class ParkingDetails extends BaseFragment implements HandleRetrofitResp, OnMapReadyCallback
{
    //region fields

    private static Model model;
    private GoogleMap googleMap;

    //endregion

    //region views

    @BindView(R.id.mvParkingDetails)
    MapView mvParkingDetails;
    @BindView(R.id.tvParkingDetailsDate)
    TextView tvParkingDetailsDate;
    @BindView(R.id.tvParkingDetailsStreet)
    TextView tvParkingDetailsStreet;
    @BindView(R.id.tvParkingDetailsArea)
    TextView tvParkingDetailsArea;
    @BindView(R.id.tvParkingDetailsPrice)
    TextView tvParkingDetailsPrice;
    @BindView(R.id.tvParkingDetailsState)
    TextView tvParkingDetailsState;
    @BindView(R.id.imgParkingDetailsCar)
    RoundedImageView imgParkingDetailsCar;
    @BindView(R.id.tvParkingDetailsPayPalAccount)
    TextView tvParkingDetailsPayPalAccount;
    @BindView(R.id.tvParkingDetailsCarBrand)
    TextView tvParkingDetailsCarBrand;
    @BindView(R.id.tvParkingDetailsCarPlate)
    TextView tvParkingDetailsCarPlate;
    @BindView(R.id.tvParkingDetailsCarOwnerName)
    TextView tvParkingDetailsCarOwnerName;

    //endregion

    //region life cycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.parking_details, container, false);

        unbinder = ButterKnife.bind(this, view);
        initMap(savedInstanceState);
        adjustView();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        HandleCalls.getInstance(getBaseActivity()).setonRespnseSucess(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
//        appHeader.setRight(0, 0, 0);
    }

    //endregion

    //region parent methods
    @Override
    protected boolean canShowAppHeader()
    {
        return false;
    }

    @Override
    protected boolean canShowBottomBar()
    {
        return false;
    }

    @Override
    protected boolean canShowBackArrow()
    {
        return false;
    }

    @Override
    protected String getTitle()
    {
        return null;
    }

    @Override
    public int getSelectedMenuId()
    {
        return 0;
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o)
    {

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

    //endregion

    //region calls

    //endregion

    //region functions

    public static ParkingDetails init(Model model)
    {
        setModel(model);
        return new ParkingDetails();
    }

    public static void setModel(Model model)
    {
        ParkingDetails.model = model;
    }

    private void adjustView()
    {

        Date newDate = new Date(), newTime = new Date();
        String[] arrTimeSt = model.getRequestdate().split("T");
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try
        {
            newDate = format.parse(arrTimeSt[0]);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        DateFormat newFormatDate = new java.text.SimpleDateFormat("EEEE , d MMMM yyyy", Locale.ENGLISH);
        String date = newFormatDate.format(newDate);

        DateFormat format_ = new java.text.SimpleDateFormat("kk:mm:ss.SSS", Locale.ENGLISH);
        try
        {
            newTime = format_.parse(arrTimeSt[1]);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        DateFormat newFormatTime = new java.text.SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String time = newFormatTime.format(newTime);


        tvParkingDetailsDate.setText(date + " - " + time);
        tvParkingDetailsArea.setText(model.getArea());
        tvParkingDetailsStreet.setText(model.getAddress());
        tvParkingDetailsPrice.setText(model.getFees() + "");
        tvParkingDetailsState.setText(model.getType() + "");
        tvParkingDetailsCarBrand.setText("brand");
        tvParkingDetailsCarPlate.setText("car plate");
        tvParkingDetailsCarOwnerName.setText(model.getName());

        if (model.getCarimage() != null)
            Picasso.with(getBaseActivity())
                    .load(model.getCarimage())
                    .into(imgParkingDetailsCar);
    }
//endregion

    //region map
    protected void initMap(Bundle savedInstanceState)
    {
        mvParkingDetails.onCreate(savedInstanceState);

        try
        {
            MapsInitializer.initialize(this.getBaseActivity());
        } catch (Exception e)
        {
            Log.e("mapError", e.getMessage());
        }

        mvParkingDetails.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.googleMap = map;

        LatLng latLng = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
        adjustMapLatLng(latLng);
    }


    private void adjustMapLatLng(@android.support.annotation.NonNull LatLng latLng)
    {
        if (googleMap != null)
        {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_avaliable_selected)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 11));

        }
    }

    //endregion

}
