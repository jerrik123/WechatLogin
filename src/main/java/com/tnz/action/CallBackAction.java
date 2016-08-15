package com.tnz.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tnz.util.HttpClientUtil;

/**
 * Servlet implementation class OpenIDAction
 */
public class CallBackAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = LoggerFactory
			.getLogger(CallBackAction.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CallBackAction() {
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
		LOGGER.info("CallBackAction begin at {}", new Date().getTime());
		String openid = request.getParameter("openid");
		String code = request.getParameter("code");
		LOGGER.info("获到的 code is : {} ", code);
		String redirectUrl = acquireOpenIDUrlWithCode(code);
		LOGGER.info("redirectUrl is : {}", redirectUrl);
		String responseText = HttpClientUtil.doGet(redirectUrl);
		LOGGER.info("responseText is : {}", redirectUrl, responseText);

		LOGGER.info("=======================================");
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.print(responseText);
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) {
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxba18404f8cfe70cd&redirect_uri="
				+ "http%3A%2F%2Fwww.jerrik168.cn%2FWechatLogin%2Fwechat%2FcallBack.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		System.out.println(HttpClientUtil.doGet(url));
	}

	private String acquireOpenIDUrlWithCode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxba18404f8cfe70cd&"
				+ "secret=028fc71aeb278ca18a9993aec7a9aa69&code="
				+ code
				+ "&grant_type=authorization_code");
		return sb.toString();
	}

}
