package io.kowalski.claptrap;

public class Settings {

	private final int DEFAULT_HTTP_PORT = 4567;
	private final int DEFAULT_SMTP_PORT = 2525;
	private final int DEFAULT_INBOX_SIZE = 250;
	
	private int httpPort;
	private int smtpPort;
	
	private int inboxSize;
	
	private boolean debug;
	
	
	public Settings() {
		this.httpPort = DEFAULT_HTTP_PORT;
		this.smtpPort = DEFAULT_SMTP_PORT;
		this.inboxSize = DEFAULT_INBOX_SIZE;
		this.debug = false;
	}
	
	public final int getHttpPort() {
		return httpPort;
	}
	
	public final void setHttpPort(final int httpPort) {
		this.httpPort = httpPort;
	}
	
	public final int getSmtpPort() {
		return smtpPort;
	}
	
	public final void setSmtpPort(final int smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	public final int getInboxSize() {
		return inboxSize;
	}
	
	public final void setInboxSize(final int inboxSize) {
		this.inboxSize = inboxSize;
	}

	public final boolean isDebug() {
		return debug;
	}
	
	public final void setDebug(final boolean debug) {
		this.debug = debug;
	}
}

