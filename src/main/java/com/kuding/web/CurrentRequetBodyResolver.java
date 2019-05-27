package com.kuding.web;

import javax.servlet.http.HttpServletRequest;

public interface CurrentRequetBodyResolver {

	default String getRequestBody(HttpServletRequest request) {
		return "";
	}
}
