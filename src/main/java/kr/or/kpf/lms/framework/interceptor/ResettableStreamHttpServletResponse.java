package kr.or.kpf.lms.framework.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResettableStreamHttpServletResponse extends HttpServletResponseWrapper {

	private String requestId;
	String payloadFilePrefix;
	String payloadTarget;

	private List<Byte> rawData = new ArrayList<>();
	private HttpServletResponse response;
	private ResettableServletOutputStream servletStream;

	ResettableStreamHttpServletResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.response = response;
		this.servletStream = new ResettableServletOutputStream(this);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return servletStream;
	}

	public PrintWriter getWriter() throws IOException {
		String encoding = getCharacterEncoding();
		if (encoding != null) {
			return new PrintWriter(new OutputStreamWriter(servletStream, encoding));
		} else {
			return new PrintWriter(new OutputStreamWriter(servletStream));
		}
	}

	public String getRequestId() { return requestId; }
	public void setRequestId(String requestId) { this.requestId = requestId; }

	public String getPayloadFilePrefix() { return payloadFilePrefix; }
	public void setPayloadFilePrefix(String payloadFilePrefix) { this.payloadFilePrefix = payloadFilePrefix; }

	public String getPayloadTarget() { return payloadTarget; }
	public void setPayloadTarget(String payloadTarget) { this.payloadTarget = payloadTarget; }

	public List<Byte> getRawData() { return rawData; }
	public void setRawData(List<Byte> rawData) { this.rawData = rawData; }

	public HttpServletResponse getResponse() { return response; }
	public void setResponse(HttpServletResponse response) { this.response = response; }

	public ResettableServletOutputStream getServletStream() { return servletStream; }
	public void setServletStream(ResettableServletOutputStream servletStream) { this.servletStream = servletStream; }
	
	
}
