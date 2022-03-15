package com.yunling.qqprefixtails;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SettingHook {
    private ClassLoader classLoader;
    private Class<?> mQQSetting;
    private Class<?> mFormSwitchItem;
    private Class<?> mFormSimpleItem;
    private MainActivity mainActivity;
    private Context context;
    private boolean isAdd = false;

    public SettingHook(ClassLoader classLoader, Context context) throws ClassNotFoundException {
        this.classLoader = classLoader;
        mQQSetting = classLoader.loadClass("com.tencent.mobileqq.activity.QQSettingSettingActivity");
        mFormSwitchItem = classLoader.loadClass("com.tencent.mobileqq.widget.FormSwitchItem");
//        this.mainActivity = mainActivity;
        this.context = context;
        mFormSimpleItem = classLoader.loadClass("com.tencent.mobileqq.widget.FormSimpleItem");
    }

    public void init() {
        show();
    }

    public static String getApplicationNameByPackageName(Context context, String packageName)
    {
        PackageManager pm = context.getPackageManager();
        String Name;
        try {
            Name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Name = "";
        }
        return Name;
    }

    private void show() {
        if (isAdd) return;
        isAdd = true;
        String old_packet = "com.tencent.mobileqq.activity.QQSettingSettingActivity";
        String new_packet = "com.tencent.mobileqq.activity.QQSettingSettingActivity";
        final String myName = getApplicationNameByPackageName(context,BuildConfig.APPLICATION_ID);
        XposedHelpers.findAndHookMethod(mQQSetting, "doOnCreate", Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        View SimpleItem = null;
                        Object QQview = getObjectField(param.thisObject, "a", "FormSimpleItem");
                        if (QQview == null) {
                            Object TimView = getObjectField(param.thisObject, "a", "FormCommonSingleLineItem");
                            SimpleItem = (View) TimView;
                        } else {
                            SimpleItem = (View) QQview;
                        }
                        final Context context = SimpleItem.getContext();
                        final Object formSimpleItem = SimpleItem.getClass().getConstructor(Context.class).newInstance(context);
                        XposedHelpers.callMethod(formSimpleItem, "setLeftText", myName);
                        XposedHelpers.callMethod(formSimpleItem, "setRightText", BuildConfig.VERSION_NAME);
                        final LinearLayout linearLayout = (LinearLayout) SimpleItem.getParent();
                        linearLayout.addView((View) formSimpleItem, 0);
                        ((View) formSimpleItem).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.showAboutItem(view.getContext(),true);
                            }
                        });
                    }
                });
        XposedHelpers.findAndHookMethod(mQQSetting, "doOnDestroy", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                isAdd = false;
            }
        });
    }

    public Object getObjectField(Object o, String fieldName, String type) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(fieldName) && field.getGenericType().toString().contains(type)) {
                try {
                    return field.get(o);
                } catch (Exception e) {
                    XposedBridge.log(e);
                    return null;
                }
            }
        }
        return null;
    }

}
