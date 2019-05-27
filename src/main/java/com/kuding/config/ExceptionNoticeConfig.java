package com.kuding.config;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.message.INoticeSendComponent;
import com.kuding.properties.DingDingExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeFrequencyStrategy;
import com.kuding.properties.ExceptionNoticeProperty;

@Configuration
@EnableConfigurationProperties({ ExceptionNoticeProperty.class, ExceptionNoticeFrequencyStrategy.class })
@ConditionalOnMissingBean({ ExceptionHandler.class })
@ConditionalOnProperty(name = "exceptionnotice.open-notice", havingValue = "true")
@EnableScheduling
public class ExceptionNoticeConfig {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Autowired
	private ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy;

	@Autowired(required = false)
	private INoticeSendComponent noticeSendComponent;

	@Autowired
	private Gson gson;

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.listen-type", havingValue = "aop", matchIfMissing = true)
	@ConditionalOnMissingBean(ExceptionNoticeAop.class)
	public ExceptionNoticeAop exceptionNoticeAop(ExceptionHandler exceptionHandler) {
		ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionHandler);
		return aop;
	}

	@Bean
	@ConditionalOnMissingBean({ ExceptionHandler.class })
	public ExceptionHandler exceptionHandler() {
		Map<String, DingDingExceptionNoticeProperty> dingding = exceptionNoticeProperty.getDingding();
		List<INoticeSendComponent> list = new LinkedList<INoticeSendComponent>();
		if (noticeSendComponent != null)
			list.add(noticeSendComponent);
		if (dingding != null && dingding.size() > 0) {
			DingDingNoticeSendComponent component = new DingDingNoticeSendComponent(simpleHttpClient(),
					exceptionNoticeProperty, dingding);
			list.add(component);
		}

		ExceptionHandler exceptionHandler = new ExceptionHandler(exceptionNoticeProperty, list,
				exceptionNoticeFrequencyStrategy);
		return exceptionHandler;
	}

	@Bean
	public SimpleHttpClient simpleHttpClient() {
		SimpleHttpClient httpClient = new SimpleHttpClient(gson);
		return httpClient;
	}

}
