package com.yunling.qqprefixtails;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class SettingUtils {
    /**
     * 随机前缀数据
     */
    public static final String setting_key_randomPrefix = "randomPrefix";

    /**
     * 随机后缀数据
     */
    public static final String setting_key_randomSubfix = "randomSubfix";

    /**
     * 开启随机前缀
     */
    public static final String setting_key_openRandomPrefix = "openRandomPrefix";

    /**
     * 开启随机后缀
     */
    public static final String setting_key_openRandomSubfix = "openRandomSubfix";

    /**
     * 自定义分隔符
     */
    public static final String setting_key_DIYdivide = "DIYdivide";


//    public static final String config_path = ""
//
//    private static final int MODE = Activity.MODE_WORLD_READABLE;
//
//    private static WeakReference<XSharedPreferences> a = new WeakReference(null);
//
//    private static XSharedPreferences getPref() {
//        XSharedPreferences xSharedPreferences = (XSharedPreferences)  a.get();
//        if (xSharedPreferences == null)
//        {
//            xSharedPreferences = new XSharedPreferences(BuildConfig.APPLICATION_ID,BuildConfig.APPLICATION_ID);
//            xSharedPreferences.makeWorldReadable();
//            a = new WeakReference(xSharedPreferences);
//            return xSharedPreferences;
//        }
//        xSharedPreferences.reload();
//        return xSharedPreferences;

//        XSharedPreferences preferences = xSharedPreferences.get();
//        if (preferences == null) {
//            preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID);
//            preferences.makeWorldReadable();
//            xSharedPreferences = new WeakReference<>(preferences);
//        } else {
//            preferences.reload();
//        }
//        return preferences;
//    }

    //    public void getTest(Context context) {
//    getPref().
//
//    getFile()

    //        Toast.makeText(context, getPref().getFile().getPath(), Toast.LENGTH_LONG).show();
//    }
//
//    public boolean getBoolean(String key) {
//        return getPref().getBoolean(key, false);
//    }
//
//    public int getInt(String key) {
//        return getPref().getInt(key, 0);
//    }
//
//    public String getString(String key) {
//        return getPref().getString(key, "");
//    }
//
//    public String getString(String key, String defealtValue) {
//
//        return getPref().getString(key, defealtValue);
//    }
//
//    public float getFontSize() {
//
//        return Float.valueOf(getString("fontSize"));
//    }
//
    public static boolean getBoolean(String key) {
        String value = ProperUtil.getConfigProperties(key);
        return value.equals("true");
//        if (context != null && key != null) {
//            return context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).getBoolean(key, false);
//        }
//        return false;
    }

    public static String getString(String key) {
        return ProperUtil.getConfigProperties(key);
//        if (context != null && key != null) {
//            return context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).getString(key, "");
//        }
//        return "";
    }

    public static int getInt(String key) {
        String value = ProperUtil.getConfigProperties(key);
        return Integer.valueOf(value);
//        if (context != null && key != null) {
//            return context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).getInt(key, 0);
//        }
//        return 0;
    }

    public static void setBoolean(String key, boolean newValue) {
        ProperUtil.writeDateToLocalFile(key, newValue ? "true" : "false");
//        if (context != null && key != null) {
//            context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).edit().putBoolean(key, newValue).apply();
//        }
    }

    public static void setString(String key, String newValue) {
        ProperUtil.writeDateToLocalFile(key, newValue);
//        if (context != null && key != null) {
//            context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).edit().putString(key, newValue).apply();
//        }
    }

    public static void setInt(String key, int Value) {
        ProperUtil.writeDateToLocalFile(key, Value + "");
//        if (context != null && key != null) {
//            context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE).edit().putInt(key, Value).apply();
//        }
    }

}
