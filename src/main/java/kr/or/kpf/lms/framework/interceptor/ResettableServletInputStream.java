package kr.or.kpf.lms.framework.interceptor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResettableServletInputStream extends ServletInputStream {

	InputStream inputStream;
	
	public InputStream getInputStream() { return inputStream; }
	public void setInputStream(InputStream inputStream) { this.inputStream = inputStream; }
	
	@SuppressWarnings("unused")
	private ServletInputStream servletInputStream = new ServletInputStream() {
		boolean isFinished = false;
		boolean isReady = true;
		ReadListener readListener = null;

		public int read() throws IOException {
			int read = inputStream.read();
			isFinished = read == -1;
			isReady = !isFinished;
			return read;
		}

		@Override
		public boolean isFinished() { return isFinished; }

		@Override
		public boolean isReady() { return isReady; }

		@Override
		public void setReadListener(ReadListener readListener) { this.readListener = readListener; }
	};

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		servletInputStream.setReadListener(readListener);
	}
	
	public int readline(byte[] b, int off, int len) throws IOException {
		if (len <= 0) {
			return 0;
		}

		int count = 0;
		int c;

		while ((c = read()) != -1) {
			b[off++] = (byte) c;
			count++;
			if (c == '\n' || count == len) {
				break;
			}
		}

		return count > 0 ? count : -1;
	}

	public boolean isReady() { return servletInputStream.isReady(); }
	public boolean isFinished() { return servletInputStream.isFinished(); }
}
