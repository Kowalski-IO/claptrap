package io.kowalski.claptrap.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.mail.internet.MimeMessage;

import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.mapdb.DBMaker;
import org.mapdb.TxMaker;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import io.kowalski.claptrap.models.Email;
import io.kowalski.claptrap.services.BroadcastService;
import io.kowalski.claptrap.services.impl.EmailBroadcastService;

@Singleton
public class GuiceModule extends AbstractModule {

    private final TxMaker txMaker;
    private final BlockingQueue<MimeMessage> mimeMessageQueue;
    private final ConcurrentHashMap<String, SseBroadcaster> broadcasterMap;

    public GuiceModule() {
        txMaker = DBMaker.memoryDB().makeTxMaker();
        mimeMessageQueue = new ArrayBlockingQueue<MimeMessage>(1024);
        broadcasterMap = new ConcurrentHashMap<String, SseBroadcaster>();
    }

    @Override
    protected void configure() {
        bindInstances();
        bindInterfaces();
    }

    private void bindInstances() {
        bind(TxMaker.class).toInstance(txMaker);
        bind(new TypeLiteral<BlockingQueue<MimeMessage>>(){}).toInstance(mimeMessageQueue);
        bind(new TypeLiteral<ConcurrentHashMap<String, SseBroadcaster>>(){}).toInstance(broadcasterMap);
    }

    private void bindInterfaces() {
        bind(new TypeLiteral<BroadcastService<Email>>(){}).to(EmailBroadcastService.class);
    }

}