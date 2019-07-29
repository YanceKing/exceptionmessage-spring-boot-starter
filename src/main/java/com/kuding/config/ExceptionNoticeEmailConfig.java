package com.kuding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.kuding.message.EmailNoticeSendComponent;
import com.kuding.properties.EmailExceptionNoticeProperty;

@Configuration
@AutoConfigureAfter({ MailSenderAutoConfiguration.class })
@ConditionalOnBean({ MailSender.class, MailProperties.class })
@ConditionalOnProperty(value = "exceptionnotice.notice-type", havingValue = "email")
@EnableConfigurationProperties({ EmailExceptionNoticeProperty.class })
public class ExceptionNoticeEmailConfig {

	@Autowired
	private MailSender mailSender;
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private EmailExceptionNoticeProperty emailExceptionNoticeProperty;

	@Bean
	@ConditionalOnMissingBean
	public EmailNoticeSendComponent emailNoticeSendComponent() {
		EmailNoticeSendComponent component = new EmailNoticeSendComponent(mailSender, mailProperties,
				emailExceptionNoticeProperty);
		return component;
	}
}
