package com.yance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.yance.httpclient.SimpleHttpClient;
import com.yance.message.DingDingNoticeSendComponent;
import com.yance.message.INoticeSendComponent;
import com.yance.properties.DingDingExceptionNoticeProperty;
import com.yance.properties.ExceptionNoticeProperty;

@Configuration
@ConditionalOnProperty(value = "exceptionnotice.notice-type", havingValue = "dingding")
@ConditionalOnMissingBean({ INoticeSendComponent.class })
@EnableConfigurationProperties({ DingDingExceptionNoticeProperty.class })
public class DingdingExceptionNoticeConfig {

	@Autowired
	private Gson gson;

	@Bean
	public SimpleHttpClient simpleHttpClient() {
		SimpleHttpClient httpClient = new SimpleHttpClient(gson);
		return httpClient;
	}

	@Bean
	public DingDingNoticeSendComponent dingDingNoticeSendComponent(ExceptionNoticeProperty exceptionNoticeProperty,
			DingDingExceptionNoticeProperty dingDingExceptionNoticeProperty) {
		DingDingNoticeSendComponent dingDingNoticeSendComponent = new DingDingNoticeSendComponent(simpleHttpClient(),
				exceptionNoticeProperty, dingDingExceptionNoticeProperty);
		return dingDingNoticeSendComponent;
	}
}
