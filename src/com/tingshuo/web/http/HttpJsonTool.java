package com.tingshuo.web.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.RoleUtil;
import com.tingshuo.tool.StatusTool;
import com.tingshuo.tool.db.CommentHelper;
import com.tingshuo.tool.db.CommentHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.db.lock;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.web.http.MyMultipartEntity.ProgressListener;

public class HttpJsonTool {

	public enum LoginInfo {
		correct, webfaild, wronginput, correctnoname
	};

	// public static String ServerUrl = "http://v.cc-railway.xzh-soft.com:8083";
	public static String imgServerUrl = "http://192.168.1.111:9999/";
	public static String ServerUrl = "http://192.168.1.111/tingshuo/index.php";
	// public static String ServerUrl2 =
	// "http://192.168.1.118/tingshuo/index.php";
	// public static final String ServerUrl = "http://192.168.137.1:8080";
	// public static final String ServerUrl = "http://10.35.45.100:8083";
	// public static final String ServerUrl = "http://10.10.1.45:8080";
	// public static final String ServerUrl =
	// "https://eaccess.syrailway.cn:8443/mapping/sjznbg";
	private static HttpJsonTool httpjsontool = null;
	public static final String STATUS = "status";

	public static final String ERROR = "<error>";
	public static final String SUCCESS = "<success>";
	public static final String ERROR403 = "<error403>";

	public static final int MELA = 1;
	public static final int FEMELA = 0;

	public static synchronized HttpJsonTool getInstance() {
		if (httpjsontool == null) {
			httpjsontool = new HttpJsonTool();
		}
		return httpjsontool;
	}

