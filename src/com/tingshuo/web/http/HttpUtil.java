package com.tingshuo.web.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpUtil {
	
	private static final String APPKEY = "App-Key";
	private static final String NONCE = "Nonce";
	private static final String TIMESTAMP = "Timestamp";
	private static final String SIGNATURE = "Signature";
	
	//����body��
	public static void setBodyParameter(StringBuilder sb,
			HttpURLConnection conn) throws IOException {
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(sb.toString());
		out.flush();
		out.close();
	}
	//���ǩ��header
	public static HttpURLConnection CreatePostHttpConnection(String appKey,
			String appSecret, String uri) throws MalformedURLException,
			IOException, ProtocolException {
		String nonce = String.valueOf(Math.random() * 1000000);
		String timestamp = String.valueOf(System.currentTimeMillis());
		StringBuilder toSign = new StringBuilder(appSecret).append(nonce)
				.append(timestamp);
		String sign = CodeUtil.hexSHA1(toSign.toString());

		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setInstanceFollowRedirects(true);

		conn.setRequestProperty(APPKEY, appKey);
		conn.setRequestProperty(NONCE, nonce);
		conn.setRequestProperty(TIMESTAMP, timestamp);
		conn.setRequestProperty(SIGNATURE, sign);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		return conn;
	}

	public static byte[] readInputStream(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}
	
	public static SdkHttpResult returnResult(HttpURLConnection conn)
			throws Exception, IOException {
		String result = new String(readInputStream(conn.getInputStream()));
		return new SdkHttpResult(conn.getResponseCode(),result);
	}
}