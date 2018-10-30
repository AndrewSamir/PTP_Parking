package parking.com.slash.parking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;

import com.bikomobile.circleindicatorpager.CircleIndicatorPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.Account.LoginActivity;
import parking.com.slash.parking.activities.Account.SignUpAccountDetailsActivity;
import parking.com.slash.parking.adapters.IntroPagerAdapter;
import parking.com.slash.parking.interfaces.IntroInterface;

public class IntroActivity extends Activity implements IntroInterface, ViewPager.OnPageChangeListener {

    @BindView(R.id.introFragment_viewPager)
    ViewPager viewPager;

    @BindView(R.id.introFragment_circleIndicator)
    CircleIndicatorPager circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this);


        viewPager.setAdapter(new IntroPagerAdapter(this, this));
        circleIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onViewClicked(int position) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    //region clicks
/*
    @OnClick(R.id.btnIntroLogin)
    public void onClickbtnIntroLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
*/
    @OnClick(R.id.tvIntroSignIn)
    public void onClickbtnIntroSignUp() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //endregion

}
