package com.kuding.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.kuding.exceptionhandle.ExceptionHandler;

@Aspect
public class ExceptionNoticeAop {

	private ExceptionHandler exceptionHandler;

	public ExceptionNoticeAop(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@AfterThrowing(value = "@within(com.kuding.anno.ExceptionLinstener)", throwing = "e")
	public void exceptionNotice(JoinPoint joinPoint, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	@AfterThrowing(value = "@annotation(com.kuding.anno.ExceptionLinstener)", throwing = "e")
	public void exceptionNoticeWithMethod(JoinPoint joinPoint, RuntimeException e) {
		handleException(e, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	private void handleException(RuntimeException exception, String methodName, Object[] args) {
		exceptionHandler.createNotice(exception, methodName, args);
	}
}
