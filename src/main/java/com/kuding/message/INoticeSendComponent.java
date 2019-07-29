package com.kuding.message;

import com.kuding.content.ExceptionNotice;

@FunctionalInterface
public interface INoticeSendComponent {

	public void send(ExceptionNotice exceptionNotice);

}
