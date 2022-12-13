package com.moxi.blog.vo;

public class BlogTags {
    private Long blogId;
    private Long tagId;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "BlogTags{" +
                "blogId=" + blogId +
                ", tagId=" + tagId +
                '}';
    }
}
