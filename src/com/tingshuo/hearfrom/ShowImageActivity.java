package com.tingshuo.hearfrom;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tingshuo.hearfrom.base.BaseSelectImageActivity;
import com.tingshuo.tool.L;
import com.tingshuo.tool.db.MapHolder;
import com.tingshuo.tool.view.imagescan.ChildAdapter;
import com.tingshuo.tool.view.imagescan.imageSelectListener;
import com.tingshuo.tool.view.imageshower.ImageDialog;
import com.tingshuo.web.img.ImageFileCache;

public class ShowImageActivity extends BaseSelectImageActivity implements
		imageSelectListener {
	private final static int PIC_LOAD_FINISH = 2;
	public static final int fin_MAX_SELECT = 9;
	public static int MAX_SELECT;
	private ChildAdapter adapter;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PIC_LOAD_FINISH:
				mProgressDialog.dismiss();
				Intent intent = new Intent(ShowImageActivity.this,
						publishTopickActivity.class);
				@SuppressWarnings("unchecked")
				MapHolder holder = new MapHolder(
						(List<Map<String, Object>>) msg.obj);
				Bundle bundle = new Bundle();
				bundle.putSerializable("imgPaths", holder);
				intent.putExtras(bundle);
				setResult(publishTopickActivity.RESULT_IMG, intent);
				finish();
				break;
			}
		}

	};
	private void takePic() {
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (isHasSdcard()) {
			cramepath = System.currentTimeMillis() + ".jpg";
			camera.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(HearFromApp.appPath + cramepath)));
		} else {
			Toast.makeText(getApplicationContext(), "没有找到sd卡",
					Toast.LENGTH_LONG).show();
		}
		startActivityForResult(camera, CAMERA_TAKE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MAX_SELECT=fin_MAX_SELECT-(getIntent().getIntExtra("count", 0));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CAMERA_TAKE:
			L.i("CAMERA_TAKE", "CAMERA_TAKE");
			if (resultCode == RESULT_OK) {
				try {
					if (cramepath.length() > 0) {
						String savePath = HearFromApp.appPath + "j_"
								+ cramepath;
						ImageFileCache.compressImage(HearFromApp.appPath
								+ cramepath, savePath, true);
						Map<String, Object> mapdata = new HashMap<String, Object>();
						List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
						mapdata.put("path", savePath);
						listdata.add(mapdata);
						Message msg = new Message();
						msg.obj = listdata;
						msg.what = PIC_LOAD_FINISH;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					L.e("Exception", e.getMessage(), e);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// Toast.makeText(this, "选中 " + adapter.getSelectItems().size() +
		// " item",
		// Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}

	@Override
	public void updateTitle(int count) {
		// TODO Auto-generated method stub
		title_right.setText("完成(" + count + "/" + MAX_SELECT + ")");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finish();
			break;
		case R.id.title_txt_right:
			mProgressDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
					for (int i : adapter.getSelectItems()) {
						Map<String, Object> data = new HashMap<String, Object>();
						String savePath = HearFromApp.appPath
								+ System.currentTimeMillis() + ".jpg";
						ImageFileCache.compressImage(Imagelist.get(i),
								savePath, false);
						data.put("path", savePath);
						listdata.add(data);
					}
					Message msg = new Message();
					msg.obj = listdata;
					msg.what = PIC_LOAD_FINISH;
					mHandler.sendMessage(msg);
				}
			}).start();
			break;
		default:
			break;
		}
	}
	private ImageDialog imageDialog;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (Imagelist.get(position).equals(CAMRE)) {
			takePic();
		}else{
			if(imageDialog!=null){
				if(imageDialog.isShowing()){
					return;
				}
			}
			imageDialog = new ImageDialog(
					ShowImageActivity.this, R.style.imgDialog, Imagelist,
					position,false);
			imageDialog.setCanceledOnTouchOutside(false);
			imageDialog.show();
		}
	}

	@Override
	protected void initGridView() {
		// TODO Auto-generated method stub
		adapter = new ChildAdapter(ShowImageActivity.this, Imagelist,
				mGridView);
		adapter.setListener(this);
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void refreshGridView() {
		// TODO Auto-generated method stub
		adapter.getmSelectMap().clear();
		adapter.notifyDataSetChanged();
	}
}
