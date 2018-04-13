package com.ven.controller;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ven.domain.account.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Controller
@RequestMapping(value="site")
public class SiteController extends BaseController{

    @ModelAttribute("module")
    String module() {
        return "site";
    }
	
	@RequestMapping(value = { "", "/index" })
	public String index() {
		return "site/index";
	}

	@GetMapping("/login")
	public String login() {
    	if(SecurityUtils.getSubject() != null && SecurityUtils.getSubject().isAuthenticated()){
			return "redirect:/";
		}
		return "site/login1";
	}

	@PostMapping("/login")
	@ResponseBody
	public Map<Object,Object> login(HttpServletRequest request) throws Exception {
		System.out.println("----->>login:error");
		// 登录失败从request中获取shiro处理的异常信息。
		// shiroLoginFailure:就是shiro异常类的全类名.
		String exception = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("exception=" + exception);
		String msg = "";
		int code = -1;
		if (exception != null) {
			if (UnknownAccountException.class.getName().equals(exception)) {
				System.out.println("UnknownAccountException -- > 账号不存在：");
				msg = "账号不存在!";
			} else if (IncorrectCredentialsException.class.getName().equals(exception)) {
				System.out.println("IncorrectCredentialsException -- > 密码不正确：");
				msg = "密码不正确!";
			} else if (LockedAccountException.class.getName().equals(exception)){
				msg = "用户被锁定！";
			}else if("captcha.error".equals(exception)) {
				msg = "验证码错误！";
			}else{
				msg = "网络繁忙，请从新操作！";
				code = 50000;
				System.out.println("else -- >" + exception);
			}
		}
		return msg(code,msg);
	}

	@RequestMapping(value="/logout",method=RequestMethod.GET)    
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息  
        SecurityUtils.getSubject().logout();    
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
		return "redirect:/site/login";
    }
}
