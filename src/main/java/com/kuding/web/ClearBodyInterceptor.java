package com.kuding.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class ClearBodyInterceptor implements HandlerInterceptor {

	private CurrentRequetBodyResolver currentRequetBodyResolver;

	public ClearBodyInterceptor(CurrentRequetBodyResolver currentRequetBodyResolver) {
		this.currentRequetBodyResolver = currentRequetBodyResolver;
	}

	public ClearBodyInterceptor() {
	}

	/**
	 * @return the currentRequetBodyResolver
	 */
	public CurrentRequetBodyResolver getCurrentRequetBodyResolver() {
		return currentRequetBodyResolver;
	}

	/**
	 * @param currentRequetBodyResolver the currentRequetBodyResolver to set
	 */
	public void setCurrentRequetBodyResolver(CurrentRequetBodyResolver currentRequetBodyResolver) {
		this.currentRequetBodyResolver = currentRequetBodyResolver;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		currentRequetBodyResolver.remove();
	}

}
