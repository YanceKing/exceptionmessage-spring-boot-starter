package com.kuding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;
import com.kuding.aop.ExceptionNoticeAop;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.httpclient.SimpleHttpClient;
import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.message.INoticeSendComponent;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.redis.ExceptionRedisStorageComponent;

@Configuration
@EnableConfigurationProperties({ ExceptionNoticeProperty.class })
@ConditionalOnMissingBean({ ExceptionHandler.class })
public class ExceptionNoticeConfig {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.notice-type", havingValue = "dingding")
	@ConditionalOnMissingBean(INoticeSendComponent.class)
	public INoticeSendComponent dingDingNoticeSendComponent(SimpleHttpClient simpleHttpClient) {
		INoticeSendComponent component = new DingDingNoticeSendComponent(simpleHttpClient, exceptionNoticeProperty);
		return component;
	}

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.enable-redis-storage", havingValue = "true")
	@ConditionalOnMissingBean(ExceptionRedisStorageComponent.class)
	public ExceptionRedisStorageComponent exceptionRedisStorageComponent(StringRedisTemplate stringRedisTemplate,
			Gson gson, ExceptionHandler exceptionHandler) {
		ExceptionRedisStorageComponent exceptionRedisStorageComponent = new ExceptionRedisStorageComponent(
				exceptionNoticeProperty, stringRedisTemplate, gson);
		exceptionHandler.setExceptionRedisStorageComponent(exceptionRedisStorageComponent);
		return exceptionRedisStorageComponent;
	}

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.enable-check-annotation", havingValue = "true")
	@ConditionalOnMissingBean(ExceptionNoticeAop.class)
	public ExceptionNoticeAop exceptionNoticeAop(ExceptionHandler exceptionHandler) {
		ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionHandler);
		return aop;
	}

	@Bean
	public ExceptionHandler exceptionHandler(INoticeSendComponent noticeSendComponent) {
		ExceptionHandler exceptionHandler = new ExceptionHandler(noticeSendComponent, exceptionNoticeProperty);
		return exceptionHandler;
	}

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.notice-type", havingValue = "dingding")
	public SimpleHttpClient simpleHttpClient(Gson gson) {
		SimpleHttpClient httpClient = new SimpleHttpClient(gson);
		return httpClient;
	}
}
