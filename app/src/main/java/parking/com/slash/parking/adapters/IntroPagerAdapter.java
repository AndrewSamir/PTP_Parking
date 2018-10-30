package parking.com.slash.parking.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import parking.com.slash.parking.R;
import parking.com.slash.parking.customViews.IntroAdapterItem;
import parking.com.slash.parking.interfaces.IntroInterface;

/**
 * Created by M.Baraka on 17-Oct-17.
 */

public class IntroPagerAdapter extends PagerAdapter
{
    private Context context;
    private IntroInterface introInterface;

    public IntroPagerAdapter(Context context, IntroInterface introInterface)
    {
        this.context = context;
        this.introInterface = introInterface;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
        IntroAdapterItem introAdapterItem = new IntroAdapterItem(context, introInterface);
        if (position == 0)
        {
            introAdapterItem.setData(R.drawable.all_onboarding1);
        } else if (position == 1)
        {
            introAdapterItem.setData(R.drawable.all_onboarding2);
        } else if (position == 2)
        {
            introAdapterItem.setData(R.drawable.all_onboarding3);
        }
        collection.addView(introAdapterItem);
        return introAdapterItem;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        collection.removeView((View) view);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }


}
