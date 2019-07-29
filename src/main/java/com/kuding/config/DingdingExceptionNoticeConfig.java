package com.kuding.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.properties.DingDingExceptionNoticeProperty;

@Configuration
@ConditionalOnProperty(value = "exceptionnotice.notice-type", havingValue = "dingding")
@EnableConfigurationProperties({ DingDingExceptionNoticeProperty.class })
public class DingdingExceptionNoticeConfig {

	//TODO 明天写
	@Bean
	public DingDingNoticeSendComponent dingDingNoticeSendComponent() {
		DingDingNoticeSendComponent dingDingNoticeSendComponent = new DingDingNoticeSendComponent(simpleHttpClient,
				exceptionNoticeProperty, dingDingExceptionNoticeProperty);
	}
}
