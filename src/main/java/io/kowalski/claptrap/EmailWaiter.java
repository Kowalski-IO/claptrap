package io.kowalski.claptrap;

import io.kowalski.claptrap.selenium.Claptrap4Selenium;
import io.kowalski.claptrap.selenium.EmailSearchCriteria;
import io.kowalski.claptrap.smtp.Message;

import java.util.Date;

public class EmailWaiter {

	public static void main(String[] args) {
		
		EmailSearchCriteria esc = new EmailSearchCriteria("kowalski.io");
		esc.setSubject("Password");
		esc.setBody("Your password is");
		esc.setSender("brandon@kowalski.io");
		esc.setRecipient("penis@pump.com");
		esc.setReceivedBefore(new Date());
		
		Message m = Claptrap4Selenium.readEmail("localhost", 4567, esc);
		
		
		System.out.println(m);
		
		
	}
	
}
