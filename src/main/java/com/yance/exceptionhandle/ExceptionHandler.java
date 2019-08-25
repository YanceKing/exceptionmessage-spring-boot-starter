package com.yance.exceptionhandle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.yance.content.ExceptionNotice;
import com.yance.content.HttpExceptionNotice;
import com.yance.message.INoticeSendComponent;
import com.yance.pojos.ExceptionStatistics;
import com.yance.properties.ExceptionNoticeFrequencyStrategy;
import com.yance.properties.ExceptionNoticeProperty;
import com.yance.redis.ExceptionRedisStorageComponent;

/**
 * @author yance
 */
public class ExceptionHandler {

	private ExceptionRedisStorageComponent exceptionRedisStorageComponent;

	private final ExceptionNoticeProperty exceptionNoticeProperty;

	private final ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy;

	private final INoticeSendComponent sendComponent;

	private final Map<String, ExceptionStatistics> checkUid = Collections.synchronizedMap(new HashMap<>());

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionHandler(ExceptionNoticeProperty exceptionNoticeProperty, INoticeSendComponent sendComponent,
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		this.exceptionNoticeProperty = exceptionNoticeProperty;
		this.sendComponent = sendComponent;
		this.exceptionNoticeFrequencyStrategy = exceptionNoticeFrequencyStrategy;
	}

	/**
	 * @param exceptionRedisStorageComponent the exceptionRedisStorageComponent to
	 *                                       set
	 */
	public void setExceptionRedisStorageComponent(ExceptionRedisStorageComponent exceptionRedisStorageComponent) {
		this.exceptionRedisStorageComponent = exceptionRedisStorageComponent;
	}

	/**
	 * 最基础的异常通知的创建方法
	 * 
	 * @param exception 异常信息
	 * 
	 * @return
	 */
	public ExceptionNotice createNotice(RuntimeException exception) {
		if (containsException(exception)) {
			return null;
		}
		ExceptionNotice exceptionNotice = new ExceptionNotice(exception,
				exceptionNoticeProperty.getIncludedTracePackage(), null);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas)
			sendComponent.send(exceptionNotice);
		return exceptionNotice;

	}

	private boolean containsException(RuntimeException exception) {
		Class<? extends RuntimeException> thisEClass = exception.getClass();
		List<Class<? extends RuntimeException>> list = exceptionNoticeProperty.getExcludeExceptions();
		for (Class<? extends RuntimeException> clazz : list) {
			if (clazz.isAssignableFrom(thisEClass))
				return true;
		}
		return false;
	}

	/**
	 * 反射方式获取方法中出现的异常进行的通知
	 * 
	 * @param ex        异常信息
	 * @param method    方法名
	 * @param args      参数信息
	 * @return ExceptionNotice
	 */
	public ExceptionNotice createNotice(RuntimeException ex, String method, Object[] args) {
		if (containsException(ex))
			return null;
		ExceptionNotice exceptionNotice = new ExceptionNotice(ex, exceptionNoticeProperty.getIncludedTracePackage(),
				args);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas) {
			sendComponent.send(exceptionNotice);
		}
		return exceptionNotice;

	}

	/**
	 * 创建一个http请求异常的通知
	 * 
	 * @param exception
	 * @param url
	 * @param param
	 * @param requesBody
	 * @param headers
	 * @return HttpExceptionNotice
	 */
	public HttpExceptionNotice createHttpNotice(RuntimeException exception, String url, Map<String, String> param,
			String requesBody, Map<String, String> headers) {
		if (containsException(exception))
			return null;
		HttpExceptionNotice exceptionNotice = new HttpExceptionNotice(exception,
				exceptionNoticeProperty.getIncludedTracePackage(), url, param, requesBody, headers);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas)
			sendComponent.send(exceptionNotice);
		return exceptionNotice;
	}

	private boolean persist(ExceptionNotice exceptionNotice) {
		Boolean needNotice = false;
		String uid = exceptionNotice.getUid();
		ExceptionStatistics exceptionStatistics = checkUid.get(uid);
		logger.debug(exceptionStatistics);
		if (exceptionStatistics != null) {
			Long count = exceptionStatistics.plusOne();
			if (exceptionNoticeFrequencyStrategy.getEnabled()) {
				if (stratergyCheck(exceptionStatistics, exceptionNoticeFrequencyStrategy)) {
					LocalDateTime now = LocalDateTime.now();
					exceptionNotice.setLatestShowTime(now);
					exceptionNotice.setShowCount(count);
					exceptionStatistics.setLastShowedCount(count);
					exceptionStatistics.setNoticeTime(now);
					needNotice = true;
				}
			}
		} else {
			exceptionStatistics = new ExceptionStatistics(uid);
			synchronized (exceptionStatistics) {
				checkUid.put(uid, exceptionStatistics);
				needNotice = true;
			}
		}
		if (exceptionRedisStorageComponent != null)
			exceptionRedisStorageComponent.save(exceptionNotice);
		return needNotice;
	}

	private boolean stratergyCheck(ExceptionStatistics exceptionStatistics,
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		switch (exceptionNoticeFrequencyStrategy.getFrequencyType()) {
		case TIMEOUT:
			Duration dur = Duration.between(exceptionStatistics.getNoticeTime(), LocalDateTime.now());
			return exceptionNoticeFrequencyStrategy.getNoticeTimeInterval().compareTo(dur) < 0;
		case SHOWCOUNT:
			return exceptionStatistics.getShowCount().longValue() - exceptionStatistics.getLastShowedCount()
					.longValue() > exceptionNoticeFrequencyStrategy.getNoticeShowCount().longValue();
		}
		return false;
	}

	@Scheduled(cron = "0 25 0 * * * ")
	public void resetCheck() {
		checkUid.clear();
	}
}
