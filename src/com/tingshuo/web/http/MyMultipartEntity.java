package com.tingshuo.web.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;


public class MyMultipartEntity extends MultipartEntity {

	private ProgressListener listener;

	public MyMultipartEntity() {
		super();

	}

	//添加监视接口
	
	public void setPropressListener(final ProgressListener listener) {
		this.listener = listener;

	}

	public MyMultipartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public MyMultipartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	//上传时候添加过滤流以实现进度侦听
	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	//内部类的过滤流实现侦听上传进度
	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;//侦听进口
		private long transferred; //进度

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		//侦听
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}
		//侦听
		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}
