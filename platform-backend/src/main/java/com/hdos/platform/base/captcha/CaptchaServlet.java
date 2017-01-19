package com.hdos.platform.base.captcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdos.platform.common.util.CacheUtils;

/**
 * CaptchaServlet
 * @author chenyang
 *
 */
public class CaptchaServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(CaptchaServlet.class);
	/** serialVersionUID */
	private static final long serialVersionUID = -7134735351873237540L;

	/** 缓存的验证码key */
	public static final String KEY_CAPTCHA = "CAPTCHA_CACHE_KEY";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 设置相应类型,告诉浏览器输出的内容为图片
		resp.setContentType("image/jpeg");
		// 不缓存此内容
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expire", 0);
		try {

			HttpSession session = req.getSession();

			CaptchaUtil tool = new CaptchaUtil();
			StringBuffer code = new StringBuffer();
			BufferedImage image = tool.genRandomCodeImage(code);
			session.removeAttribute(KEY_CAPTCHA);
			session.setAttribute(KEY_CAPTCHA, code.toString());
			
			//session超时后无法取到验证码，故放入缓存
			CacheUtils.put(KEY_CAPTCHA,code.toString());
			
			// 将内存中的图片通过流动形式输出到客户端
			ImageIO.write(image, "JPEG", resp.getOutputStream());

		} catch (Exception e) {
			logger.info("Exception",e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		try {
			doGet(req, resp);
		} catch (ServletException e) {
			logger.info("ServletException",e);
		} catch (IOException e) {
			logger.info("IOException",e);
		}
	}
}
