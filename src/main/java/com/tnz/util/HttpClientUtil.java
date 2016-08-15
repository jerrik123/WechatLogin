/**
 * @projectName:dhtserver
 * @packageName:com.dht.base.util
 * @className:HttpClientUtils.java
 *
 * @createTime:2014年3月13日-下午2:13:40
 *
 *
 */
package com.tnz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTT请求处理类
 * 
 * @author hongxiaodong
 */
public class HttpClientUtil {
	private static Logger LOGGER = LoggerFactory
			.getLogger(HttpClientUtil.class);

	public static String doGet(String url) {
		String result = null;
		try {
			HttpClient httpClient = HttpClients.createDefault();
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数

			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			result = EntityUtils.toString(entity);
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;

	}

	public static String doPost(String url, List<NameValuePair> params) {
		String result = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
		} catch (Exception e1) {

			e1.printStackTrace();
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;

	}

	/**
	 * HTTP请求处理
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String readContentFromGet(String url,
			Map<String, String> params) throws IOException {

		StringBuffer buffer_URL = new StringBuffer(url);
		Set<String> pas = params.keySet();
		List<String> list = new ArrayList<String>();
		list.addAll(pas);
		for (int i = 0; i < list.size(); i++) {
			String pa = list.get(i);
			String va = params.get(pa);
			if (i == 0) {
				buffer_URL.append("?").append(pa).append("=")
						.append(URLEncoder.encode(va, "utf-8"));
			} else {
				buffer_URL.append("&").append(pa).append("=")
						.append(URLEncoder.encode(va, "utf-8"));
			}
		}
		URL getUrl = new URL(buffer_URL.toString());
		HttpURLConnection connection = (HttpURLConnection) getUrl
				.openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuffer lines = new StringBuffer("");
		String string;
		while ((string = reader.readLine()) != null) {
			System.out.println(string);
			lines.append(string);
		}
		reader.close();
		connection.disconnect();
		return lines.toString();
	}

	public static String httpPostBody(String url, Object params) {
		String body = null;
		if (url != null && params != null) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = httpPost(url, params.toString());
			HttpResponse response = null;
			try {
				response = httpclient.execute(httpost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			String charset = EntityUtils.getContentCharSet(entity);
			try {
				body = EntityUtils.toString(entity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return body;
	}

	private static HttpPost httpPost(String url, Object params) {
		if (url != null && params != null) {
			HttpPost httpost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params instanceof Map) {
				Map<String, String> paramsMap = (Map<String, String>) params;
				Set<String> keySet = paramsMap.keySet();
				for (String key : keySet) {
					nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
				}
				try {
					httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return httpost;
			} else if (params instanceof String) {
				httpost.addHeader("Content-type",
						"application/json; charset=utf-8");
				httpost.setHeader("Accept", "application/json");
				httpost.setEntity(new StringEntity(params.toString(), Charset
						.forName("UTF-8")));
				return httpost;
			}
		}
		return null;
	}

}
