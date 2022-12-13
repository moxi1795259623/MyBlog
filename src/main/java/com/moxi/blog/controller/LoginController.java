package com.moxi.blog.controller;


import com.moxi.blog.entity.User;
import com.moxi.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("api/v1/pri/admin")
public class LoginController {
    @Autowired
    IUserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (null != user) {
            session.setAttribute("username", username);
            session.setAttribute("pwd", password);
            session.setAttribute("user",user);
            user.setPassword(null);
            return "admin/index";
        } else {
            //暂时放到session中
            attributes.addFlashAttribute("message", "用户名密码错误");
            return "redirect:/api/v1/pri/admin";
        }
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.removeAttribute("user");
        // 斜杠表示上下文路径，不要忘记
        return "redirect:/api/v1/pri/admin";
    }
}
