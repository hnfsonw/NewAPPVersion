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
    private String url;
    private String updateTime;
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
