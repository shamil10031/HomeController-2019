package shomazzapp.com.homecontorl.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class PreferencesHelper {

    public static final String APP_PREFERENCES = "cache";
    public static final String KEY_LOGIN = "login";

    public void putString(String key, String value, Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Nullable
    public String getString(String key, Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

}
