package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.configuration.Constants;
import io.kowalski.claptrap.services.StorageService;
import org.subethamail.smtp.server.SMTPServer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    @Inject
    public Server(@Named(Constants.SMTP_SERVER_PORT) int serverPort,
                  StorageService storageService) {
        SMTPServer
                .port(serverPort)
                .connectionTimeout(1, TimeUnit.MINUTES)
                .messageHandlerFactory(new HandlerFactory(storageService))
                .executorService(Executors.newFixedThreadPool(8))
                .softwareName("Claptrap 3.0.0")
                .build()
                .start();
    }
}
