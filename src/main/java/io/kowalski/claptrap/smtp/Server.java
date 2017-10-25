package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.services.ContactStorage;
import io.kowalski.claptrap.services.EmailStorage;
import org.subethamail.smtp.server.SMTPServer;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    @Inject
    public Server(ContactStorage contactStorage, EmailStorage emailStorage) {
        SMTPServer
                .port(2525)
                .connectionTimeout(1, TimeUnit.MINUTES)
                .messageHandlerFactory(new HandlerFactory(contactStorage, emailStorage))
                .executorService(Executors.newFixedThreadPool(8))
                .softwareName("Claptrap 3.0.0")
                .build()
                .start();
    }
}
