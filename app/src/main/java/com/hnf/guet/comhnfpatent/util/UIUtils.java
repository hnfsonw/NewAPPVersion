package com.hnf.guet.comhnfpatent.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;


import com.hnf.guet.comhnfpatent.base.MyApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 类    名:  UIUtils
 * 描    述： 和ui相关的工具类
 */
public class UIUtils {

    private static File mFile;
    public static double pi = 3.141592653589793 * 3000.0 / 180.0;

    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.sInstance;
    }


    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }


    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }


    /**
     * 得到String.xml中字符串数组
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }


    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }


    /**
     * dip-->px
     *
     * @param dip
     * @return
     */
    public static int dp2px(int dip) {
        //dip和px的转换关系
        //1. px/(ppi/160) = dp
        //2  px/dip = density
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (density * dip + .5f);
        return px;
    }


    /**
     * 获取版本信息
     * @return
     */
    public static String getVersion(){
        PackageManager packageManager = getContext().getPackageManager();
        String version = "";
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            version = versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode(){
        PackageManager packageManager = getContext().getPackageManager();
        int version = 1;
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = info.versionCode;
            version = versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }



    /**
     * px-->dip
     *
     * @param px
     * @return
     */
    public static int px2Dip(int px) {
        //dip和px的转换关系
        //1. px/(ppi/160) = dp
        //2  px/dip = density
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px/density+.5f);
        return dip;
    }


    /**
     * 是否存在sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将bitmap保存file路径
     * @param bitmap
     */
    public static void saveBitmapFile(Bitmap bitmap) {
        File filesDir = MyApplication.sInstance.getFilesDir();
        Log.d("BabyDataActivity", "saveBitmapFile: " + filesDir);
        //将要保存图片的路径
        mFile = new File(String.valueOf(filesDir)+"/01.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {

        }
    }


    /**
     * 获取存储图片的路径
     * @return
     */
    public static File getBitmapFile(){
        return mFile;
    }


    /**
     * 获取活动网路信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * 判断网络是否可用
     * <p>需添加权限 android.permission.ACCESS_NETWORK_STATE</p>
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }





    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

}
