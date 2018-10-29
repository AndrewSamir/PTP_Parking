package parking.com.slash.parking.customViews;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import parking.com.slash.parking.R;
import parking.com.slash.parking.interfaces.IntroInterface;


/**
 * Created by M.Baraka on 17-Oct-17.
 */

public class IntroAdapterItem extends FrameLayout
{
    private ImageView imageView;
    IntroInterface introInterface;

    public IntroAdapterItem(@NonNull Context context, IntroInterface introInterface)
    {
        super(context);
        init(context);
        this.introInterface = introInterface;
    }

    public IntroAdapterItem(@NonNull Context context, @Nullable AttributeSet attrs, IntroInterface introInterface)
    {
        super(context, attrs);
        init(context);
        this.introInterface = introInterface;

    }

    public IntroAdapterItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, IntroInterface introInterface)
    {
        super(context, attrs, defStyleAttr);
        init(context);
        this.introInterface = introInterface;

    }

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.intro_adapter_item, this, true);
       /* txt_label = findViewById(R.id.introAdapter_txt_label);
        txt_info = findViewById(R.id.introAdapter_txt_info);*/
        imageView = findViewById(R.id.introAdapter_img);


    }


    public void setData(@DrawableRes int drawableRes)
    {
        imageView.setImageResource(drawableRes);
    }
}
