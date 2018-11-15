package parking.com.slash.parking.activities.Leaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.activities.Seeker.SeekerMapsActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelGetNearBy.Model;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.HelpMe;
import retrofit2.Call;

public class WaitingSeekerActivity extends Activity implements OnMapReadyCallback, HandleRetrofitResp {

    //region fields
    private GoogleMap googleMap;
    Double lat, lng;
    Long leaveTime;
    CountDownTimer countDownTimer;
    Model model;
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
        model = (Model) intent.getSerializableExtra("model");
//        int animationDuration = 900000; // 2500ms = 2,5s
        HandleCalls.getInstance(this).setonRespnseSucess(this);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        HelpMe.getInstance(this);
        HelpMe.showMessage(this, getString(R.string.are_you_sure_to_cancel), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                callCancelRequest();
//                startActivity(new Intent(WaitingSeekerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
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

            switch (intent.getIntExtra("extra", 0)) {
                case 1:

                    Intent intent1 = new Intent(WaitingSeekerActivity.this, SeekerMapsActivity.class);
                    intent1.putExtra("fromLeaver", true);
                    intent1.putExtra("model", model);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void setCountDownTimer() {
        Calendar dateCalendar = Calendar.getInstance();
        int animationDuration = (int) (leaveTime - dateCalendar.getTimeInMillis());
        Log.d("timerDuration", animationDuration + "");
        if (dateCalendar.getTimeInMillis() >= leaveTime) {
            startActivity(new Intent(WaitingSeekerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
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
                    callCancelRequest();
                }
            }.start();
        }
    }
    //endregion

    //region calls


    private void callCancelRequest() {
        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
        modelCommonRequest.setRequestID(model.getRequestid());
        modelCommonRequest.setSeeker(false);
        Call call = HandleCalls.restParki.getClientService().callCancelRequest(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callCancelRequest.name(), true);
    }

    @Override
    public void onResponseSuccess(String flag, Object o) {
        startActivity(new Intent(WaitingSeekerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    @Override
    public void onNoContent(String flag, int code) {
        startActivity(new Intent(WaitingSeekerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    //endregion

    //region clicks

    @OnClick(R.id.btnWaitingCancel)
    public void onClickbtnWaitingCancel() {
        HelpMe.getInstance(this);
        HelpMe.showMessage(this, getString(R.string.are_you_sure_to_cancel), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                callCancelRequest();
//                startActivity(new Intent(WaitingSeekerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
    }

    //endregion
}
//https://github.com/lopspower/CircularProgressBar
//https://abhiandroid.com/ui/countdown-timer