package parking.com.slash.parking.activities.Seeker;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

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
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.model.ModelConfirmRequest.ModelConfirmRequest;
import parking.com.slash.parking.model.ModelGetNearBy.Model;
import parking.com.slash.parking.utlities.DataEnum;

public class ConfirmRequestActivity extends Activity implements OnMapReadyCallback
{

    //region fields
    ModelConfirmRequest modelConfirmRequest;
    Model model;
    private GoogleMap googleMap;

    //endregion

    //region views

    @BindView(R.id.tvConfirmRequestPrice)
    TextView tvConfirmRequestPrice;
    @BindView(R.id.tvConfirmRequestDate)
    TextView tvConfirmRequestDate;
    @BindView(R.id.tvConfirmRequestWithName)
    TextView tvConfirmRequestWithName;
    @BindView(R.id.tvConfirmRequestStreet)
    TextView tvConfirmRequestStreet;
    @BindView(R.id.tvConfirmRequestArea)
    TextView tvConfirmRequestArea;
    @BindView(R.id.mvConfirmRequest)
    MapView mvConfirmRequest;

    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);
        ButterKnife.bind(this);
        Intent intent = getIntent();
//        modelConfirmRequest = (ModelConfirmRequest) intent.getSerializableExtra(DataEnum.intentModelConfirmRequest.name());
        model = (Model) intent.getSerializableExtra(DataEnum.intentModel.name());
        adjustView();
        initMap(savedInstanceState);
    }

    //endregion

    //region clicks

    @OnClick(R.id.btnConfirmRequestBackToHome)
    void onClickbtnConfirmRequestBackToHome(View view)
    {
        startActivity(new Intent(this, SeekerMapsActivity.class));
    }

    @OnClick(R.id.btnConfirmRequestShowFullReceipt)
    void onClickbtnConfirmRequestShowFullReceipt(View view)
    {

    }
    //endregion

    //region functions

    private void adjustView()
    {
        tvConfirmRequestPrice.setText("E£ " + model.getFees());
        tvConfirmRequestDate.setText(model.getLeavingtime());
        tvConfirmRequestWithName.setText(getString(R.string.exchanged_parking_with_s, model.getLeavername()));
        tvConfirmRequestStreet.setText(model.getAddress());
        tvConfirmRequestArea.setText(model.getAddress());
    }
    //endregion

    //region map
    protected void initMap(Bundle savedInstanceState)
    {
        mvConfirmRequest.onCreate(savedInstanceState);

        try
        {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e)
        {
            Log.e("mapError", e.getMessage());
        }

        mvConfirmRequest.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.googleMap = map;

        LatLng latLng = new LatLng(model.getLatitude(), model.getLongitude());
        adjustMapLatLng(latLng);
    }


    private void adjustMapLatLng(@android.support.annotation.NonNull LatLng latLng)
    {
        if (googleMap != null)
        {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(model.getFees() + "", false))));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 11));

        }
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

        } catch (Exception e)
        {
            return null;
        }
    }

    //endregion

}
