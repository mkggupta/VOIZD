/**
 * 
 */
package com.voizd.framework.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.framework.exception.util.ErrorCodesEnum;

/**
 * @author Suresh
 * 
 */
public class SendEmail {

	static Logger logger = LoggerFactory.getLogger(SendEmail.class);

	public static void sendEmail(String toMailId, String fromMailId, String subject, String text, String smtphost, String smtpport, String senderUsername,
			String senderPassword) throws MailServiceFailedException {

		final String username = senderUsername;
		final String password = senderPassword;

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtphost);
		if (null != smtpport) {
			props.put("mail.smtp.port", smtpport);
		}

		try {
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromMailId));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailId));
			message.setSubject(subject);
			message.setText(text, "utf-8", "html");

			Transport.send(message);

		} catch (Exception e) {
			logger.error("Exception in sending mail toMailId : " + toMailId + " fromMailId " + fromMailId + " subject " + subject + " text " + text
					+ " smtphost " + smtphost + " senderUsername " + senderUsername + " senderPassword " + senderPassword, e);

			throw new MailServiceFailedException(ErrorCodesEnum.EMAIL_SENDING_FAILED_EXCEPTION);
		}
	}
}
