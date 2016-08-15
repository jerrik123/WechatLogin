package com.tnz.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class OpenIDAction
 */
public class OpenIDAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = LoggerFactory.getLogger(OpenIDAction.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OpenIDAction() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("acquireOpenID begin at {}", new Date().getTime());
		String reqUrl = generateReqUrl();
		LOGGER.info("acquireOpenID url is {}", reqUrl);
		response.sendRedirect(reqUrl);
		return;
	}

	@SuppressWarnings("deprecation")
	private String generateReqUrl() throws UnsupportedEncodingException {
		return "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid="
				+ "wxba18404f8cfe70cd"
				+ "&redirect_uri="
				+ URLEncoder.encode("http://www.jerrik168.cn/WechatLogin/wechat/callBack.do","UTF-8")
				+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	}
}
