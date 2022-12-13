package com.moxi.blog.controller;

import com.moxi.blog.annotation.MethodExporter;
import com.moxi.blog.entity.Blog;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.entity.Type;
import com.moxi.blog.service.IBlogService;
import com.moxi.blog.service.ITagService;
import com.moxi.blog.service.ITypeService;
import com.moxi.blog.vo.BlogBlockVo;
import com.moxi.blog.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    IBlogService blogService;
    @Autowired
    ITypeService typeService;
    @Autowired
    ITagService tagService;


    /**
     * 访问首页
     *
     * @param model
     * @param pageBean
     * @return
     */
    @GetMapping("/")
    public String index(Model model, PageBean pageBean) {
        //进入首页时需要博客信息分页显示，用户推荐列表，类型列表(根据博客类别个数排名)，标签列表（同类型）
        PageBean<BlogBlockVo> blogPage = blogService.pageIndexBlog(pageBean);
        Map<Type, Integer> types = typeService.typeTop(pageBean.getPageSize());//{type1,count;type2,count}
        Map<Tag, Integer> tags = tagService.tagTop(pageBean.getPageSize());
        //按照更新日期排名
        List<Blog> recommendList = blogService.getRecommandTitle(pageBean.getPageSize());
        //一个博客块的分页
        model.addAttribute("blogPage", blogPage);
        //类别
        model.addAttribute("types", types);
        //标签
        model.addAttribute("tags", tags);
        //推荐列表
        model.addAttribute("recommendList", recommendList);
        return "index";
    }

    /**
     * 全局搜索
     *
     * @param model
     * @param pageBean
     * @param query    模糊搜索的内容
     * @return
     */
    @PostMapping("/search")
    public String search(Model model, PageBean pageBean, @RequestParam("query") String query) {
        PageBean<BlogBlockVo> blogPage = blogService.selectBlogByQuery(pageBean, query);
        if (StringUtils.hasLength(query)) {
            model.addAttribute("blogPage", blogPage);
            model.addAttribute("query", query);//搜索框里可以回显数据
            return "search";
        } else {
            return "index";
        }
    }

    /**
     * 上一页 下一页 get方式
     *
     * @param model
     * @param pageBean
     * @param query
     * @return
     */
    @GetMapping("/search1")
    public String search1(Model model, PageBean pageBean, @RequestParam("query") String query) {
        return search(model, pageBean, query);
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(Model model, @PathVariable("id") Long blogId) {
        BlogBlockVo blogVo = blogService.getBlogDetailsById(blogId);
        model.addAttribute("blogVo", blogVo);
        return "detail";
    }


    @GetMapping("/detail")
    public String detail() {
        return "detail";
    }


    //测试
    @ResponseBody
    @MethodExporter
    @GetMapping("/hello")
    public String sayHello(@RequestParam("name") String name) {
        System.out.println("hello:" + name);
        return "hi~";
    }

    //表单和url同名会拼接 aaa="bbb,ccc"
    @PostMapping("/test1")
    public String hello(@RequestParam("aaa") String aaa) {
        System.out.println("hello:" + aaa);
        return "hi~";
    }

    @GetMapping("/about")
    public String toAbout() {
        return "about";
    }

    @GetMapping("/footer/footBlogList")
    public String footBlogList(Model model) {
        List<Blog> blogList = blogService.getRecommandTitle(3);
        model.addAttribute("newBlogList", blogList);
        return "_fragments :: footBlogList";
    }


}
