/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tingshuo.web.img;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.tingshuo.tool.L;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 */
public class ImageDownloader {
    private static final String LOG_TAG = "ImageDownloader";

    public enum Mode { NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT }
    private Mode mode = Mode.CORRECT;
    private ImageFileCache fileCache;
    private ImageMemoryCache imageCache;
    
    
    private static Bitmap loadingimage;
    public static void setloadingimage(Bitmap image){
    	loadingimage = image;
    }
	/**
     * Download the specified image from the Internet and binds it to the provided ImageView. The
     * binding is immediate if the image is found in the cache and will be done asynchronously
     * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
     *
     * @param url The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     * 
     * edit by Cuiyao 2013-9-22 add fileCache
     * 
     * 崔垚：圆角处理BitmapFillet.fillet函数，处理过程中产生新的Bitmap，为了避免内存浪费,所以不可以在显示之前才做处理。
     * 圆角处理流程：
     * 一、imageCache缓冲区和SDCard中没有图片文件时，从网络异步下载文件，把下载得到bitmap做圆角处理得到filletbitmap，接下来把filletbitmap存入imageCache缓存区。
     * 同时把原始的bitmap存入SDCard缓冲区，最后把调用bitmap的recycle来释放原始bitmap，并在imageview上显示filletbitmap。
     * 
     * 二、imageCache中没有图片文件，而SDCard中存在时，从SDCard中读取bitmap，然后做圆角处理得到filletbitmap，接下来把filletbitmap存入imageCache缓存区，
     * 最后调用recycle释放原始bitmap，并在imageview上显示filletbitmap。
     * 
     * 三、imageCache中存在图片缓存，读取并显示（所有的缓冲区入口都是经过圆角处理的）。
     * 
     */
    private int type=0;
    public ImageDownloader(Context context ,int type){
    	fileCache=new ImageFileCache();
    	imageCache=new ImageMemoryCache(context);
    	this.type=type;
    }
    
    public void cleanCache(){
    	imageCache.clearCache();
    }
    public void download(String url, ImageView imageView) {
    	imageCache.resetPurgeTimer();
        Bitmap bitmap = imageCache.getBitmapFromCache(url);
        
		if (bitmap == null) {
			// 文件缓存中获取
			bitmap = fileCache.getImage(url,""+type);
			if (bitmap == null) {
				forceDownload(url, imageView);
			} else {
				//edit bv cuiyao 
				imageCache.addBitmapToCache(url, bitmap);
				cancelPotentialDownload(url, imageView);
				imageView.setImageBitmap(bitmap);
			}
		}else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
    }
    private ProgressBar progressBar=null;
    public void download(String url, ImageView imageView,ProgressBar progressBar) {
    	this.progressBar=progressBar;
    	if(this.progressBar!=null){
			this.progressBar.setVisibility(View.VISIBLE);
		}
    	imageCache.resetPurgeTimer();
        Bitmap bitmap = imageCache.getBitmapFromCache(url);
        
		if (bitmap == null) {
			// 文件缓存中获取
			bitmap = fileCache.getImage(url,""+type);
			if (bitmap == null) {
				forceDownload(url, imageView);
			} else {
				//edit bv cuiyao 
				imageCache.addBitmapToCache(url, bitmap);
				cancelPotentialDownload(url, imageView);
				if(this.progressBar!=null){
					this.progressBar.setVisibility(View.GONE);
				}
				imageView.setImageBitmap(bitmap);
			}
		}else {
			cancelPotentialDownload(url, imageView);
			if(this.progressBar!=null){
				this.progressBar.setVisibility(View.GONE);
			}
			imageView.setImageBitmap(bitmap);
		}
    }
    /*
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
       private void forceDownload(String url, ImageView view) {
          forceDownload(url, view, null);
       }
     */

    /**
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
     */
    private void forceDownload(String url, ImageView imageView) {
        // State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }

        if (cancelPotentialDownload(url, imageView)) {
            switch (mode) {
                case NO_ASYNC_TASK:
                    Bitmap bitmap = downloadBitmap(url);
                    imageCache.addBitmapToCache(url, bitmap);
                    if(this.progressBar!=null){
    					this.progressBar.setVisibility(View.GONE);
    				}
    				imageView.setImageBitmap(bitmap);
                    break;

                case NO_DOWNLOADED_DRAWABLE:
                    imageView.setMinimumHeight(156);
                    BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                    task.execute(url);
                    break;

                case CORRECT:
                    task = new BitmapDownloaderTask(imageView);
                    DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                    imageView.setImageDrawable(downloadedDrawable);
//                    imageView.setMinimumHeight(156);
//                    imageView.setMinimumWidth(156);
                    task.execute(url);
                    break;
            }
        }
    }

    /**
     * Returns true if the current download has been canceled or if there was no download in
     * progress on this image view.
     * Returns false if the download in progress deals with the same url. The download is not
     * stopped in that case.
     */
    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    Bitmap downloadBitmap(String url) {
        final int IO_BUFFER_SIZE = 4 * 1024;
        Bitmap bitmap = null;
        // AndroidHttpClient is not allowed to be used from the main thread
        final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() :
            AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                L.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                } catch(OutOfMemoryError e){
                	BitmapFactory.Options options = new BitmapFactory.Options();
                	options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream),null,options);
                }
                finally {
                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }
                    entity.consumeContent();
                }
            }
            return bitmap;
        } catch (IOException e) {
            getRequest.abort();
            L.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            L.w(LOG_TAG, "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            L.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
        } finally {
            if ((client instanceof AndroidHttpClient)) {
                ((AndroidHttpClient) client).close();
            }
            
        }
        return null;
    }

    /*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
        	L.i(""+n, "");
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    /**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return downloadBitmap(url);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            imageCache.addBitmapToCache(url, bitmap);
            fileCache.saveBitmap(bitmap, url,""+type);
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                // Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
                if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
                	if(bitmap==null){
                		if(progressBar!=null){
        					progressBar.setVisibility(View.GONE);
        				}
                		imageView.setImageBitmap(loadingimage);
                		return;
                	}
                	if(progressBar!=null){
    					progressBar.setVisibility(View.GONE);
    				}
    				imageView.setImageBitmap(bitmap);    				
                }
            }
        }
    }


    /**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     *
     * <p>Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.</p>
     */
    static class DownloadedDrawable extends BitmapDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) { 
        	super(loadingimage);
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */
   
}
