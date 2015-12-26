/**
 * 
 */
package com.voizd.util.email;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.util.VoizdUrlUtils;
import com.voizd.framework.mail.MailServiceFailedException;
import com.voizd.framework.mail.SendEmail;
import com.voizd.util.VoizdProperties;
import com.voizd.util.VoizdPropertyKeys;

/**
 * @author Manish
 * 
 */
public class EmailUtil {

	static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	private static String getEmailVerificationText(String firstName, String lastName, long id, String emailAddress, String verificationCode) {
		String verificationUrl = VoizdUrlUtils.getServerUrl() + "rest/api/authentication/verifyEmail/" + id + "/" + emailAddress + "/" + verificationCode;
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine ve = new VelocityEngine();
		try {
			ve.init(props);
			Template t = ve.getTemplate("templates/verificationEmail.vm");
			VelocityContext context = new VelocityContext();
			context.put("fname", firstName);
			if(lastName != null && lastName.length()>0){
			context.put("lname", lastName);
			}else{
				context.put("lname", "");
			}
			context.put("email", emailAddress);
			context.put("verificationUrl", verificationUrl);
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			return writer.toString();
		} catch (Exception e) {
			logger.error("getEmailVerificationText : Exception in fetching mail template " + e.getMessage(), e);
			if(lastName == null){
				lastName="";
			}
			return "Dear "
					+ firstName
					+ " "
					+ lastName
					+ ",\n You've entered "
					+ emailAddress
					+ " as the contact email address for your VOIZD userID. To complete the process, we just need to verify that this email address belongs to you. Simply click the link below to verify your registration.\n "
					+ verificationUrl
					+ " \n\n Wondering why you got this email? \n \n It's sent when someone registers with VOIZD. \n \n If you didn't do this, don't worry. Your email address cannot be used as a contact address for anywhere without your verification.\n\n Thanks,\n\n VOIZD Customer Support";

		}

	}

	private static String getResetPasswordEmailText(String firstName, String lastName, long forgetPasswordId, long userId, String emailAddress,
			String verificationCode) {

		String resetPasswordUrl = VoizdUrlUtils.getServerUrl() + "pages/changePassword.jsp?forgetPasswordId=" + forgetPasswordId + "&id=" + userId
				+ "&usrName=" + emailAddress + "&verificationCode=" + verificationCode;
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine ve = new VelocityEngine();
		try {
			ve.init(props);
			Template t = ve.getTemplate("templates/forgetPasswordEmail.vm");
			VelocityContext context = new VelocityContext();
			context.put("fname", firstName);
			if(lastName != null && lastName.length()>0){
			context.put("lname", lastName);
			}else{
				context.put("lname", "");
			}
			context.put("email", emailAddress);
			context.put("resetPasswordUrl", resetPasswordUrl);
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			return writer.toString();

		} catch (Exception e) {
			logger.error("getResetPasswordEmailText : Exception in fetching mail template " + e.getMessage(), e);
			return "\n Forgot your password, " + firstName + " " + lastName
					+ ",\n We received a request to reset the password for your VOIZD account  " + emailAddress
					+ "\n To reset your password, click on the link below (or copy and paste the URL into your browser): \n " + resetPasswordUrl
					+ " \n\n Thanks, \n \n VOIZD Customer Support";

		}

	}

	public static void sendEmailVerificationEmail(String firstName, String lastName, long id, String emailAddress, String verificationCode)
			throws MailServiceFailedException {

		String emailText = getEmailVerificationText(firstName, lastName, id, emailAddress, verificationCode);
		VoizdProperties voizdProperties = VoizdProperties.getInstance();
		SendEmail.sendEmail(emailAddress, voizdProperties.getProperty(VoizdPropertyKeys.EMAIL_SENDER),
				voizdProperties.getProperty(VoizdPropertyKeys.EMAIL_VERIFICATION_SUBJECT), emailText,
				voizdProperties.getProperty(VoizdPropertyKeys.SMTP_SERVER), null, voizdProperties.getProperty(VoizdPropertyKeys.SMTP_USER),
				voizdProperties.getProperty(VoizdPropertyKeys.SMTP_PASSWORD));
	}

	public static void sendResetPasswordEmail(String firstName, String lastName, long forgetPasswordId, long userId, String emailAddress,
			String verificationCode) throws MailServiceFailedException {

		String emailText = getResetPasswordEmailText(firstName, lastName, forgetPasswordId, userId, emailAddress, verificationCode);

		VoizdProperties voizdProperties = VoizdProperties.getInstance();

		SendEmail.sendEmail(emailAddress, voizdProperties.getProperty(VoizdPropertyKeys.EMAIL_SENDER),
				voizdProperties.getProperty(VoizdPropertyKeys.EMAIL_CHANGE_PASSWORD_SUBJECT), emailText,
				voizdProperties.getProperty(VoizdPropertyKeys.SMTP_SERVER), null, voizdProperties.getProperty(VoizdPropertyKeys.SMTP_USER),
				voizdProperties.getProperty(VoizdPropertyKeys.SMTP_PASSWORD));
	}
}
