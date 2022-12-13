package com.moxi.blog.service;

import com.moxi.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> getCommentByBlogId(String blogId);

    void saveComment(Comment comment, HttpSession session);

    List<Comment> getAllChildComment(Comment p);
}
