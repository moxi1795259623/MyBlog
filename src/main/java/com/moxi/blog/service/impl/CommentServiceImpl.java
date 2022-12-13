package com.moxi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxi.blog.entity.Comment;
import com.moxi.blog.entity.User;
import com.moxi.blog.mapper.CommentMapper;
import com.moxi.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    CommentMapper commentMapper;


    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 获得某一博客的【根】评论列表
     * @param blogId 博客Id
     * @return 评论列表
     */
    @Override
    public List<Comment> getCommentByBlogId(String blogId) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        //注意这里排序,返回根类节点
        commentQueryWrapper.eq("blog_id", blogId).isNull("parent_reply_id").orderByDesc("create_time");
        List<Comment> parentList = commentMapper.selectList(commentQueryWrapper);
        return parentList;
    }


    //1.存放迭代找出的所有子代的集合
    List<Comment> tempReplys;

    @Override
    public List<Comment> getAllChildComment(Comment p) {
        tempReplys = new ArrayList<>();
        //2.取出根节点的一级孩子节点
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("parent_reply_id", p.getId()).orderByAsc("create_time");
        List<Comment> childList = commentMapper.selectList(commentQueryWrapper);
        for (Comment c : childList) {
            //循环迭代，找出子代，存放在tempReplys中
            String parentName = commentMapper.selParentNickNameById(c.getId());
            c.setParentName(parentName);
            recursively(c);
        }
        return tempReplys;
    }

    /**
     * 递归迭代，剥洋葱
     *
     * @param c 被迭代的对象
     * @return
     */
    private void recursively(Comment c) {
        tempReplys.add(c);
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("parent_reply_id", c.getId()).orderByDesc("create_time");
        List<Comment> childList = commentMapper.selectList(commentQueryWrapper);
        if (childList != null && childList.size() > 0) {
            for (Comment ch : childList) {
                String parentName = commentMapper.selParentNickNameById(ch.getId());
                ch.setParentName(parentName);
                recursively(ch);
            }
        }
    }

    /**
     * 插入一条评论，注意:设置父评论id
     * @param comment
     * @param session
     */
    @Transactional
    @Override
    public void saveComment(Comment comment, HttpSession session) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        Integer parentReplyId = comment.getParentReplyId();
        if (parentReplyId == -1) { //该博客的第一条评论数据
            comment.setParentReplyId(null);
        } else {
            comment.setParentReplyId(parentReplyId);
        }
        //判断当前是不是博主发表的评论或者回复【其余用户没有登录功能】
        User user = (User) session.getAttribute("user");
        if(user!=null){
            comment.setAvatar(user.getAvatar());
            comment.setAdmin(true);
        }else{
            comment.setAvatar(avatar);
        }
        commentMapper.insert(comment);
    }


}
