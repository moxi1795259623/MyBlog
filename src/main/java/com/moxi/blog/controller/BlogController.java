package com.moxi.blog.controller;


import com.moxi.blog.entity.Blog;
import com.moxi.blog.entity.Type;
import com.moxi.blog.service.IBlogService;
import com.moxi.blog.service.ITagService;
import com.moxi.blog.service.ITypeService;
import com.moxi.blog.service.IUserService;
import com.moxi.blog.utils.JsonData;
import com.moxi.blog.vo.BlogPageVo;
import com.moxi.blog.vo.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
@Slf4j
@RequestMapping("/api/v1/pri/admin/blog")
public class BlogController {
    @Autowired
    IBlogService blogService;
    @Autowired
    ITypeService typeService;
    @Autowired
    ITagService tagService;
    @Autowired
    IUserService userService;


    /**
     * 列表分页展示【无条件和条件共用】
     *
     * @param model
     * @param pageBean
     * @param blogPageVo
     * @return
     */
    @GetMapping("/blogList")
    public String pageBlogsVo(Model model, PageBean pageBean, BlogPageVo blogPageVo) {
        PageBean<BlogPageVo> blogPage = blogService.pageBlog(pageBean, blogPageVo);
        log.info("Records:{},Total:{},Size:{},Current:{}", blogPage.getItems(), blogPage.getTotalNum(), blogPage.getPageSize(), blogPage.getCurrentPage());
        //第一次点击index的请求进入blogs页面的时候，携带types类型数据供用户选择
        List<Type> types = typeService.listTypes();
        model.addAttribute("types", types);
        model.addAttribute("blogPage", blogPage);
        return "admin/blogs";
    }

    /**
     * 组合条件查询：点击搜索框
     *
     * @param model
     * @param pageBean
     * @param blogPageVo
     * @return
     */
    @PostMapping("/blogList/search")
    public String search(Model model, PageBean pageBean, BlogPageVo blogPageVo) {
        PageBean<BlogPageVo> blogPage = blogService.pageBlog(pageBean, blogPageVo);
        log.info("Records:{},Total:{},Size:{},Current:{}", blogPage.getItems(), blogPage.getTotalNum(), blogPage.getPageSize(), blogPage.getCurrentPage());
        model.addAttribute("blogPage", blogPage);
        //themeleaf整合ajax, 局部响应的实现
        return "admin/blogs :: blogList";
    }

    /**
     * 添加和修改共用，保存数据
     *
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @Transactional
    @PostMapping("/saveBlog")
    public String saveBlog(Blog blog, HttpSession session, RedirectAttributes attributes) {
        Integer state = blog.getId();
        //设置用户
        String username = (String) session.getAttribute("username");
        String pwd = (String) session.getAttribute("pwd");
        blog.setUserId(userService.getUser(username, pwd).getId());
        //blog的typeId和tagsId都已经赋值过，这里只要将中间表blog_tags保存即可
        if (blogService.saveBlog(blog) > 0) {
            if (state == null) {//新增时更新blog_tags表
                tagService.saveBlogTags(blog.getId(), blog);
            }
            attributes.addFlashAttribute("message", "操作成功");
        } else {
            attributes.addFlashAttribute("message", "操作失败，请稍后再试");
        }
        return "redirect:/api/v1/pri/admin/blog/blogList";
    }

    /**
     * 点击添加按钮，预查询类型和标签
     *
     * @param model
     * @return
     */
    @GetMapping("/addBlog/input")
    public String toBlogPage(Model model) {
        toModel(model);
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    public void toModel(Model model) {
        model.addAttribute("types", typeService.listTypes());
        model.addAttribute("tags", tagService.listTags());
    }

    /**
     * 编辑页面的数据回显
     *
     * @param model
     * @return
     */
    @GetMapping("/editBlog/{id}/input")
    public String editBlog(Model model, @Validated(Type.Update.class) @PathVariable("id") Long id) {
        //根据id查询数据回显->blog+type+tags
        Blog blog = blogService.getById(id);
        List<Integer> tagIds = tagService.getTags(blog.getId());
        blog.init(tagIds);
        toModel(model);
        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }


    /**
     * 删除博客
     *
     * @param attributes
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("/deleteBlog/{id}/input")
    public String deleteBlog(RedirectAttributes attributes, @Validated(Type.Update.class) @PathVariable("id") Long id) {
        //删除博客列表
        blogService.removeById(id);
        //删除标签中间表
        tagService.removeByBlogId(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/api/v1/pri/admin/blog/blogList";
    }
}
