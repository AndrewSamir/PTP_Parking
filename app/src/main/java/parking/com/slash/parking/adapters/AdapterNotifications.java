package parking.com.slash.parking.adapters;

/**
 * Created by ksi on 19-Jul-17.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import parking.com.slash.parking.model.ModelNotifications.Model;


/**
 * Created by andre on 07-May-17.
 */

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyViewHolder> {

    private List<Model> adapterList;
    private Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRvItemNotificationTxt, tvRvItemNotificationTime;
        ImageView imgRvItemNotificationRemove;


        public MyViewHolder(View view) {
            super(view);

            tvRvItemNotificationTxt = view.findViewById(R.id.tvRvItemNotificationTxt);
            tvRvItemNotificationTime = view.findViewById(R.id.tvRvItemNotificationTime);
            imgRvItemNotificationRemove = view.findViewById(R.id.imgRvItemNotificationRemove);

            imgRvItemNotificationRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition());
        }
    }

    public AdapterNotifications(List<Model> adapterList, Activity activity) {
        this.adapterList = adapterList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Model model = adapterList.get(position);

        Date newDate = new Date(), newTime = new Date();
        String[] arrTimeSt = model.getNotificationdate().split("T");
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

        holder.tvRvItemNotificationTxt.setText(model.getNotificationtitle());
        holder.tvRvItemNotificationTime.setText(time);

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

