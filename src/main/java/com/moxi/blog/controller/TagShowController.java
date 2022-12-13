package com.moxi.blog.controller;

import com.moxi.blog.entity.Tag;
import com.moxi.blog.entity.Type;
import com.moxi.blog.service.IBlogService;
import com.moxi.blog.service.ITagService;
import com.moxi.blog.service.ITypeService;
import com.moxi.blog.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class TagShowController {
    public static final int PAGESIZE = 3;

    @Autowired
    ITagService tagService;
    @Autowired
    IBlogService blogService;

    /**
     * 分页 标签 博客列表
     *
     * @param pageBean
     * @param tagId
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}")
    public String tagsShow(PageBean pageBean, @PathVariable("id") Long tagId, Model model) {
        //查询所有的tag,和对应的数量
        Map<Tag, Integer> tagMap = tagService.tagTopNotPage();
        if (tagId == -1) {//查询第一个标签的博客列表，并分页显示
            tagId = Long.valueOf(((Tag) tagMap.keySet().toArray()[0]).getId());
            pageBean = blogService.selectPageBlogByTagId(pageBean, tagId);
        } else {//查询tagId类型的博客列表，并分页显示
            pageBean = blogService.selectPageBlogByTagId(pageBean, tagId);
        }
        model.addAttribute("tagMap", tagMap);
        model.addAttribute("blogPage", pageBean);
        //注意id也返回，点击获得分类的下一页
        model.addAttribute("tagId",tagId);
        model.addAttribute("totalTags",tagMap.keySet().size());
        return "tags";
    }
}
