package com.kuding.content;

import java.util.Map;

public class MultiTenantExceptionNotice extends HttpExceptionNotice {

	public MultiTenantExceptionNotice(RuntimeException exception, String filter, String url, Map<String, String> param,
			String tenantId) {
		super(exception, filter, url, param);
		// TODO Auto-generated constructor stub
		this.tenantId = tenantId;
	}

	private String tenantId;

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MultiTenantExceptionNotice [tenantId=" + tenantId + ", url=" + url + ", paramInfo=" + paramInfo
				+ ", project=" + project + ", noticePhone=" + noticePhone + ", methodName=" + methodName + ", parames="
				+ parames + ", classPath=" + classPath + ", exceptionMessage=" + exceptionMessage + ", traceInfo="
				+ traceInfo + "]";
	}

}
