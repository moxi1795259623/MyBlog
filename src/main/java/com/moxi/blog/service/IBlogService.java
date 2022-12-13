package com.moxi.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moxi.blog.vo.BlogBlockVo;
import com.moxi.blog.vo.BlogPageVo;
import com.moxi.blog.vo.PageBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface IBlogService extends IService<Blog> {

    PageBean<BlogPageVo> pageBlog(PageBean pageBean, BlogPageVo blog);
    int saveBlog(Blog blog);

    PageBean<BlogBlockVo> pageIndexBlog(PageBean pageBean);

    List<Blog> getRecommandTitle(Integer pageSize);

    PageBean<BlogBlockVo> selectBlogByQuery(PageBean pageBean, String query);

    BlogBlockVo getBlogDetailsById(Long blogId);

    PageBean selectPageBlogByTypeId(PageBean pageBean, Long typeId);

    PageBean selectPageBlogByTagId(PageBean pageBean, Long tagId);

    Map<String, List<Blog>> archiveBlog();

    int blogCount();
}
