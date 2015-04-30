package com.tingshuo.tool.view.imageshower;

/**  
 * GalleryAdapter.java
 * @version 1.0
 * @author Haven
 * @createTime 2011-12-9 涓����05:04:34
 */

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.L;
import com.tingshuo.tool.view.imagescan.NativeImageLoader;
import com.tingshuo.tool.view.imagescan.NativeImageLoader.NativeImageCallBack;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.ImageDownloader;
import com.tingshuo.web.img.fetcher.ImageCache;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private List<String> images;
	// ImageLoaders imageLoaders;
	private LayoutInflater mInflater;
	private View parentView;
	private Point mPoint = new Point(0, 0);
	private boolean isWeb=false;
	private ImageCache mImageCache;
	private ImageDownloader downloader;
	public GalleryAdapter(Context context, List<String> imgs, View parentView,
			boolean isWeb) {
		this.context = context;
		this.images = imgs;
		this.parentView = parentView;
		this.isWeb = isWeb;
		mImageCache=new ImageCache(context,"tingshuo");
		downloader=new ImageDownloader(context, mImageCache);
		
		// imageLoaders = ImageLoaders.getInstance(context);
		// imageLoaders.clear();
		mInflater = LayoutInflater.from(context);
	}

	public void clearImg() {
		// imageLoaders.mapBitmaps.clear();
		System.gc();
	}

	public int getCount() {
		return images.size();
	}

	public Object getItem(int position) {
		return images.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ShowerImageView view;
		View cell=mInflater.inflate(R.layout.galler_cell_pic, null);
		try {
			String path = images.get(position);
//			view = new ShowerImageView(context, 0, 0);
			cell.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			view=(ShowerImageView) cell.findViewById(R.id.shower_img);
			view.setTag(path);
			// 利用NativeImageLoader类加载本地图片
			if (isWeb) {
				
				Bitmap bit =mImageCache.getBitmapFromMemCache(HttpJsonTool.imgServerUrl+"small/"+path);
				if(bit==null){
					bit =mImageCache.getBitmapFromDiskCache(HttpJsonTool.imgServerUrl+"small/"+path);
				}
				if(bit==null){
					bit= BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.friends_sends_pictures_no);
				}
				downloader.download(HttpJsonTool.imgServerUrl+"big/"+path, view,null,bit);
				
			} else {
				// 利用NativeImageLoader类加载本地图片
				Bitmap bitmap = NativeImageLoader.getInstance(context)
						.loadBigNativeImage(path, null,
								new NativeImageCallBack() {
									@Override
									public void onImageLoader(Bitmap bitmap,
											String path) {
										ShowerImageView mImageView = (ShowerImageView) parentView
												.findViewWithTag(path);
										L.i("onImageLoader");
										if (bitmap != null
												&& mImageView != null) {
											L.i("onImageLoader!=null");
											mImageView.setImageBitmap(bitmap);
											// mImageView.layoutToCenter();
										}
									}
								});

				if (bitmap != null) {
					view.setImageBitmap(bitmap);
					// view.layoutToCenter();
				} else {
					Bitmap bit = BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.friends_sends_pictures_no);
					view.setImageBitmap(bit);
					// view.layoutToCenter();
				}
			}
			return cell;
		} catch (OutOfMemoryError e) {
			mImageCache.clearMemorCaches();
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
		}
		return cell;
	}

}
