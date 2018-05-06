package com.hnf.guet.comhnfpatent.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/23.
 */

public class IOUtils  {
    //关闭流
    public static boolean close(Closeable io){
        if (io != null){
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
