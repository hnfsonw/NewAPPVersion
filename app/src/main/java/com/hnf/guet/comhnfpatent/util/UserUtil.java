package com.hnf.guet.comhnfpatent.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {

    /** 检测手机号是否有效 **/
    public static boolean checkPhone(String phone) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(phone);
        b = m.matches();
        return b;
    }


    /**
     * 隱藏软键盘
     */
    public static void hideSoftInput(Activity mContext) {
        try {
            ((InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mContext.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Activity mContext) {
        try {
            ((InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断手机号码是否合理
     *y
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }


    /**
     * 判断密码是否合理   6-16位字母加数字
     */
    public static boolean judgePassword(String password){
        String telRegex = "(?=.*[0-9])(?=.*[a-z]).{6,16}";
        if (TextUtils.isEmpty(password)){
            return false;
        }else {
            return password.matches(telRegex);
        }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }



    public static boolean checkChEn(String ch_en){
        String regex = "^[\u4e00-\u9fa5_a-zA-Z0-9]+$";
        return Pattern.matches(regex, ch_en);
    }


}
