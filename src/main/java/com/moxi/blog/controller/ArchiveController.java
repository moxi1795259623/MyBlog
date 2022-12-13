package com.moxi.blog.controller;

import com.moxi.blog.entity.Blog;
import com.moxi.blog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchiveController {
    @Autowired
    private IBlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model) {
        Map<String, List<Blog>> archiveBlogMap = blogService.archiveBlog();
        int blogCount = blogService.blogCount();
        model.addAttribute("archiveBlogMap", archiveBlogMap);
        model.addAttribute("blogCount", blogCount);
        return "archives";
    }
}
