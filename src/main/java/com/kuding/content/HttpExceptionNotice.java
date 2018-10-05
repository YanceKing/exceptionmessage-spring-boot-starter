package com.kuding.content;

import static java.util.stream.Collectors.toList;

import java.util.Map;

public class HttpExceptionNotice extends ExceptionNotice {

	public HttpExceptionNotice(RuntimeException exception, String filter, String url, Map<String, String> param,
			String requestBody) {
		super(exception, filter, null);
		this.url = url;
		this.paramInfo = param;
		this.requestBody = requestBody;
	}

	protected String url;

	protected Map<String, String> paramInfo;

	protected String requestBody;

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

	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kuding.content.ExceptionNotice#createText()
	 */
	@Override
	public String createText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("工程信息：").append(project).append("\r\n");
		stringBuilder.append("接口地址：").append(url).append("\r\n");
		if (paramInfo != null && paramInfo.size() > 0) {
			stringBuilder.append("接口参数：").append("\r\n")
					.append(String.join("\r\r", paramInfo.entrySet().stream()
							.map(x -> String.format("%s:%s", x.getKey(), x.getValue())).collect(toList())))
					.append("\r\n");
			if (requestBody != null) {
				stringBuilder.append("请求体数据：").append(requestBody).append("\r\n");
			}
		}
		stringBuilder.append("类路径：").append(classPath).append("\r\n");
		stringBuilder.append("方法名：").append(methodName).append("\r\n");
		if (parames != null && parames.size() > 0) {
			stringBuilder.append("参数信息：")
					.append(String.join(",", parames.stream().map(x -> x.toString()).collect(toList()))).append("\r\n");
		}
		stringBuilder.append("异常信息：").append(exceptionMessage).append("\r\n");
		stringBuilder.append("异常追踪：").append("\r\n").append(String.join("\r\n", traceInfo)).append("\r\n");
		return stringBuilder.toString();
	}

}
