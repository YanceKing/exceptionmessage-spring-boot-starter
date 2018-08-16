package com.kuding.properties;

import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.kuding.properties.enums.NoticeType;

@ConfigurationProperties(prefix = "exceptionnotice")
public class ExceptionNoticeProperty {

	/**
	 * 过滤信息的关键字
	 */
	private String filterTrace = "com.kuding";

	/**
	 * 异常工程名
	 */
	private String projectName;

	/**
	 * 电话信息
	 */
	private String phoneNum;

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
	 * 保留时间（小时）
	 */
	private long expireTime;

	/**
	 * 通知类型
	 */
	private NoticeType noticeType;

	/**
	 * web钩子
	 */
	private String webHook;

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
	 * @return the webHook
	 */
	public String getWebHook() {
		return webHook;
	}

	/**
	 * @param webHook the webHook to set
	 */
	public void setWebHook(String webHook) {
		this.webHook = webHook;
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
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExceptionNoticeProperty [filterTrace=" + filterTrace + ", projectName=" + projectName + ", phoneNum="
				+ phoneNum + ", enableCheckAnnotation=" + enableCheckAnnotation + ", enableRedisStorage="
				+ enableRedisStorage + ", redisKey=" + redisKey + ", expireTime=" + expireTime + ", noticeType="
				+ noticeType + ", webHook=" + webHook + ", excludeExceptions=" + excludeExceptions + "]";
	}

}
