package io.kowalski.claptrap.selenium;

import io.kowalski.claptrap.smtp.Message;
import io.kowalski.condottieri.Condottieri;
import io.kowalski.condottieri.Condottiero;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Claptrap4Selenium {

	private Claptrap4Selenium() {

	}

	public static Message readEmail(String host, int port,
			EmailSearchCriteria criteria) {

		Condottiero client = Condottieri.condottiero();

		String uri = "http://" + host + ":" + port + "/emails/"
				+ criteria.getServer();
		Type messageListType = new TypeToken<List<Message>>() {
		}.getType();

		while (true) {
			@SuppressWarnings("unchecked")
			List<Message> messages = (List<Message>) client.get().uri(uri)
					.execute().unmarshalJsonAsList(messageListType);

			for (Message m : messages) {
				if (meetsCriteria(m, criteria)) {
					return m;
				}
			}
		}
	}

	private static boolean meetsCriteria(final Message msg,
			final EmailSearchCriteria criteria) {
		String subject = msg.getSubject().toLowerCase();
		String criteriaSubject = criteria.getSubject().toLowerCase();
		boolean containsSubject = subject.contains(criteriaSubject);

		String body = msg.getBody().toLowerCase();
		String criteriaBody = criteria.getBody().toLowerCase();
		boolean containsBody = body.contains(criteriaBody);

		String sender = msg.getFrom().toLowerCase();
		String criteriaSender = criteria.getSender().toLowerCase();
		boolean containsSender = sender.contains(criteriaSender);

		String recipient = msg.getRecipient().toLowerCase();
		String criteriaRecipient = criteria.getRecipient().toLowerCase();
		boolean containsRecipient = recipient.contains(criteriaRecipient);

		boolean receivedAfter = false;

		if (criteria.getReceivedBefore() != null) {
			receivedAfter = criteria.getReceivedBefore()
					.before(msg.getReceived());
		}

		return containsSubject && containsBody && containsSender
				&& containsRecipient && receivedAfter;
	}

}
