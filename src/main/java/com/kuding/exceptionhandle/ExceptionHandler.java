package com.kuding.exceptionhandle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kuding.content.ExceptionNotice;
import com.kuding.content.HttpExceptionNotice;
import com.kuding.content.MultiTenantExceptionNotice;
import com.kuding.message.INoticeSendComponent;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.redis.ExceptionRedisStorageComponent;

@Component
public class ExceptionHandler {

	@Autowired(required = false)
	private ExceptionRedisStorageComponent exceptionRedisStorageComponent;

	@Autowired
	private INoticeSendComponent noticeSendComponent;

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	private Set<String> checkUid = Collections.synchronizedSet(new HashSet<>());

	/**
	 * @param exceptionRedisStorageComponent the exceptionRedisStorageComponent to
	 *                                       set
	 */
	public void setExceptionRedisStorageComponent(ExceptionRedisStorageComponent exceptionRedisStorageComponent) {
		this.exceptionRedisStorageComponent = exceptionRedisStorageComponent;
	}

	public ExceptionNotice createNotice(RuntimeException exception) {
		if (exceptionNoticeProperty.getExcludeExceptions().contains(exception.getClass()))
			return null;
		ExceptionNotice exceptionNotice = new ExceptionNotice(exception, exceptionNoticeProperty.getFilterTrace(),
				null);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		exceptionNotice.setNoticePhone(exceptionNoticeProperty.getPhoneNum());
		redisStore(exceptionNotice);
		messageSend(exceptionNotice);
		return exceptionNotice;

	}

	public ExceptionNotice createNotice(RuntimeException exception, String method, Object[] args) {
		if (exceptionNoticeProperty.getExcludeExceptions().contains(exception.getClass()))
			return null;
		ExceptionNotice exceptionNotice = new ExceptionNotice(exception, exceptionNoticeProperty.getFilterTrace(),
				args);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		exceptionNotice.setNoticePhone(exceptionNoticeProperty.getPhoneNum());
		redisStore(exceptionNotice);
		messageSend(exceptionNotice);
		return exceptionNotice;

	}

	public HttpExceptionNotice createHttpNotice(RuntimeException exception, String url, Map<String, String> param) {
		if (exceptionNoticeProperty.getExcludeExceptions().contains(exception.getClass()))
			return null;
		HttpExceptionNotice exceptionNotice = new HttpExceptionNotice(exception,
				exceptionNoticeProperty.getFilterTrace(), url, param);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		exceptionNotice.setNoticePhone(exceptionNoticeProperty.getPhoneNum());
		redisStore(exceptionNotice);
		messageSend(exceptionNotice);
		return exceptionNotice;
	}

	public MultiTenantExceptionNotice createHttpNotice(RuntimeException exception, String url,
			Map<String, String> param, String tenantId) {
		if (exceptionNoticeProperty.getExcludeExceptions().contains(exception.getClass()))
			return null;
		MultiTenantExceptionNotice exceptionNotice = new MultiTenantExceptionNotice(exception,
				exceptionNoticeProperty.getFilterTrace(), url, param, tenantId);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		exceptionNotice.setNoticePhone(exceptionNoticeProperty.getPhoneNum());
		redisStore(exceptionNotice);
		messageSend(exceptionNotice);
		return exceptionNotice;
	}

	private boolean redisStore(ExceptionNotice exceptionNotice) {
		if (exceptionRedisStorageComponent != null)
			return exceptionRedisStorageComponent.save(exceptionNotice);
		if (checkUid.contains(exceptionNotice.getUid()))
			return false;
		else {
			checkUid.add(exceptionNotice.getUid());
			return true;
		}
	}

	private void messageSend(ExceptionNotice exceptionNotice) {
		noticeSendComponent.send(exceptionNotice);
	}

}
