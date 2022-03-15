package com.yunling.qqprefixtails;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ProperUtil {

    private static String FilePath = Environment.getExternalStorageDirectory().getPath() + "/.yunling/";
    private static String FileName = ".config.properties";

    private static void init()
    {
//        FilePath = Environment.getExternalStorageDirectory().getPath() + FilePath;
    }

    /**
     * 得到properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */
    public static String getConfigProperties(String key) {
        init();
        File mfile = new File(FilePath);
        if (!mfile.exists()) {
            mfile.mkdirs();
        }
        File mfileName = new File(FilePath + FileName);
        if (!mfileName.exists()) {
            return "";
        }
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(FilePath + FileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = props.getProperty(key);
        if (value == null) {
            value = "";
        }
        return value;
    }


    /**
     * 给属性文件添加属性
     *
     * @param value
     * @author qiulinhe
     * @createTime 2016年6月7日 下午1:46:53
     */
    public static void writeDateToLocalFile(String key, String value) {
        init();
        File mfile = new File(FilePath);
        if (!mfile.exists()) {
            boolean result = mfile.mkdirs();
            if (!result)
            {
                throw new RuntimeException("write error");
            }
        }
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(FilePath + FileName);
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.put(key, value);
        OutputStream fos;
        try {
            fos = new FileOutputStream(FilePath + FileName);
            p.store(fos, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}