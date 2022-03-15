package com.yunling.qqprefixtails;

import android.app.Application;
import android.content.Context;

import java.time.chrono.MinguoChronology;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    public static final String QQ = "com.tencent.mobileqq";
    public static final int QQ810Version = 1232;

    private boolean isHook = false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
//        XposedBridge.log("enter MainHook.java line 19");
//        XposedBridge.log(loadPackageParam.packageName);
        //hook自己方法，判断是否激活
        if (loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) {
//            XposedBridge.log("-----hook myself success");
//            XposedBridge.log("-----" + MainActivity.class.getName());

            XposedHelpers.findAndHookMethod("com.yunling.qqprefixtails.MainActivity", loadPackageParam.classLoader,
                    "getStatus", XC_MethodReplacement.returnConstant(true));

        }
//        Context context = MainActivity.this;
        if (loadPackageParam.packageName.equals(QQ)) {
//            XposedBridge.log("-----hook QQ success");
            XposedHelpers.findAndHookMethod(
                    XposedHelpers.findClass(loadPackageParam.appInfo.className, loadPackageParam.classLoader).getSuperclass(),
                    "onCreate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            if (!isHook) {
                                isHook = true;//防止多次hook
                                Application application = (Application) param.thisObject;

                                new MsgHook(application.getClassLoader(), application.getApplicationContext()).init();
                                new SettingHook(application.getClassLoader(),application.getApplicationContext()).init();
                            }
                        }
                    }
            );
        }
//        XposedBridge.log("MainHook.java end");
    }
}
