package com.kuding.content;

import java.util.Map;

public class HttpExceptionNotice extends ExceptionNotice {

	public HttpExceptionNotice(RuntimeException exception, String filter, String url, Map<String, String> param) {
		super(exception, filter, null);
		this.url = url;
		this.paramInfo = param;
	}

	protected String url;

	protected Map<String, String> paramInfo;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the paramInfo
	 */
	public Map<String, String> getParamInfo() {
		return paramInfo;
	}

	/**
	 * @param paramInfo the paramInfo to set
	 */
	public void setParamInfo(Map<String, String> paramInfo) {
		this.paramInfo = paramInfo;
	}

}
