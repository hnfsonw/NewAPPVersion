package com.hnf.guet.comhnfpatent.util;

/**
 * Created by Administrator on 2018/3/24.
 */

public class StringUtils {

    public static boolean isEmpty(String value){
        if (value != null&&!value.trim().equals("")&&value.trim().equals("null")){
            return false;
        }else {
            return true;
        }
    }



}
