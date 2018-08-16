package com.kuding.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kuding.content.ExceptionNotice;
import com.kuding.httpclient.SimpleHttpClient;
import com.kuding.pojos.dingding.DingDingNotice;
import com.kuding.pojos.dingding.DingDingResult;
import com.kuding.properties.ExceptionNoticeProperty;

public class DingDingNoticeSendComponent implements INoticeSendComponent {

	private SimpleHttpClient simpleHttpClient;

	private ExceptionNoticeProperty exceptionNoticeProperty;

	private final Log logger = LogFactory.getLog(getClass());

	public DingDingNoticeSendComponent(SimpleHttpClient simpleHttpClient,
			ExceptionNoticeProperty exceptionNoticeProperty) {
		this.simpleHttpClient = simpleHttpClient;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
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
	 * @param exceptionNoticeProperty the exceptionNoticeProperty to set
	 */
	public void setExceptionNoticeProperty(ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	@Override
	public void send(ExceptionNotice exceptionNotice) {
		DingDingNotice dingDingNotice = new DingDingNotice(exceptionNotice.createText(),
				exceptionNotice.getNoticePhone());
		DingDingResult result = simpleHttpClient.post(exceptionNoticeProperty.getWebHook(), dingDingNotice,
				DingDingResult.class);
		logger.debug(result);
	}

}
