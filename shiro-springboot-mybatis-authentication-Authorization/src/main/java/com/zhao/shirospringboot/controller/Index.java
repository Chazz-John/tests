package com.zhao.shirospringboot.controller;


import com.zhao.shirospringboot.config.PwdConfig;
import com.zhao.shirospringboot.entity.UserInfo;
import com.zhao.shirospringboot.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chazz
 */
@Controller
public class Index {

    @Autowired
    UserInfoService userService;

    @RequestMapping(value = {"/","/index"})
    public String index() {
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
    @RequestMapping("/register")
    public String register(Model model) {
        return "registration";
    }
    @RequestMapping("/toLogin")
    public String toLogin(String username,String pwd,Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
        try {
            subject.login(token);
            Subject current = SecurityUtils.getSubject();
            Session session = current.getSession();
            String s = "hello,"+token.getUsername()+",欢迎使用shrio";
            session.setAttribute("logined", s );
            return "index";
        }catch (UnknownAccountException e) {
            model.addAttribute("msg","用户名不存在！");
            return "login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/toRegister")
    public String toRegister(String username,String pwd, Integer roleId ,Model model) {
        UserInfo users = userService.findUserInfoByName(username);
        if (users!=null){
            model.addAttribute("msg","对不起,你设置的账号已存在!");
            return "registration";
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        UserInfo info = new UserInfo(null, username, pwd, salt, roleId,null,null);
        String s = new PwdConfig().getPwd(username,pwd,salt);
        info.setPassword(s);
        userService.addUserInfo(info);
        // model.addAttribute("username",info.getUserName());
        // model.addAttribute("pwd",user.getPassword());
        return "login";
    }

    @RequestMapping("/admin/add")
    @RequiresPermissions("user:select")
    public String AdminAdd(Model model) {
        return "admin/add";
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }
}
