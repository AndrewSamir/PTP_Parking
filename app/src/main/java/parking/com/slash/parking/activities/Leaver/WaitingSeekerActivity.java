package parking.com.slash.parking.activities.Leaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.utlities.DataEnum;

public class WaitingSeekerActivity extends Activity implements OnMapReadyCallback {

    //region fields
    private GoogleMap googleMap;
    Double lat, lng;
    Long leaveTime;
    CountDownTimer countDownTimer;
    //endregion

    //region views
    @BindView(R.id.mvWaitingSeeker)
    MapView mvWaitingSeeker;

    @BindView(R.id.circleProgressWaitingSeeker)
    CircularProgressBar circleProgressWaitingSeeker;

    @BindView(R.id.tvWaitingSeekerTimer)
    TextView tvWaitingSeekerTimer;
    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_seeker);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra(DataEnum.intentLeaveLocationLat.name()));
        lng = Double.parseDouble(intent.getStringExtra(DataEnum.intentLeaveLocationLng.name()));
        leaveTime = intent.getLongExtra(DataEnum.intentLeaveTime.name(), 900000);

//        int animationDuration = 900000; // 2500ms = 2,5s

        initMap(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNotificationReceiver, new IntentFilter("KEY"));
        setCountDownTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNotificationReceiver);
        countDownTimer.cancel();
    }


    //endregion
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

    //region functions


    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("notificationBroadCast", "received");

//                      Log.d("testBroadCast", intent.getStringExtra("KEY"));

          /*  Long currentTomeToStart = System.currentTimeMillis();
            final Long closeTimeStamp = Long.parseLong(modelConversationsDetails.getCloseTime());
            if (!callClose || currentTomeToStart > closeTimeStamp)
            {
                String message;
                if (isWriter)
                    message = getString(R.string.another_writer_close_the_conversation);
                else
                    message = getString(R.string.one_writer_close_the_conversation);

                showMessage(message);
            }
            getBaseActivity().onBackPressed();*/
        }
    };

    private void setCountDownTimer() {
        Calendar dateCalendar = Calendar.getInstance();
        int animationDuration = (int) (leaveTime - dateCalendar.getTimeInMillis());
        Log.d("timerDuration", animationDuration + "");
        if (dateCalendar.getTimeInMillis() >= leaveTime) {
//            callCloseConversations();
        } else {
            circleProgressWaitingSeeker.setProgressWithAnimation(100, animationDuration); // Default duration = 1500ms

            countDownTimer = new CountDownTimer((animationDuration), 1000) {
                public void onTick(long millisUntilFinished) {
                    SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                    String d = format.format(new Date(millisUntilFinished));
                    tvWaitingSeekerTimer.setText(d);
                }

                public void onFinish() {
//                    callClose = true;
                    tvWaitingSeekerTimer.setText("finished");
//                    callCloseConversations();
                }
            }.start();
        }
    }
    //endregion
}
//https://github.com/lopspower/CircularProgressBar
//https://abhiandroid.com/ui/countdown-timer