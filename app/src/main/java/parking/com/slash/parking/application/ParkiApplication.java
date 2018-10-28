package parking.com.slash.parking.application;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


import parking.com.slash.parking.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ParkiApplication extends MultiDexApplication
{
    Configuration config;
    Locale locale;

    @Override
    public void onCreate()
    {
        super.onCreate();

        initLang();

//        Fabric.with(this, new Crashlytics());
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        Twitter.initialize(this);
//        Log.e("h", "ad");
//          printKeyHash();
//        printKeyHash();
//        initFont();

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }


    private void initFont()
    {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/TheSansArabic-Plain.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void initLang()
    {

        //  String lang = SharedPrefUtil.getInstance(getApplicationContext()).read("settingLangName", "en");
        String lang = "en";
        locale = new Locale(lang);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void printKeyHash()
    {
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("VIVZ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e)
        {

        } catch (NoSuchAlgorithmException e)
        {

        }
    }

   /*
    public Socket getSocket() {
        return mSocket;
    }
    */
}
