package com.moxi.blog.controller;

import com.moxi.blog.entity.Type;
import com.moxi.blog.service.IBlogService;
import com.moxi.blog.service.ITypeService;
import com.moxi.blog.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class TypeShowController {
    public static final int PAGESIZE = 3;

    @Autowired
    ITypeService typeService;
    @Autowired
    IBlogService blogService;

    /**
     * 分页 分类 博客列表
     *
     * @param pageBean
     * @param typeId
     * @param model
     * @return
     */
    @GetMapping("/types/{id}")
    public String typesShow(PageBean pageBean, @PathVariable("id") Long typeId, Model model) {
        //查询所有的type,和对应的数量
        Map<Type, Integer> typeMap = typeService.typeTopNotPage();
        if (typeId == -1) {//查询第一个类型的博客列表，并分页显示
            typeId= Long.valueOf(((Type) typeMap.keySet().toArray()[0]).getId());
            pageBean = blogService.selectPageBlogByTypeId(pageBean, typeId);
        } else {//查询typeId类型的博客列表，并分页显示
            pageBean = blogService.selectPageBlogByTypeId(pageBean, typeId);
        }
        model.addAttribute("typeMap", typeMap);
        model.addAttribute("blogPage", pageBean);
        //注意id也返回，点击获得分类的下一页
        model.addAttribute("typeId",typeId);
        model.addAttribute("totalTags",typeMap.keySet().size());
        return "types";
    }
}
