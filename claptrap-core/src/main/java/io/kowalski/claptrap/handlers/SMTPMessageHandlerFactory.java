package io.kowalski.claptrap.handlers;

import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.internet.MimeMessage;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

@Singleton
public class SMTPMessageHandlerFactory implements MessageHandlerFactory {

    private final BlockingQueue<MimeMessage> mimeMessageQueue;

    @Inject
    public SMTPMessageHandlerFactory(final BlockingQueue<MimeMessage> mimeMessageQueue) {
        this.mimeMessageQueue = mimeMessageQueue;
    }

    @Override
    public MessageHandler create(final MessageContext messageContext) {
        return new SMTPMessageHandler(mimeMessageQueue);
    }
}
