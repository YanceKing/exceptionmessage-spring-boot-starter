package com.kuding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSender;

import com.google.gson.Gson;
import com.kuding.aop.ExceptionNoticeAop;
import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.httpclient.SimpleHttpClient;
import com.kuding.message.DingDingNoticeSendComponent;
import com.kuding.message.INoticeSendComponent;
import com.kuding.properties.DingDingExceptionNoticeProperty;
import com.kuding.properties.EmailExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeFrequencyStrategy;
import com.kuding.properties.ExceptionNoticeProperty;
import com.kuding.redis.ExceptionRedisStorageComponent;

@Configuration
@EnableConfigurationProperties({ ExceptionNoticeProperty.class, ExceptionNoticeFrequencyStrategy.class })
@ConditionalOnMissingBean({ ExceptionHandler.class })
public class ExceptionNoticeConfig {

	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@Bean
	@ConditionalOnProperty(name = "exceptionnotice.enable-redis-storage", havingValue = "true")
	@ConditionalOnClass({ StringRedisTemplate.class })
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
	@ConditionalOnMissingBean({ ExceptionHandler.class })
	public ExceptionHandler exceptionHandler(INoticeSendComponent noticeSendComponent) {
		//TODO 准备完善这里
		ExceptionHandler exceptionHandler = new ExceptionHandler(exceptionNoticeProperty);
		return exceptionHandler;
	}

	@Bean
	public SimpleHttpClient simpleHttpClient(Gson gson) {
		SimpleHttpClient httpClient = new SimpleHttpClient(gson);
		return httpClient;
	}

}
