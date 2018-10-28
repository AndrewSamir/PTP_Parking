package parking.com.slash.parking.adapters;

/**
 * Created by ksi on 19-Jul-17.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import parking.com.slash.parking.R;
import parking.com.slash.parking.model.ModelHistory.ModelHistory;


/**
 * Created by andre on 07-May-17.
 */

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder> {

    private List<ModelHistory> adapterList;
    private Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
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


        }


    }

    public AdapterHistory(List<ModelHistory> adapterList, Activity activity) {
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
        holder.tvRvItemHistoryTime.setText("");
        holder.tvRvItemHistoryName.setText("");
        holder.tvRvItemHistoryStreet.setText("");
        holder.tvRvItemHistoryArea.setText("");
        holder.tvRvItemHistoryPrice.setText("");
        holder.tvRvItemHistoryState.setText("");

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }


    //region helper methods

    public void addItem(ModelHistory item) {
        insertItem(item, adapterList.size());
        notifyDataSetChanged();
    }

    public void insertItem(ModelHistory item, int position) {
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

    public void addAll(List<ModelHistory> items) {
        int startIndex = adapterList.size();
        adapterList.addAll(items);
        notifyItemRangeInserted(startIndex, items.size());
    }

    public List<ModelHistory> getAllData() {
        return adapterList;
    }


    //endregion


}

