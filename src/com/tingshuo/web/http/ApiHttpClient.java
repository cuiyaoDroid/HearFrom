package com.tingshuo.web.http;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class ApiHttpClient {

	private static final String RONGCLOUDURI = "https://api.cn.rong.io";
	private static final String UTF8 = "UTF-8";
	
	//ªÒ»°token
	public static SdkHttpResult getToken(String appKey, String appSecret,
			String userId, String userName, String portraitUri,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey, appSecret,
				RONGCLOUDURI + "/user/getToken." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&name=").append(URLEncoder.encode(userName, UTF8));
		sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}	
}