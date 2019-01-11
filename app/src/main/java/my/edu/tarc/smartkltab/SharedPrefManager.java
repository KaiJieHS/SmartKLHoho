package my.edu.tarc.smartkltab;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARE_PREF_NAME = "sharepref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NO = "phoneNo";
    private static final String KEY_EMAIL = "email";

    private SharedPrefManager(Context context){
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance ==null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
