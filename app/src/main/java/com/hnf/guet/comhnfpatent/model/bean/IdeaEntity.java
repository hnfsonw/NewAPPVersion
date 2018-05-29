package com.hnf.guet.comhnfpatent.model.bean;

import java.util.Date;

public class IdeaEntity {
    private long acountId;
    private String title;//需求标题
    private String content;//需求内容
    private String ideaImage;//需求图片url
    private String ideaFile;//需求文件
    private Date createTime;//创建时间

    public long getAcountId() {
        return acountId;
    }

    public void setAcountId(long acountId) {
        this.acountId = acountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdeaImage() {
        return ideaImage;
    }

    public void setIdeaImage(String ideaImage) {
        this.ideaImage = ideaImage;
    }

    public String getIdeaFile() {
        return ideaFile;
    }

    public void setIdeaFile(String ideaFile) {
        this.ideaFile = ideaFile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
