package com.yance.message;

import com.yance.content.ExceptionNotice;

@FunctionalInterface
public interface INoticeSendComponent {

	public void send(ExceptionNotice exceptionNotice);

}
