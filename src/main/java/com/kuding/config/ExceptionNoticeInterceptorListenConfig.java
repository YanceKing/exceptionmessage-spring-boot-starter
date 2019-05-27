package com.kuding.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.web.CurrentRequestHeaderResolver;
import com.kuding.web.CurrentRequetBodyResolver;
import com.kuding.web.DefaultRequestBodyResoulver;
import com.kuding.web.DefaultRequestHeaderResolver;
import com.kuding.web.ExceptionNoticeResolver;

@Configuration
@ConditionalOnClass({ WebMvcConfigurer.class, RequestBodyAdvice.class, RequestMappingHandlerAdapter.class })
@ConditionalOnProperty(name = "exceptionnotice.listen-type", havingValue = "interceptor")
public class ExceptionNoticeInterceptorListenConfig implements WebMvcConfigurer {

	@Autowired
	private ExceptionHandler exceptionHandler;

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(0, exceptionNoticeResolver());
	}

	@Bean
	public ExceptionNoticeResolver exceptionNoticeResolver() {
		ExceptionNoticeResolver exceptionNoticeResolver = new ExceptionNoticeResolver(exceptionHandler,
				currentRequetBodyResolver(), currentRequestHeaderResolver());
		return exceptionNoticeResolver;
	}

	@Bean
	@ConditionalOnMissingBean(value = CurrentRequestHeaderResolver.class)
	public CurrentRequestHeaderResolver currentRequestHeaderResolver() {
		return new DefaultRequestHeaderResolver();
	}

	@Bean
	@ConditionalOnMissingBean(value = CurrentRequetBodyResolver.class)
	public CurrentRequetBodyResolver currentRequetBodyResolver() {
		return new DefaultRequestBodyResoulver();
	}

}
