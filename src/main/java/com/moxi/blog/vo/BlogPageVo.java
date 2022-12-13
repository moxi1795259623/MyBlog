package com.moxi.blog.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * Blog的查询条件类
 */

public class BlogPageVo implements Serializable {
    private static final long serialVersionUID = -4294461808433313537L;
    private Integer typeId;
    private String typeName;
    private Integer blogId;
    private String blogTitle;
    private boolean recommend;
    private Date updateTime;
    private boolean published;

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BlogPageVo{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", recommend=" + recommend +
                ", updateTime=" + updateTime +
                ", published=" + published +
                '}';
    }
}