	private static CookieStore cookieInfo = null;
	private HttpClient getHttpClient(){
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  20000);//Á¬½ÓÊ±¼ä20s
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  30000);
		return client;
	}
	public void getCookieInfo() {
		Thread th = new Thread() {
			public void run() {
				HttpClient httpClient = HttpsClient.getInstance()
						.getHttpsClient();
				HttpGet httpRequest = new HttpGet(ServerUrl + "/1.jsp");
				try {
					httpClient.execute(httpRequest);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cookieInfo = ((AbstractHttpClient) httpClient).getCookieStore();
			}
		};
		th.start();
	}

	public synchronized String register(Context context, int role_id, int sex) {
		try {
			HttpClient client = getHttpClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/user/fastregister/");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("role_id", String
					.valueOf(role_id)));
			params.add(new BasicNameValuePair("sex", String.valueOf(sex)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
			JSONObject json = jsonObject.getJSONObject("data");
			insertUserTable(context, json, true);
		} catch(ConnectTimeoutException e){
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertUserTable(Context context, JSONObject json, boolean self)
			throws JSONException {
		String token = json.getString("token");
		int id = json.getInt("id");
		if (self) {
			PreferenceUtils.setPrefString(context, PreferenceConstants.TOKEN,
					token);
			HearFromApp.token = token;
			PreferenceUtils
					.setPrefInt(context, PreferenceConstants.USER_ID, id);
			HearFromApp.user_id = id;
			RoleUtil.saveDefaultRoleId(context, RoleUtil.role_id);
		}
		UserInfoHelper helper = new UserInfoHelper(context);
		String account = json.optString("account");
		String nickname = json.optString("nickname");
		String head = json.optString("head");
		int sex = json.optInt("sex");
		long login_time = json.optLong("login_time");
		String brithday = json.optString("brithday");
		String phonenum = json.optString("phonenum");
		long level_score = json.optLong("level_score");
		int level = json.optInt("level");
		int is_vip = json.optInt("is_vip");
		long vip_score = json.optLong("vip_score");
		int vip_level = json.optInt("vip_level");
		int status = json.optInt("status");
		UserInfoHolder holder = new UserInfoHolder(id, account, nickname, head,
				sex, login_time, brithday, phonenum, level_score, level,
				is_vip, vip_score, vip_level, status);
		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
		}
		helper.close();
	}

	public synchronized String login(Context context, String account,
			String password) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/user/fastregister/");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("account", account));
			params.add(new BasicNameValuePair("password", HttpStringMD5
					.md5(password)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
			JSONObject json = jsonObject.getJSONObject("data");
			insertUserTable(context,json,true);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	public synchronized String getTingshuoList(Context context, int max_id,
			int min_id, int page, int role_id, int sex) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/mainpost/getpost/");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (max_id != -1) {
				params.add(new BasicNameValuePair("max_id", String
						.valueOf(max_id)));
			}
			if (min_id != -1) {
				params.add(new BasicNameValuePair("min_id", String
						.valueOf(min_id)));
			}
			params.add(new BasicNameValuePair("page", String.valueOf(page)));
			params.add(new BasicNameValuePair("token", HearFromApp.token));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
			JSONArray list = jsonObject.getJSONArray("data");
			insertTingshuoList(context, list);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} 
		return SUCCESS;
	}
	public synchronized String getCommentList(Context context, int max_id,
			int start, int limit , int post_id) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/secondpost/getsecond/");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (max_id != -1) {
				params.add(new BasicNameValuePair("max_id", String
						.valueOf(max_id)));
			}
			if (start != -1) {
				params.add(new BasicNameValuePair("start", String
						.valueOf(start)));
			}
			params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
			params.add(new BasicNameValuePair("post_id", String.valueOf(post_id)));
			params.add(new BasicNameValuePair("token", HearFromApp.token));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
			JSONArray list = jsonObject.getJSONArray("data");
			insertCommentList(context, list);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} 
		return SUCCESS;
	}
	public synchronized String setZanMainPost(Context context,int post_id,boolean isZan) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ (isZan?"/mainpost/zan/":"/mainpost/cancelzan"));
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("post_id", String.valueOf(post_id)));
			params.add(new BasicNameValuePair("token", HearFromApp.token));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} 
		return SUCCESS;
	}
	public synchronized String setZanComment(Context context,int second_id,boolean isZan) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ (isZan?"/secondpost/zan/":"/secondpost/cancelzan"));
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("second_id", String.valueOf(second_id)));
			params.add(new BasicNameValuePair("token", HearFromApp.token));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} 
		return SUCCESS;
	}
	private void insertTingshuoList(Context context, JSONArray list)
			throws JSONException {
		mainPostListHelper helper = new mainPostListHelper(context);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				insertTingshuo(json, helper, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}
	private void insertCommentList(Context context, JSONArray list)
			throws JSONException {
		CommentHelper helper = new CommentHelper(context);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				insertComment(json, helper, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}
	private void insertComment(JSONObject json, CommentHelper helper,
			SQLiteDatabase db) {
		int id = json.optInt("id");
		int user_id = json.optInt("user_id");
		int post_id = json.optInt("post_id");
		String nickname = json.optString("nickname");
		String head = json.optString("head");
		String content = json.optString("content");
		String image = json.optString("image");
		String longitude = json.optString("longitude");
		String latitude = json.optString("latitude");
		Long time = json.optLong("time");
		int comment_count = json.optInt("comment_count");
		int zan_count = json.optInt("zan_count");
		int cai_count = json.optInt("cai_count");
		int role_id = json.optInt("role_id");
		String role = json.optString("role");
		CommentHolder holder = new CommentHolder(id, user_id,post_id,
				nickname, head, content, image, longitude, latitude, time,
				comment_count, zan_count, cai_count, role_id, role, 0);
		helper.insert(holder, db);
	}
	private void insertTingshuo(JSONObject json, mainPostListHelper helper,
			SQLiteDatabase db) {
		int id = json.optInt("id");
		int user_id = json.optInt("user_id");
		String nickname = json.optString("nickname");
		String head = json.optString("head");
		String content = json.optString("content");
		String image = json.optString("image");
		String longitude = json.optString("longitude");
		String latitude = json.optString("latitude");
		Long time = json.optLong("time");
		int comment_count = json.optInt("comment_count");
		int zan_count = json.optInt("zan_count");
		int cai_count = json.optInt("cai_count");
		int role_id = json.optInt("role_id");
		String role = json.optString("role");
		mainPostListHolder holder = new mainPostListHolder(id, user_id,
				nickname, head, content, image, longitude, latitude, time,
				comment_count, zan_count, cai_count, role_id, role, 0);
		helper.insert(holder, db);
	}
	
	public synchronized String sendComment(Context context,
			CommentHolder holder) {
		try {
			HttpClient client = getHttpClient();
			;
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/secondpost/reply/");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("content", holder.getContent()));
			params.add(new BasicNameValuePair("role_id", String.valueOf(holder.getRole_id())));
			params.add(new BasicNameValuePair("post_id", String.valueOf(holder.getPost_id())));
			params.add(new BasicNameValuePair("token", HearFromApp.token));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}
			JSONObject json = jsonObject.getJSONObject("data");
			CommentHelper helper = new CommentHelper(context);
			synchronized (lock.Lock) {
				SQLiteDatabase db = helper.getWritableDatabase();
				insertComment(json, helper, db);
			}
			helper.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} 
		return SUCCESS;
	}
	private long filesize;

	public synchronized String publicsTopic(Context context,
			mainPostListHolder holder, final ProgressDialog progressDialog) {
		try {
			HttpClient client = getHttpClient();
			client.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET,
					Charset.forName("UTF-8"));
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl + "/mainpost/fatie/");
			boolean ifimg = holder.getImage().length() > 0;
			filesize = holder.getContent().length();
			progressDialog.setProgress(0);
			ProgressListener listener = new ProgressListener() {

				@Override
				public void transferred(long num) {
					// TODO Auto-generated method stub
					progressDialog.setProgress((int) ((num / filesize) * 100));
				}
			};
			MyMultipartEntity mpEntity = new MyMultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName("UTF-8"), listener);
			if (ifimg) {
				String[] imgs = holder.getImage().split(",");
				for (int i = 0; i < imgs.length; i++) {
					String img = imgs[i];
					File f_file = new File(img);
					if (f_file.exists()) {
						filesize += f_file.length();
						ContentBody cbFile = new FileBody(f_file, "image/jpeg",
								"UTF-8");
						String imagepath = "image" + i;
						mpEntity.addPart(imagepath, cbFile);
					}
				}
			}
			progressDialog.setMax(100);
			mpEntity.addPart("content", new StringBody(holder.getContent(),
					Charset.forName("UTF-8")));
			mpEntity.addPart(
					"role_id",
					new StringBody(String.valueOf(holder.getRole_id()), Charset
							.forName("UTF-8")));
			mpEntity.addPart("token",
					new StringBody(HearFromApp.token, Charset.forName("UTF-8")));
			httpRequest.setEntity(mpEntity);
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				httpjsontool = null;
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("error", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			int status = jsonObject.optInt(STATUS);
			if (status != StatusTool.STATUS_OK) {
				return ERROR;
			}

			JSONObject json = jsonObject.getJSONObject("data");
			mainPostListHelper helper = new mainPostListHelper(context);
			synchronized (lock.Lock) {
				SQLiteDatabase db = helper.getWritableDatabase();
				insertTingshuo(json, helper, db);
			}
			helper.close();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}
}
