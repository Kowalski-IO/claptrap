package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.services.StorageService;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

public class HandlerFactory implements MessageHandlerFactory {

    private final StorageService storageService;

    HandlerFactory(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public MessageHandler create(MessageContext messageContext) {
        return new Handler(storageService);
    }

}
