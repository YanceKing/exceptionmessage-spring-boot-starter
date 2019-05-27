package com.kuding.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.kuding.anno.ExceptionListener;
import com.kuding.content.HttpExceptionNotice;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.properties.ExceptionNoticeProperty;

public class ExceptionNoticeResolver implements HandlerExceptionResolver {

	private ExceptionHandler exceptionHandler;

	private ExceptionNoticeProperty exceptionNoticeProperty;

	private CurrentRequetBodyResolver currentRequetBodyResolver;

	private CurrentRequestHeaderResolver currentRequestHeaderResolver;

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionNoticeResolver(ExceptionHandler exceptionHandler,
			CurrentRequetBodyResolver currentRequetBodyResolver,
			CurrentRequestHeaderResolver currentRequestHeaderResolver,
			ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionHandler = exceptionHandler;
		this.currentRequestHeaderResolver = currentRequestHeaderResolver;
		this.currentRequetBodyResolver = currentRequetBodyResolver;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		RuntimeException e = null;
		if (ex instanceof RuntimeException)
			e = (RuntimeException) ex;
		HandlerMethod handlerMethod = null;
		if (handler instanceof HandlerMethod)
			handlerMethod = (HandlerMethod) handler;
		ExceptionListener listener = getListener(handlerMethod);
		if (listener != null && e != null && handler != null) {
			HttpExceptionNotice exceptionNotice = exceptionHandler.createHttpNotice(listener.value(), e,
					request.getRequestURI(), getParames(request), getRequestBody(), getHeader(request));
			logger.debug(exceptionNotice);
		}
		return null;
	}

	private Map<String, String> getParames(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		request.getParameterMap().forEach((x, y) -> map.put(x, String.join(" , ", Arrays.asList(y))));
		return map;
	}

	private String getRequestBody() {
		return currentRequetBodyResolver.getRequestBody();
	}

	private Map<String, String> getHeader(HttpServletRequest request) {
		return currentRequestHeaderResolver.headers(request, exceptionNoticeProperty.getIncludeHeaderName());
	}

	private ExceptionListener getListener(HandlerMethod handlerMethod) {
		ExceptionListener listener = handlerMethod.getMethodAnnotation(ExceptionListener.class);
		if (listener == null)
			listener = handlerMethod.getBeanType().getAnnotation(ExceptionListener.class);
		return listener;
	}
}
