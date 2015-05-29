package io.kowalski.claptrap;

import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	public static void main(String[] args) {
		String to = "@kowalski.io";
		String from = "claptrap-test@kowalski.io";


		String host = "localhost";

		Properties properties = System.getProperties();

		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", "5555");

		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));			

			message.setSubject("This is the Subject Line!");
			message.setText("This is the actual message");

			for (int i = 0; i < 100; i++) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					UUID.randomUUID().toString() + to));
				Transport.send(message);
			}

			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
