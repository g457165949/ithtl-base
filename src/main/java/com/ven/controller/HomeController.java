package com.ven.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ven.config.shiro.MyShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Controller
public class HomeController extends BaseController{
	@Autowired
	DefaultKaptcha defaultKaptcha;

	@Autowired
	MyShiroRealm myShiroRealm;

	@RequestMapping("/index")
	public String index() {
		System.out.println("------>>index");
		return "home/index";
	}

	@RequestMapping("/")
	public String main(){
		System.out.println("------>>main");
		return "layout/main";
	}
	
	@RequestMapping("/err")
	public String error(Model model) {
		model.addAttribute("message", "(403)您没有访问权限!");
		return "home/error";
	}

	@RequestMapping("/clear-cache")
	@ResponseBody
	public Map<Object,Object> clearCache(){
		myShiroRealm.clearAllCache();
		return success();
	}

	@RequestMapping("/captcha")
	public void captcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			//生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			httpServletRequest.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, createText);
			//使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		//定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
}
