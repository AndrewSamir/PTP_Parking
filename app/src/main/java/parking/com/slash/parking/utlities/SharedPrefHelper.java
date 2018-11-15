package parking.com.slash.parking.utlities;

import android.content.Context;
import android.content.SharedPreferences;

import parking.com.slash.parking.model.ModelLoginResponse.Model;
import parking.com.slash.parking.model.ModelLoginResponse.ModelLoginResponse;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andre on 22-Jan-18.
 */

public class SharedPrefHelper {
    private static Context context;
    private static SharedPrefHelper instance = null;
    private static SharedPreferences prefs = null;
    private static SharedPreferences.Editor editor;

    public static SharedPrefHelper getInstance(Context context) {
        SharedPrefHelper.context = context;

        if (instance == null) {
            instance = new SharedPrefHelper();
            SharedPrefHelper.prefs = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        }
        return instance;
    }


    public void setUser(Model modelLoginResponse) {
        editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString(DataEnum.shUserID.name(), modelLoginResponse.getUserid());
        editor.putString(DataEnum.shAccessToken.name(), modelLoginResponse.getAccesstoken());
//        editor.putString(DataEnum.shProfileIdentifier.name(), modelLoginResponse.getProfileidentifier());
        editor.putString(DataEnum.shUserName.name(), modelLoginResponse.getUsername());
//        editor.putString(DataEnum.shFullName.name(), modelLoginResponse.getFullname());
//        editor.putString(DataEnum.shBio.name(), modelLoginResponse.getBio());
        editor.putString(DataEnum.shMobile.name(), modelLoginResponse.getMobile());
//        editor.putBoolean(DataEnum.shIsVerified.name(), modelLoginResponse.getIsverified());
//        editor.putString(DataEnum.shImageLocation.name(), modelLoginResponse.getImagelocation());
//        editor.putInt(DataEnum.shCountryID.name(), modelLoginResponse.getCountryid());
//        editor.putString(DataEnum.shCountryName.name(), modelLoginResponse.getCountryname());
//        editor.putInt(DataEnum.shFollowersCount.name(), modelLoginResponse.getFollowerscount());
//        editor.putInt(DataEnum.shFollowingCount.name(), modelLoginResponse.getFollowingcount());
//        editor.putInt(DataEnum.shSharesCount.name(), modelLoginResponse.getSharescount());

        editor.apply();
    }

    public String getAccessToken() {
        return prefs.getString(DataEnum.shAccessToken.name(), null);
    }


    public void shSignOut() {
        editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.remove(DataEnum.shUserID.name());
        editor.remove(DataEnum.shAccessToken.name());
        editor.remove(DataEnum.shUserName.name());
        editor.remove(DataEnum.shMobile.name());
        editor.apply();
    }
}
