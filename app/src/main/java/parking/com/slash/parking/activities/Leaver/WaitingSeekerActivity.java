package parking.com.slash.parking.activities.Leaver;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.utlities.DataEnum;

public class WaitingSeekerActivity extends Activity implements OnMapReadyCallback {

    //region fields
    private GoogleMap googleMap;
    Double lat, lng;

    //endregion

    //region views
    @BindView(R.id.mvWaitingSeeker)
    MapView mvWaitingSeeker;

    //endregion

    //region
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_seeker);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra(DataEnum.intentLeaveLocationLat.name()));
        lng = Double.parseDouble(intent.getStringExtra(DataEnum.intentLeaveLocationLng.name()));
        initMap(savedInstanceState);
    }


    //region map
    protected void initMap(Bundle savedInstanceState) {
        mvWaitingSeeker.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            Log.e("mapError", e.getMessage());
        }

        mvWaitingSeeker.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        LatLng latLng = new LatLng(lat, lng);
        adjustMapLatLng(latLng);
    }


    private void adjustMapLatLng(@android.support.annotation.NonNull LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_avaliable_selected)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 16));
        }
    }

    //endregion
}
//https://github.com/lopspower/CircularProgressBar
//https://abhiandroid.com/ui/countdown-timer