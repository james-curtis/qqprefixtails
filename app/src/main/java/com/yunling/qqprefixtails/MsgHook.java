package com.yunling.qqprefixtails;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;


public class MsgHook {
    private ClassLoader classLoader;
    private Context context;
//    private SettingUtils settingUtils = new SettingUtils();
    private MainActivity mainActivity;

    //    private Class<?>mQQAppInterface;
//    private Class<?>mMessageInfo;
//    private Class<?>mPttItemBuilder;
//    private Class<?>mMessageForPtt;
//    private Class<?>mChatActivityFacade;
//    private Class<?>mQQCustomMenu;
    private Class<?> mBaseChatPie;
//    private Class<?> mXEditTextEx;
//    private TroopAndFriendSelectAdpter troopAndFriendSelectAdpter;
//    private ClassLoader classLoader;


    public MsgHook(ClassLoader classLoader, Context context/*,MainActivity mainActivity*/) throws ClassNotFoundException {
        this.classLoader = classLoader;
        this.context = context;
//        this.mainActivity = mainActivity;
//        mQQAppInterface=classLoader.loadClass("com.tencent.mobileqq.app.QQAppInterface");
//        mMessageInfo=classLoader.loadClass("com.tencent.mobileqq.troop.data.MessageInfo");
//        mPttItemBuilder=classLoader.loadClass("com.tencent.mobileqq.activity.aio.item.PttItemBuilder");
//        mMessageForPtt=classLoader.loadClass(MessageForPtt);
//        mChatActivityFacade=classLoader.loadClass(ChatActivityFacade);
//        mQQCustomMenu=classLoader.loadClass(QQCustomMenu);
//        mXEditTextEx=classLoader.loadClass(XEditTextEx);
        mBaseChatPie = XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", this.classLoader);
        this.classLoader = classLoader;
    }

    public void init()
    {
        try {
            show();
        }
        catch (Exception e)
        {
            XposedBridge.log(e);
        }
    }

    /**
     * 获取版本号
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    private void show() throws PackageManager.NameNotFoundException {
        /*if (!settingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix) &&
                !settingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix)
        ) return;*/
        String old_packet = "com.tencent.mobileqq.activity.ChatActivityFacade$SendMsgParams";
        String new_packet = "aaos";
        String packet = new_packet;
//        final String subfix = getSubfix(settingUtils.getString(SettingUtils.setting_key_randomSubfix));
//        XposedBridge.log("******" + subfix);
//
//        final String prefix = getPrefix(settingUtils.getString(SettingUtils.setting_key_randomPrefix));
//        String prefix = getPrefix(settingUtils.getString(SettingUtils.setting_key_randomPrefix, ""));
        Context context_qq = context.createPackageContext(MainHook.QQ,CONTEXT_IGNORE_SECURITY);
        if (getVersionCode(context_qq) < MainHook.QQ810Version)
        {
            packet = old_packet;
        }
        XposedHelpers.findAndHookMethod(mBaseChatPie, "a", String.class, packet, ArrayList.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                context.getSharedPreferences()

                String subfix = getSubfix(SettingUtils.getString(SettingUtils.setting_key_randomSubfix));
                String prefix = getPrefix(SettingUtils.getString(SettingUtils.setting_key_randomPrefix));
                String msg = param.args[0].toString();
                param.args[0] = prefix + msg + subfix;
//                Toast.makeText(context,prefix,Toast.LENGTH_SHORT).show();
//                settingUtils.getTest(context);
            }
        });
    }

    private String getSubfix(String msg) {
        if (!msg.equals("")) {
            if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix)) {
                String[] msgs = msg.split(getDivide());
                int num = msgs.length > 1 ? new Random().nextInt(msgs.length) : 0;
                return msgs[num];
            } else {
                return msg;
            }
        }
        ;
        return "";
    }

    private String getPrefix(String msg) {
        if (!msg.equals("")) {
            if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomPrefix)) {
                String[] msgs = msg.split(getDivide());
                int num = msgs.length > 1 ? new Random().nextInt(msgs.length) : 0;
                return msgs[num];
            } else {
                return msg;
            }
        }
        ;
        return "";
    }

    private String getDivide()
    {
        return SettingUtils.getString(SettingUtils.setting_key_DIYdivide);
    }
}
