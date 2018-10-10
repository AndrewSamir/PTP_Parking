package parking.com.slash.parking.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import parking.com.slash.parking.activities.Account.LoginActivity;
import parking.com.slash.parking.activities.Account.SignUpAccountDetailsActivity;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
