package com.kuding.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.kuding.exceptionhandle.ExceptionHandler;
import com.kuding.message.EmailNoticeSendComponent;
import com.kuding.properties.EmailExceptionNoticeProperty;
import com.kuding.properties.ExceptionNoticeProperty;

@Configuration
@AutoConfigureAfter({ MailSenderAutoConfiguration.class, ExceptionNoticeConfig.class })
@ConditionalOnBean({ MailSender.class, MailProperties.class })
public class ExceptionNoticeEmailConfig {

	@Autowired
	private MailSender mailSender;
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private ExceptionHandler exceptionHandler;
	@Autowired
	private ExceptionNoticeProperty exceptionNoticeProperty;

	@PostConstruct
	public void emailNoticeSendComponent() {
		Map<String, EmailExceptionNoticeProperty> emails = exceptionNoticeProperty.getEmail();
		if (emails != null && emails.size() > 0) {
			EmailNoticeSendComponent component = new EmailNoticeSendComponent(mailSender, mailProperties, emails);
			exceptionHandler.registerNoticeSendComponent(component);
		}
	}
}
