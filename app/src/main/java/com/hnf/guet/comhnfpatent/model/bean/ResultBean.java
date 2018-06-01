package com.hnf.guet.comhnfpatent.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class ResultBean implements Serializable {
    private String acountName;
    private String token;
    private long acountId;
    private boolean result;
    private long id;
    private int type;
    private String createDate;
    private List<AcountListBean> acountList;
    private boolean hasNewVersion;
    private int versionCode;
    private String version;
    private List<IdeaEntity> ideaList;
    private String ideaTitle;
    private String ideaContent;

    public String getIdeaTitle() {
        return ideaTitle;
    }

    public void setIdeaTitle(String ideaTitle) {
        this.ideaTitle = ideaTitle;
    }

    public String getIdeaContent() {
        return ideaContent;
    }

    public void setIdeaContent(String ideaContent) {
        this.ideaContent = ideaContent;
    }

    public List<IdeaEntity> getIdeaList() {
        return ideaList;
    }

    public void setIdeaList(List<IdeaEntity> ideaList) {
        this.ideaList = ideaList;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    private String url;
    private boolean isCollected;//是否已经收藏

    public List<ResultBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<ResultBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

    private String updateTime;
    private int acountType;//账号类型
    private String job;//职业
    private boolean tokenIsWord;//token是否还有效
    public List<ResultBean> userInfoList;

    public boolean isTokenIsWord() {
        return tokenIsWord;
    }

    public void setTokenIsWord(boolean tokenIsWord) {
        this.tokenIsWord = tokenIsWord;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    private String infomation;


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

    public String getWorkExprience() {
        return workExprience;
    }

    public void setWorkExprience(String workExprience) {
        this.workExprience = workExprience;
    }

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }

    private String workExprience;//工作经验
    private String goodAt;//擅长领域

    private String createTime;

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

    private String password;
    private String nickName;
    private String phone;
    private String imgUrl;
    private String lastLongTime;


    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAcountName() {
        return acountName;
    }

    public void setAcountName(String acountName) {
        this.acountName = acountName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getAcountId() {
        return acountId;
    }

    public void setAcountId(long acountId) {
        this.acountId = acountId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<AcountListBean> getAcountList() {
        return acountList;
    }

    public void setAcountList(List<AcountListBean> acountList) {
        this.acountList = acountList;
    }

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
