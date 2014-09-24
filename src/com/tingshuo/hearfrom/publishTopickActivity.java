package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tingshuo.tool.ActivityTool;
import com.tingshuo.tool.T;
import com.tingshuo.tool.db.MapHolder;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.tool.view.adapter.PicAdapter;
import com.tingshuo.tool.view.imageshower.ImageDialog;
import com.tingshuo.web.http.HttpJsonTool;

public class publishTopickActivity extends BaseAcivity{
	private List<Map<String, Object>> pic_data;
	private PicAdapter adapter;
	private GridView mPicGridView;
	public static final String PIC_ADD = "com.tingshuo.hearfrom.pic_add";
	public static final int RESULT_IMG = 1236664;
	private EditText content_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publis_topick);
		initProgressDialog("正在发布主题，请稍后...");
		initContentView();
		refreshPicData(null);
	}
	@Override
	protected void initContentView() {
		super.initContentView();
		title_middle.setText("");
		titleLeft.setText("返回");
		title_right.setText("发布");
		title_right.setVisibility(View.VISIBLE);
		
		pic_data = new ArrayList<Map<String, Object>>();
		mPicGridView = (GridView) findViewById(R.id.pic_gridView);
		content_txt = (EditText) findViewById(R.id.content_txt);
		adapter = new PicAdapter(getApplicationContext(), pic_data,
				mPicGridView);
		mPicGridView.setAdapter(adapter);
		mPicGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (pic_data.get(position).get("path").equals(PIC_ADD)) {
					pic_data.remove(position);
					Intent intent = new Intent(getApplicationContext(),
							ShowImageActivity.class);
					intent.putExtra("count", pic_data.size());
					startActivityForResult(intent, RESULT_IMG);
				} else {
					
					List<String>list=new ArrayList<String>();
					for(Map<String,Object>map:pic_data){
						String path=(String) map.get("path");
						if(path.equals(PIC_ADD)){
							continue;
						}
						list.add(path);
					}
					ImageDialog imageDialog = new ImageDialog(
							publishTopickActivity.this, R.style.imgDialog, list,
							position,false);
					imageDialog.setCanceledOnTouchOutside(false);
					imageDialog.show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_IMG:
			if (data == null) {
				refreshPicData(null);
				return;
			}
			MapHolder holder = (MapHolder) data
					.getSerializableExtra("imgPaths");
			if (holder == null) {
				refreshPicData(null);
				return;
			}
			refreshPicData(holder);
			break;

		default:
			break;
		}
	}

	private void refreshPicData(MapHolder holder) {
		if (holder != null) {
			pic_data.addAll(holder.getUsers());
		}
		if (!(ShowImageActivity.fin_MAX_SELECT <= pic_data.size())) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("path", PIC_ADD);
			pic_data.add(data);
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.select_pic_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.commit_btn:
			commitTopick();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void initProgressDialog(String content) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("");
		mProgressDialog.setMessage(content);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}
	private ProgressDialog mProgressDialog;
	private void commitTopick(){
		final String content = content_txt.getText().toString();
		if(content.trim().length()<5){
			T.show(getApplicationContext(), "请填写主题内容，至少5个字。", Toast.LENGTH_LONG);
			return;
		}
		mProgressDialog.show();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String imgs="";
				int i=0;
				for(Map<String,Object>map:pic_data){
					String path=(String) map.get("path");
					if(path.equals(PIC_ADD)){
						continue;
					}
					imgs+=path+((i==pic_data.size()-1)?"":",");
					i++;
				}
				
				mainPostListHolder holder=new mainPostListHolder(-1, -1, "", "", content
						, imgs, "", "", 0, 0, 0, 0, 1, "",0);
				return HttpJsonTool.getInstance().publicsTopic(getApplicationContext(),holder, mProgressDialog);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				mProgressDialog.dismiss();
				if(result.startsWith(HttpJsonTool.ERROR403)){
					ActivityTool.gotoLoginView(getApplicationContext());
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.show(getApplicationContext(), "网络错误", Toast.LENGTH_LONG);
				}else{
					T.show(getApplicationContext(), "发布成功", Toast.LENGTH_LONG);
					finish();
				}
			}
		};
		task.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_left:
			finish();
			break;
		case R.id.title_txt_right:
			commitTopick();
			break;
		default:
			break;
		}
	}
}
