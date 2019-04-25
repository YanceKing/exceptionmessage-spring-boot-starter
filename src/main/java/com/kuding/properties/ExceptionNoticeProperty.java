package com.kuding.properties;

import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.kuding.properties.enums.NoticeType;

@ConfigurationProperties(prefix = "exceptionnotice")
public class ExceptionNoticeProperty {

	/**
	 * 是否开启异常通知
	 */
	private boolean openNotice = false;

	/**
	 * 过滤信息的关键字
	 */
	private String filterTrace;

	/**
	 * 异常工程名
	 */
	private String projectName;

	/**
	 * 通过注解进行监控
	 */
	private boolean enableCheckAnnotation = true;

	/**
	 * 开启redis存储
	 */
	private boolean enableRedisStorage;

	/**
	 * redis的键
	 */
	private String redisKey;

	/**
	 * 保留时间（小时），此字段暂时没用，开发中
	 */
	private long expireTime;

	/**
	 * 通知类型
	 */
	private NoticeType noticeType;

	/**
	 * 排除的需要统计的异常
	 */
	private List<Class<? extends RuntimeException>> excludeExceptions = new LinkedList<>();

	/**
	 * @return the filterTrace
	 */
	public String getFilterTrace() {
		return filterTrace;
	}

	/**
	 * @param filterTrace the filterTrace to set
	 */
	public void setFilterTrace(String filterTrace) {
		this.filterTrace = filterTrace;
	}

	/**
	 * @return the enableCheckAnnotation
	 */
	public boolean isEnableCheckAnnotation() {
		return enableCheckAnnotation;
	}

	/**
	 * @param enableCheckAnnotation the enableCheckAnnotation to set
	 */
	public void setEnableCheckAnnotation(boolean enableCheckAnnotation) {
		this.enableCheckAnnotation = enableCheckAnnotation;
	}

	/**
	 * @return the enableRedisStorage
	 */
	public boolean isEnableRedisStorage() {
		return enableRedisStorage;
	}

	/**
	 * @param enableRedisStorage the enableRedisStorage to set
	 */
	public void setEnableRedisStorage(boolean enableRedisStorage) {
		this.enableRedisStorage = enableRedisStorage;
	}

	/**
	 * @return the expireTime
	 */
	public long getExpireTime() {
		return expireTime;
	}

	/**
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * @return the noticeType
	 */
	public NoticeType getNoticeType() {
		return noticeType;
	}

	/**
	 * @param noticeType the noticeType to set
	 */
	public void setNoticeType(NoticeType noticeType) {
		this.noticeType = noticeType;
	}

	/**
	 * @return the excludeExceptions
	 */
	public List<Class<? extends RuntimeException>> getExcludeExceptions() {
		return excludeExceptions;
	}

	/**
	 * @param excludeExceptions the excludeExceptions to set
	 */
	public void setExcludeExceptions(List<Class<? extends RuntimeException>> excludeExceptions) {
		this.excludeExceptions = excludeExceptions;
	}

	/**
	 * @return the redisKey
	 */
	public String getRedisKey() {
		return redisKey;
	}

	/**
	 * @param redisKey the redisKey to set
	 */
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the openNotice
	 */
	public boolean isOpenNotice() {
		return openNotice;
	}

	/**
	 * @param openNotice the openNotice to set
	 */
	public void setOpenNotice(boolean openNotice) {
		this.openNotice = openNotice;
	}

	
	
}
