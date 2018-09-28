package parking.com.slash.parking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.Account.LoginActivity;
import parking.com.slash.parking.activities.Account.SignUpAccountDetailsActivity;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this);
    }


    //region clicks

    @OnClick(R.id.btnIntroLogin)
    public void onClickbtnIntroLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btnIntroSignup)
    public void onClickbtnIntroSignUp() {
        startActivity(new Intent(this, SignUpAccountDetailsActivity.class));
        finish();
    }

    //endregion

}
