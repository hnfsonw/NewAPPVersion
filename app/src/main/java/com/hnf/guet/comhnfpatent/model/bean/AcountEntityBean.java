package com.hnf.guet.comhnfpatent.model.bean;

import java.io.Serializable;

public class AcountEntityBean implements Serializable{
    private int acountId;
    private String acountName;
    private String createTime;
    private String password;
    private String nickName;
    private String phone;
    private String imgUrl;
    private String lastLongTime;
    private int acountType;
    private String job;
    private String workExprience;
    private String goodAt;
    private String infomation;

    public String getWorkExprience() {
        return workExprience;
    }

    public void setWorkExprience(String workExprience) {
        this.workExprience = workExprience;
    }

    public int getAcountType() {
        return acountType;
    }

    public void setAcountType(int acountType) {
        this.acountType = acountType;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public int getAcountId() {
        return acountId;
    }

    public void setAcountId(int acountId) {
        this.acountId = acountId;
    }

    public String getAcountName() {
        return acountName;
    }

    public void setAcountName(String acountName) {
        this.acountName = acountName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLastLongTime() {
        return lastLongTime;
    }

    public void setLastLongTime(String lastLongTime) {
        this.lastLongTime = lastLongTime;
    }
}
