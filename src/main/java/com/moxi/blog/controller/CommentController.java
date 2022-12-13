package com.moxi.blog.controller;


import com.moxi.blog.entity.Comment;
import com.moxi.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Controller
public class CommentController {
    @Autowired
    ICommentService commentService;

    /**
     * 针对某一条博客 进行回复 【未添加回复者的初级版】-->添加递归【实现二级列表更改版】
     *
     * @param blogId
     * @param model
     * @return 返回该博客对应的评论列表【评论内容，评论者的名字，评论时间】
     */
    @GetMapping("/comments/{blogId}")
    public String reply(@PathVariable("blogId") String blogId, Model model) {
        List<Comment> parentList = commentService.getCommentByBlogId(blogId);
        //<Comment_p , List<Comment>_c>
        if (parentList != null && parentList.size() > 0) {
            HashMap<Comment, List<Comment>> commentListHashMap = new HashMap<>();
            for (Comment p : parentList) {
                //根据双亲找到封装了所有孩子的列表
                List<Comment> childList = commentService.getAllChildComment(p);
                commentListHashMap.put(p, childList);
            }
            model.addAttribute("commentListHashMap", commentListHashMap);
        }
        return "detail :: commentList";
    }

    /**
     * 提交评论表单的时候，保存评论数据
     *
     * @param comment
     * @return 返回评论列表片段
     */
    @PostMapping("/comments")
    public String saveComment(Comment comment, HttpSession session) {
        commentService.saveComment(comment, session);
        return "redirect:/comments/" + comment.getBlogId();
    }


}
