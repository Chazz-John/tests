package com.zhao.shirospringboot.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chazz
 */
@Controller
public class Index {

    @RequestMapping(value = {"/","/index"})
    public String index(Model model) {
        model.addAttribute("msg","hello,shrio");
        return "index";
    }
    @RequestMapping("/user/add")
    public String add(Model model) {
        return "user/add";
    }
    @RequestMapping("/user/update")
    public String update(Model model){
        return "user/update";
    }
    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/toLogin")
    public String toLogin(String username,String pwd,Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
        try {
            subject.login(token);
            return "index";
        }catch (UnknownAccountException e) {
            model.addAttribute("msg","用户名不存在！");
            return "login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }
    }
}
