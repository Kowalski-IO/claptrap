package io.kowalski.claptrap.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.TooMuchDataException;

public class SMTPMessageHandler implements MessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SMTPMessageHandler.class);

	private final BlockingQueue<MimeMessage> mimeMessageQueue;
	private MimeMessage mimeMessage;

	public SMTPMessageHandler(final BlockingQueue<MimeMessage> mimeMessageQueue) {
		this.mimeMessageQueue = mimeMessageQueue;
	}

	@Override
	public void from(final String from) throws RejectException {
		// Leaving unimplemented. Not needed.
	}

	@Override
	public void recipient(final String recipient) throws RejectException {
		// Leaving unimplemented. Not needed.
	}

	@Override
	public void data(final InputStream data) throws RejectException, TooMuchDataException, IOException {
		try {
			mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()), data);
		} catch (final MessagingException e) {
			LOGGER.error("Unable to handle Email data. Aborting...", e);
			mimeMessage = null;
			return;
		}
	}

	@Override
	public void done() {
		if (mimeMessage != null) {
			mimeMessageQueue.add(mimeMessage);
		}
	}

}
