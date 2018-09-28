package parking.com.slash.parking.utlities;

import android.util.Log;

import parking.com.slash.parking.BuildConfig;


/**
 * Created by andre on 17-Jan-18.
 */

public class LoggerHelper
{

    public void i(String tag, String message)
    {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    public void d(String tag, String message)
    {
        if (BuildConfig.DEBUG)
            Log.d(tag, message);
    }

    public void e(String tag, String message)
    {
        if (BuildConfig.DEBUG)
            Log.e(tag, message);
    }

    public void e(Throwable throwable)
    {

        if (BuildConfig.DEBUG)
        {
            Log.e("Logger", ">>>>>> Crash happened Check it!! <<<<<<");
            throwable.printStackTrace();
        }
    }

}
