package parking.com.slash.parking.adapters;

/**
 * Created by ksi on 19-Jul-17.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
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
import java.util.List;
import java.util.Locale;

import parking.com.slash.parking.R;
import parking.com.slash.parking.model.ModelHistory.Model;
import parking.com.slash.parking.model.ModelHistory.ModelHistory;


/**
 * Created by andre on 07-May-17.
 */

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder> {

    private List<Model> adapterList;
    private Activity activity;
    private GoogleMap mMap;


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView tvRvItemHistoryTime, tvRvItemHistoryName, tvRvItemHistoryStreet, tvRvItemHistoryArea, tvRvItemHistoryPrice,
                tvRvItemHistoryState;
        RoundedImageView imgRvItemHistory;

        MapView mvRvItemHistory;

        public MyViewHolder(View view) {
            super(view);

            mvRvItemHistory = view.findViewById(R.id.mvRvItemHistory);
            imgRvItemHistory = view.findViewById(R.id.imgRvItemHistory);
            tvRvItemHistoryTime = view.findViewById(R.id.tvRvItemHistoryTime);
            tvRvItemHistoryName = view.findViewById(R.id.tvRvItemHistoryName);
            tvRvItemHistoryStreet = view.findViewById(R.id.tvRvItemHistoryStreet);
            tvRvItemHistoryArea = view.findViewById(R.id.tvRvItemHistoryArea);
            tvRvItemHistoryPrice = view.findViewById(R.id.tvRvItemHistoryPrice);
            tvRvItemHistoryState = view.findViewById(R.id.tvRvItemHistoryState);

//            initMap(mvRvItemHistory);

        }

        public void initializeMapView() {
            if (mvRvItemHistory != null) {
                mvRvItemHistory.onCreate(null);
                mvRvItemHistory.onResume();
                mvRvItemHistory.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(activity.getApplicationContext());
            mMap = googleMap;

            Double lat = Double.parseDouble(adapterList.get(getAdapterPosition()).getLatitude());
            Double lng = Double.parseDouble(adapterList.get(getAdapterPosition()).getLongitude());
            LatLng latLng = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(latLng).title("test"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        }
    }

    public AdapterHistory(List<Model> adapterList, Activity activity) {
        this.adapterList = adapterList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Model model = adapterList.get(position);
        Date newDate = new Date(), newTime = new Date();
        String[] arrTimeSt = model.getRequestdate().split("T");
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            newDate = format.parse(arrTimeSt[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat newFormatDate = new java.text.SimpleDateFormat("EEEE , d MMMM yyyy", Locale.ENGLISH);
        String date = newFormatDate.format(newDate);

        DateFormat format_ = new java.text.SimpleDateFormat("kk:mm:ss.SSS", Locale.ENGLISH);
        try {
            newTime = format_.parse(arrTimeSt[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat newFormatTime = new java.text.SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String time = newFormatTime.format(newTime);


        holder.tvRvItemHistoryTime.setText(date + " - " + time);
        holder.tvRvItemHistoryName.setText(model.getName());
        holder.tvRvItemHistoryStreet.setText(model.getAddress());
        holder.tvRvItemHistoryArea.setText(model.getArea());
        holder.tvRvItemHistoryPrice.setText(model.getFees() + "LE");
        holder.tvRvItemHistoryState.setText(model.getType() + "");

        if (model.getCarimage() != null)
            Picasso.with(activity)
                    .load(model.getCarimage())
                    .into(holder.imgRvItemHistory);

        holder.initializeMapView();

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }


    //region helper methods

    public void addItem(Model item) {
        insertItem(item, adapterList.size());
        notifyDataSetChanged();
    }

    public void insertItem(Model item, int position) {
        adapterList.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        adapterList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAllListData() {
        int size = adapterList.size();
        adapterList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Model> items) {
        int startIndex = adapterList.size();
        adapterList.addAll(items);
        notifyItemRangeInserted(startIndex, items.size());
    }

    public List<Model> getAllData() {
        return adapterList;
    }


    //endregion


}

