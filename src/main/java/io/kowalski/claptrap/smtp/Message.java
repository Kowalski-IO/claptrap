package io.kowalski.claptrap.smtp;

import io.kowalski.claptrap.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.TooMuchDataException;

public class Message implements MessageHandler, Serializable, Comparable<Message> {

	private static final long serialVersionUID = 110539019912757560L;

	private final UUID uuid = UUID.randomUUID();
	private String from;
	private String recipient;
	private String subject;
	private String body;
	private Date received;

	@Override
	public final void data(final InputStream data) throws RejectException,
			TooMuchDataException, IOException {
		
		Session session = Session.getDefaultInstance(new Properties());
				 
		try {
			MimeMessage message = new MimeMessage(session, data);
			setSubject(message.getSubject());
			setBody(message.getContent().toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public final void done() {
		setReceived(new Date());
		String mapName = from.substring(from.indexOf('@') + 1);
		App.MEM_STORE.getServers().add(mapName);
		App.MEM_STORE.add(this, mapName);
	}

	@Override
	public final void from(final String from) throws RejectException {
		setFrom(from);
	}

	@Override
	public final void recipient(final String recipient) throws RejectException {
		setRecipient(recipient);
	}

	public final UUID getUuid() {
		return uuid;
	}

	public final String getFrom() {
		return from;
	}

	public final void setFrom(final String from) {
		this.from = from;
	}

	public final String getRecipient() {
		return recipient;
	}

	public final void setRecipient(final String recipient) {
		this.recipient = recipient;
	}

	public final Date getReceived() {
		return received;
	}

	public final void setReceived(final Date received) {
		this.received = received;
	}

	public final String getSubject() {
		return subject;
	}

	public final void setSubject(final String subject) {
		this.subject = subject;
	}

	public final String getBody() {
		return body;
	}

	public final void setBody(final String body) {
		this.body = body;
	}

	// Flipped the ordering to sort from new to old <3.
	@Override
	public int compareTo(Message o) {
		return o.received.compareTo(this.received);
	}

}
