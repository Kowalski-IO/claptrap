package io.kowalski.claptrap.smtp;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

public class DefaultMessageHandlerFactory implements MessageHandlerFactory {

	@Override
	public MessageHandler create(MessageContext arg0) {
		return new Message();
	}

}
