package com.kuding.aop;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.kuding.exceptionhandle.ExceptionHandler;

@Aspect
public class ExceptionNoticeAop {

	private ExceptionHandler exceptionHandler;

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionNoticeAop(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@AfterThrowing(value = "@within(com.kuding.anno.ExceptionListener)", throwing = "e")
	public void exceptionNotice(JoinPoint joinPoint, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	@AfterThrowing(value = "@annotation(com.kuding.anno.ExceptionListener)", throwing = "e")
	public void exceptionNoticeWithMethod(JoinPoint joinPoint, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	private void handleException(RuntimeException exception, String methodName, Object[] args) {
		logger.debug("出现异常：" + methodName
				+ String.join(",", Arrays.stream(args).map(x -> x.toString()).toArray(String[]::new)));
		exceptionHandler.createNotice(exception, methodName, args);
	}
}
