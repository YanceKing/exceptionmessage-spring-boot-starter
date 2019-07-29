package com.kuding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.gson.Gson;
import com.kuding.aop.ExceptionNoticeAop;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.httpclient.SimpleHttpClient;
import com.kuding.message.INoticeSendComponent;
import com.kuding.properties.ExceptionNoticeFrequencyStrategy;
import com.kuding.properties.ExceptionNoticeProperty;

@Configuration
@EnableConfigurationProperties({ ExceptionNoticeProperty.class, ExceptionNoticeFrequencyStrategy.class })
@ConditionalOnMissingBean({ ExceptionHandler.class })
@ConditionalOnProperty(name = "exceptionnotice.open-notice", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class ExceptionNoticeConfig {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Autowired
	private Gson gson;

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.listen-type", havingValue = "common", matchIfMissing = true)
	@ConditionalOnMissingBean(ExceptionNoticeAop.class)
	public ExceptionNoticeAop exceptionNoticeAop(ExceptionHandler exceptionHandler) {
		ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionHandler);
		return aop;
	}

	@Bean
	@ConditionalOnMissingBean
	public ExceptionHandler exceptionHandler(INoticeSendComponent sendComponent,
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		ExceptionHandler exceptionHandler = new ExceptionHandler(exceptionNoticeProperty, sendComponent,
				exceptionNoticeFrequencyStrategy);
		return exceptionHandler;
	}

	@Bean
	public SimpleHttpClient simpleHttpClient() {
		SimpleHttpClient httpClient = new SimpleHttpClient(gson);
		return httpClient;
	}

}
