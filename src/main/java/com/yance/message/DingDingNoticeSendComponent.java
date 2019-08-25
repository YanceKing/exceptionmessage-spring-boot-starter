package com.yance.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yance.content.ExceptionNotice;
import com.yance.httpclient.SimpleHttpClient;
import com.yance.pojos.dingding.DingDingNotice;
import com.yance.pojos.dingding.DingDingResult;
import com.yance.properties.DingDingExceptionNoticeProperty;
import com.yance.properties.ExceptionNoticeProperty;

public class DingDingNoticeSendComponent implements INoticeSendComponent {

	private SimpleHttpClient simpleHttpClient;

	private final ExceptionNoticeProperty exceptionNoticeProperty;

	private final DingDingExceptionNoticeProperty dingDingExceptionNoticeProperty;

	private final Log logger = LogFactory.getLog(getClass());

	public DingDingNoticeSendComponent(SimpleHttpClient simpleHttpClient,
			ExceptionNoticeProperty exceptionNoticeProperty,
			DingDingExceptionNoticeProperty dingDingExceptionNoticeProperty) {
		this.simpleHttpClient = simpleHttpClient;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
		this.dingDingExceptionNoticeProperty = dingDingExceptionNoticeProperty;
	}

	/**
	 * @return the simpleHttpClient
	 */
	public SimpleHttpClient getSimpleHttpClient() {
		return simpleHttpClient;
	}

	/**
	 * @return the exceptionNoticeProperty
	 */
	public ExceptionNoticeProperty getExceptionNoticeProperty() {
		return exceptionNoticeProperty;
	}

	/**
	 * @param simpleHttpClient the simpleHttpClient to set
	 */
	public void setSimpleHttpClient(SimpleHttpClient simpleHttpClient) {
		this.simpleHttpClient = simpleHttpClient;
	}

	/**
	 * @return the dingDingExceptionNoticeProperty
	 */
	public DingDingExceptionNoticeProperty getDingDingExceptionNoticeProperty() {
		return dingDingExceptionNoticeProperty;
	}

	@Override
	public void send(ExceptionNotice exceptionNotice) {
		DingDingNotice dingDingNotice = new DingDingNotice(exceptionNotice.createText(),
				dingDingExceptionNoticeProperty.getPhoneNum());
		DingDingResult result = simpleHttpClient.post(dingDingExceptionNoticeProperty.getWebHook(), dingDingNotice,
				DingDingResult.class);
		logger.debug(result);
	}
}
