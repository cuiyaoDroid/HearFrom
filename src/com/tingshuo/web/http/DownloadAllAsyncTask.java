package com.tingshuo.web.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.CoreProtocolPNames;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.tool.FileTool;
import com.tingshuo.tool.L;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;


public class DownloadAllAsyncTask extends
		AsyncTask<ArrayList<String>, Integer, Boolean> {

	private ProgressBar bar;
	private int count = 0;
	private File downloadFile = null;
	private downloadCallbackListener listener;

	public DownloadAllAsyncTask(ProgressBar bar) {
		super();
		if (bar != null) {
			this.bar = bar;
			bar.setProgress(count);
		}
	}

	static TrustManager[] xtmArray = new X509TrustManageTool[] { new X509TrustManageTool() };

	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	@Override
	protected Boolean doInBackground(ArrayList<String>... params) {
		try {
			String content = "token="
					+ URLEncoder.encode(HearFromApp.token, "UTF-8");
			//
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			client.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET,
					Charset.forName("UTF-8"));
			for (String path : params[0]) {
				downloadFile = new File(HearFromApp.appPath
						+ FileTool.getExtensionName(path));
				// bar.setMax(response.getHeaders(name));
				if (downloadFile.exists()) {
					continue;
				}
				HttpGet httpRequest = new HttpGet(HttpJsonTool.ServerUrl + path
						+ "?" + content);
				L.i("httpdownload", HttpJsonTool.ServerUrl + path + "?"
						+ content);
				HttpResponse response;
				response = client.execute(httpRequest);
				HttpEntity entity = response.getEntity();

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					downloadFile.createNewFile();
					InputStream inputStream = entity.getContent();
					FileOutputStream fileOutputStream = new FileOutputStream(
							downloadFile);
					byte[] buffer = new byte[10 * 1024];
					while (true) {
						int len = inputStream.read(buffer);
						// publishProgress(len);
						// L.i("len", len + "");
						if (len == -1) {
							break;
						}
						fileOutputStream.write(buffer, 0, len);
					}
					inputStream.close();

					fileOutputStream.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return true;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (downloadFile != null) {
			if (downloadFile.exists()) {
				downloadFile.delete();
			}
		}
		if (bar != null) {
			bar.setVisibility(View.GONE);
		}
	}

	public void setdownloadCallbackListener(downloadCallbackListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (downloadFile != null) {
			if (downloadFile.exists()) {
				if (downloadFile.length() == 0) {
					downloadFile.delete();
				}
			}
		}
		if (bar != null) {
			bar.setVisibility(View.GONE);
		}
		listener.onComplete();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		count += values[0];
		// bar.setProgress(count);
		super.onProgressUpdate(values);
	}

}
