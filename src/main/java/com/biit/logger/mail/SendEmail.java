package com.biit.logger.mail;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.biit.logger.BiitLogger;
import com.biit.logger.configuration.EmailConfigurationReader;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;

public class SendEmail {

	public static void sendEmail(List<String> mailToList, String subject, String htmlContent)
			throws EmailNotSentException, InvalidEmailAddressException {
		for (String mailTo : mailToList) {
			sendEmail(mailTo, subject, htmlContent);
			BiitLogger.info(SendEmail.class.getName(), "Sending email to " + mailTo);
		}
	}

	public static void sendEmail(String mailTo, String subject, String htmlContent) throws EmailNotSentException,
			InvalidEmailAddressException {
		sendEmail(EmailConfigurationReader.getInstance().getSmtpServer(), EmailConfigurationReader.getInstance()
				.getEmailUser(), EmailConfigurationReader.getInstance().getEmailPassword(), EmailConfigurationReader
				.getInstance().getEmailSender(), mailTo, subject, htmlContent);
	}

	/**
	 * Sends Mail
	 * 
	 * @param smtpServer
	 * @param emailUser
	 * @param emailPassword
	 * @param emailSender
	 * @param mailTo
	 * @param subject
	 * @param htmlContent
	 * @throws EmailNotSentException
	 * @throws InvalidEmailAddressException
	 */
	public static void sendEmail(String smtpServer, String emailUser, String emailPassword, String emailSender,
			String mailTo, String subject, String htmlContent) throws EmailNotSentException,
			InvalidEmailAddressException {
		List<String> to = Arrays.asList(new String[] { mailTo });
		Postman postman = new Postman(smtpServer, emailUser, emailPassword);
		try {
			if (!isValidEmailAddress(emailSender)) {
				throw new InvalidEmailAddressException("Address email '" + emailSender + "' is invalid");
			}
			postman.setSubject(subject);
			postman.addHtml(htmlContent);
			postman.sendMail(to, null, null, emailSender);
		} catch (MessagingException e) {
			throw new EmailNotSentException(e.getMessage());
		}
	}

	/**
	 * This method is not too strong, some invalid emails will pass this test. Only for very basic validation of email
	 * 
	 * @param email
	 * @return
	 */
	private static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
}
