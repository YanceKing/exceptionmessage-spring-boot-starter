package com.kuding.properties;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.kuding.properties.enums.ListenType;

@ConfigurationProperties(prefix = "exceptionnotice")
public class ExceptionNoticeProperty {

	/**
	 * 是否开启异常通知
	 */
	private Boolean openNotice;

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
	private ListenType listenType = ListenType.AOP;

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
	 * 排除的需要统计的异常
	 */
	private List<Class<? extends RuntimeException>> excludeExceptions = new LinkedList<>();

	/**
	 * 发送钉钉异常通知给谁
	 */
	Map<String, DingDingExceptionNoticeProperty> dingding;

	/**
	 * 发送邮件异常通知给谁
	 */
	Map<String, EmailExceptionNoticeProperty> email;

	/**
	 * @return the openNotice
	 */
	public Boolean getOpenNotice() {
		return openNotice;
	}

	/**
	 * @param openNotice the openNotice to set
	 */
	public void setOpenNotice(Boolean openNotice) {
		this.openNotice = openNotice;
	}

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

	public ListenType getListenType() {
		return listenType;
	}

	public void setListenType(ListenType listenType) {
		this.listenType = listenType;
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
	 * @return the dingding
	 */
	public Map<String, DingDingExceptionNoticeProperty> getDingding() {
		return dingding;
	}

	/**
	 * @param dingding the dingding to set
	 */
	public void setDingding(Map<String, DingDingExceptionNoticeProperty> dingding) {
		this.dingding = dingding;
	}

	/**
	 * @return the email
	 */
	public Map<String, EmailExceptionNoticeProperty> getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(Map<String, EmailExceptionNoticeProperty> email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ExceptionNoticeProperty [openNotice=" + openNotice + ", filterTrace=" + filterTrace + ", projectName="
				+ projectName + ", listenType=" + listenType + ", enableRedisStorage=" + enableRedisStorage
				+ ", redisKey=" + redisKey + ", expireTime=" + expireTime + ", excludeExceptions=" + excludeExceptions
				+ ", dingding=" + dingding + ", email=" + email + "]";
	}

}
