package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.services.ContactStorage;
import io.kowalski.claptrap.services.EmailStorage;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

public class HandlerFactory implements MessageHandlerFactory {

    private final ContactStorage contactStorage;
    private final EmailStorage emailStorage;

    HandlerFactory(ContactStorage contactStorage, EmailStorage emailStorage) {
        this.contactStorage = contactStorage;
        this.emailStorage = emailStorage;
    }

    @Override
    public MessageHandler create(MessageContext messageContext) {
        return new Handler(contactStorage, emailStorage);
    }

}
