package parking.com.slash.parking.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by M.Baraka on 31-Oct-17.
 */

public interface OnRequestMoreListener
{
    void requestMoreData(final RecyclerView.Adapter adapter, final int position);
}
