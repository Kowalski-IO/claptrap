package io.kowalski.claptrap.handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.storage.BroadcastService;
import io.kowalski.claptrap.storage.email.EmailStorageService;

@Singleton
public class SMTPMessageHandlerFactory implements MessageHandlerFactory {

    final EmailStorageService emailStorageService;
    final BroadcastService<Email, String> emailBroadcastService;

    @Inject
    public SMTPMessageHandlerFactory(final EmailStorageService emailStorageService,
            final BroadcastService<Email, String> emailBroadcastService) {
        this.emailStorageService = emailStorageService;
        this.emailBroadcastService = emailBroadcastService;
    }

    @Override
    public MessageHandler create(final MessageContext messageContext) {
        return new SMTPMessageHandler(emailStorageService, emailBroadcastService);
    }

}
