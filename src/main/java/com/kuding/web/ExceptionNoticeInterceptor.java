package com.kuding.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kuding.anno.ExceptionListener;
import com.kuding.exceptionhandle.ExceptionHandler;

public class ExceptionNoticeInterceptor implements HandlerInterceptor {

	private ExceptionHandler exceptionHandler;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		RuntimeException e = null;
		if (ex instanceof RuntimeException)
			e = (RuntimeException) ex;
		HandlerMethod handlerMethod = null;
		if (handler instanceof HandlerMethod)
			handlerMethod = (HandlerMethod) handler;
		ExceptionListener listener = handlerMethod.getMethodAnnotation(ExceptionListener.class);
//		if(listener == null)
//			listener = handlerMethod.getBeanType().getAnnotation(ExceptionHandler)
//		if (e != null && handler != null) {
//			exceptionHandler.createHttpNotice(blamedFor, exception, url, param, requesBody, headers)
//		}

	}

	private ExceptionListener getListener(HandlerMethod handlerMethod) {
		//TODO 接着做
		return null;
	}

}
