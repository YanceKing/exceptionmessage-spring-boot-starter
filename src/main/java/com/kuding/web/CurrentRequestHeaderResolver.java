package com.kuding.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CurrentRequestHeaderResolver {

	default Map<String, String> headers(HttpServletRequest httpRequest) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> enumeration = httpRequest.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String str = enumeration.nextElement();
			map.put(str, httpRequest.getHeader(str));
		}
		return map;
	}
}
