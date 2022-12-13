package com.moxi.blog.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.entity.Type;
import com.moxi.blog.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BlogBlockVo {
    private Integer blogId;
    private String blogTitle;
    private Date updateTime;
    private Date createTime;
    private List<Tag> tag;//{"id","tag1"} 开始看错了 ，前端显示的是类别，不是标签
    private Type type; //补充 这里体现了老师设计的好处
    private int views;
    private User user; //username ,avatar
    private String description;
    private String firstPicture;
    private String content;
    private String flag;
    private boolean appreciation;

    private boolean shareStatement;

    private boolean commentable;

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "BlogBlockVo{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", tag=" + tag +
                ", type=" + type +
                ", views=" + views +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", content='" + content + '\'' +
                ", flag='" + flag + '\'' +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentable=" + commentable +
                '}';
    }
}
