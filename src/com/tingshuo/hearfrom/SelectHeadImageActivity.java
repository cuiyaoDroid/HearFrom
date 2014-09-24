package com.tingshuo.hearfrom;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tingshuo.tool.L;
import com.tingshuo.tool.view.adapter.HeadAdapter;
import com.tingshuo.web.img.ImageFileCache;
import com.tingshuo.web.img.PictureUtil;

public class SelectHeadImageActivity extends BaseSelectImageActivity {
	private final static int PIC_LOAD_FINISH = 2;
	private HeadAdapter adapter;
	private static final int CROP_BIG_PICTURE = 3;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PIC_LOAD_FINISH:
				mProgressDialog.dismiss();
				String path=(String) msg.obj;
				PictureUtil.cropImageUri(SelectHeadImageActivity.this, Uri.parse(path), 320, 320, CROP_BIG_PICTURE);
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
						Message msg = new Message();
						msg.obj = savePath;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_left:
			finish();
			break;
		case R.id.title_txt_right:
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (Imagelist.get(position).equals(CAMRE)) {
			takePic();
		}else{
			String path=Imagelist.get(position);
			PictureUtil.cropImageUri(this, Uri.parse(path), 320, 320, CROP_BIG_PICTURE);
		}
	}

	@Override
	protected void initGridView() {
		// TODO Auto-generated method stub
		adapter = new HeadAdapter(SelectHeadImageActivity.this, Imagelist,
				mGridView);
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void refreshGridView() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}
}
