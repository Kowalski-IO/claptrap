package io.kowalski.claptrap.selenium;

import java.util.Date;

public class EmailSearchCriteria {
	
	private final String server;
	private String sender = "";
	private String recipient = "";
	private String subject = "";
	private String body = "";
	
	private Date receivedBefore = null;
	
	public EmailSearchCriteria(String server) {
		this.server = server;
	}

	public final String getServer() {
		return server;
	}

	public final String getSender() {
		return sender;
	}

	public final void setSender(final String sender) {
		this.sender = sender;
	}

	public final String getRecipient() {
		return recipient;
	}

	public final void setRecipient(final String recipient) {
		this.recipient = recipient;
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

	public final Date getReceivedBefore() {
		return receivedBefore;
	}

	public final void setReceivedBefore(final Date receivedBefore) {
		this.receivedBefore = receivedBefore;
	}

}
