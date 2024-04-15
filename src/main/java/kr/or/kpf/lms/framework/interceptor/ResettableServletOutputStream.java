package kr.or.kpf.lms.framework.interceptor;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@SuppressWarnings("unused")
public class ResettableServletOutputStream extends ServletOutputStream {

	@Autowired
	HttpInterceptor httpInterceptor;

	private OutputStream outputStream;
	private ResettableStreamHttpServletResponse wrappedResponse;
	private ServletOutputStream servletOutputStream = new ServletOutputStream() {
		boolean isFinished = false;
		boolean isReady = true;
		WriteListener writeListener = null;

		@Override
		public void setWriteListener(WriteListener writeListener) { this.writeListener = writeListener; }

		public boolean isReady() { return isReady; }

		@Override
		public void write(int w) throws IOException {
			outputStream.write(w);
			List<Byte> rawData = wrappedResponse.getRawData();
			Integer byteOfInteger = Integer.valueOf(w);
			rawData.add(byteOfInteger.byteValue());
		}
	};
	
	@Override
	public void close() throws IOException {
		outputStream.close();
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		servletOutputStream.setWriteListener(writeListener);
	}

	@Override
	public boolean isReady() {
		return servletOutputStream.isReady();
	}

	@Override
	public void write(int w) throws IOException {
		servletOutputStream.write(w);
	}

	public ResettableServletOutputStream(ResettableStreamHttpServletResponse wrappedResponse) throws IOException {
		this.outputStream = wrappedResponse.getResponse().getOutputStream();
		this.wrappedResponse = wrappedResponse;
	}

}
