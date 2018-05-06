package com.hnf.guet.comhnfpatent.model;

import com.hnf.guet.comhnfpatent.model.bean.ResultBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/29.
 */

public class ResponeModelInfo implements Serializable {
    private String resultMsg;
    private int resultCode;
    private int errorCode;
    private long time;
    public ResultBean result;

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

}
