package com.yunling.qqprefixtails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class MainActivity extends AppCompatActivity {
    private static final String qun = "677806860";
    private SettingUtils settingUtils = new SettingUtils();

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"请给写入权限，否则无法保存配置文件",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE , REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"请给读出权限，否则无法保存配置文件",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE , REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //申请权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE , REQUEST_PERMISSION_CODE);
        }


        TextView textView = (TextView) findViewById(R.id.text);
        final EditText editText_prefix = (EditText) findViewById(R.id.editText_prefix);
        final EditText editText_subfix = (EditText) findViewById(R.id.editText_subfix);
        final EditText editText_DIYdivde = (EditText) findViewById(R.id.editText_DIYdivde);
        editText_prefix.setHorizontallyScrolling(true);//不让超出屏幕的文本自动换行，使用滚动条
        editText_subfix.setHorizontallyScrolling(true);//不让超出屏幕的文本自动换行，使用滚动条
        if (getStatus()) {
            textView.setText("已激活");
        } else {
            textView.setText("未激活");
        }

        editText_prefix.setText(SettingUtils.getString(SettingUtils.setting_key_randomPrefix));
        editText_subfix.setText(SettingUtils.getString(SettingUtils.setting_key_randomSubfix));
        editText_DIYdivde.setText(SettingUtils.getString(SettingUtils.setting_key_DIYdivide));


        final Button button_openRandomPrefix = (Button) findViewById(R.id.button_openRandomPrefix);
        final Button button_openRandomSubfix = (Button) findViewById(R.id.button_openRandomSubfix);
        Button button_reload = (Button) findViewById(R.id.button_reload);
        Button button_save = (Button) findViewById(R.id.button_save);


        //随机前缀
        if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomPrefix)) {
            button_openRandomPrefix.setText("关闭随机前缀(当前:开启)");
        } else {
            button_openRandomPrefix.setText("开启随机前缀(当前:关闭)");
        }
        button_openRandomPrefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingUtils.setBoolean(SettingUtils.setting_key_openRandomPrefix,
                        !SettingUtils.getBoolean(SettingUtils.setting_key_openRandomPrefix));
                if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomPrefix)) {
                    button_openRandomPrefix.setText("关闭随机前缀(当前:开启)");
                } else {
                    button_openRandomPrefix.setText("开启随机前缀(当前:关闭)");
                }
                Toast.makeText(MainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            }
        });


        //随机后缀
        if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix)) {
            button_openRandomSubfix.setText("关闭随机后缀(当前:开启)");
        } else {
            button_openRandomSubfix.setText("开启随机后缀(当前:关闭)");
        }
        button_openRandomSubfix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingUtils.setBoolean(SettingUtils.setting_key_openRandomSubfix,
                        !SettingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix));
                if (SettingUtils.getBoolean(SettingUtils.setting_key_openRandomSubfix)) {
                    button_openRandomSubfix.setText("关闭随机前缀(当前:开启)");
                } else {
                    button_openRandomSubfix.setText("开启随机前缀(当前:关闭)");
                }
                Toast.makeText(MainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            }
        });

        button_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_prefix.setText(SettingUtils.getString(SettingUtils.setting_key_randomPrefix));
                editText_subfix.setText(SettingUtils.getString(SettingUtils.setting_key_randomSubfix));
                editText_DIYdivde.setText(SettingUtils.getString(SettingUtils.setting_key_DIYdivide));

            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingUtils.setString(SettingUtils.setting_key_randomPrefix, editText_prefix.getText().toString());
                SettingUtils.setString(SettingUtils.setting_key_randomSubfix, editText_subfix.getText().toString());
                SettingUtils.setString(SettingUtils.setting_key_DIYdivide, editText_DIYdivde.getText().toString());
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        // 通过getMenuInflater()方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，
        // 第一个参数：用于指定我们通过哪一个资源文件来创建菜单；
        // 第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /****************
     *
     * 发起添加群流程。群号：云凌阁(677806860) 的 key 为： 5SnX3eUZY9FsUD99UYJDhCEPzC63iyhf
     * 调用 joinQQGroup(5SnX3eUZY9FsUD99UYJDhCEPzC63iyhf) 即可发起手Q客户端申请加群 云凌阁(677806860)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/

    /****************
     *
     * 发起添加群流程。群号：刷机屋(454090519) 的 key 为： g2DJw5gY5K3sEBQdW65zWKROR1Kq3VaV
     * 调用 joinQQGroup(g2DJw5gY5K3sEBQdW65zWKROR1Kq3VaV) 即可发起手Q客户端申请加群 刷机屋(454090519)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        String contextActivity_ = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return contextActivity_;
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    public static void showAboutItem(final Context context, boolean showExtra) {
//        String custom_app_name = getRunningActivityName(context);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialog.setTitle("关于");
        alertDialog.setMessage("QQ发消息添加随机前后缀，支持自定义分隔符\n" +
                "By：云凌工作室@leave\n" +
                "纯小白第一次开发Android应用，诶debug版本高达50+又苦于硬件资源不好，只能拿树莓派远控阿里抢占式实例写代码。\n" +
                "特别感谢 @祈无 大佬的指导！！！\n" +
                "QQ群：" + qun);
        alertDialog.setPositiveButton("确定", null);
//        Toast.makeText(context,custom_app_name,Toast.LENGTH_SHORT);
//        XposedBridge.log("66666---" + custom_app_name);
//        Toast.makeText(context,getAppProcessName(context),Toast.LENGTH_SHORT);
//        XposedBridge.log("77777-----" + getAppProcessName(context));
        if (showExtra) {
            alertDialog.setNegativeButton("打赏", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();

                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("targetUin", "1462066778");
                        jSONObject.put("targetNickname", "leave");
                        jSONObject.put("sign", "");
                        jSONObject.put("trans_fee", "");
                        jSONObject.put("source", "1");
                        jSONObject.put("desc", "");
                    } catch (Exception e) {
                        XposedBridge.log(e);
                    }

                    intent.setClassName(context, "com.tencent.mobileqq.activity.qwallet.TransactionActivity");
                    if (intent != null) {
                        intent.putExtra("come_from", 5);
                        intent.putExtra("extra_data", jSONObject.toString());
                        intent.putExtra("app_info", "appid#20000001|bargainor_id#1000026901|channel#wallet");
                        intent.putExtra("callbackSn", "0");
                        context.startActivity(intent);
                    }
                }
            });

            alertDialog.setNeutralButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //跳转到本应用
                    Context context1 = context;
                    Intent intent = context1.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
                    context1.startActivity(intent);
                }
            });
        }
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutItem(this, false);
                break;

            case R.id.menu_copy_qun:
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(qun, qun);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.enter_qun:
                joinQQGroup("5SnX3eUZY9FsUD99UYJDhCEPzC63iyhf");
                break;
        }
        return true;
    }

    private boolean getStatus() {
        return false;
    }
}
