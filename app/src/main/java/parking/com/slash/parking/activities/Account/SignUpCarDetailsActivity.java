package parking.com.slash.parking.activities.Account;

import android.os.Bundle;
import android.app.Activity;

import parking.com.slash.parking.R;


public class SignUpCarDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_car_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
