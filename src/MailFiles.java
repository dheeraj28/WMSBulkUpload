import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.*;
public class MailFiles {
	public static void main(String[] args) {
	
		//sendMail3("test", "dheeraj.joshi@thameswater.co.uk", ", subject, filepath, myLog);
		/*		try {
			Config configr= new Config();
			//MailFiles.sendMail("test1", "dheeraj.joshi@thameswater.co.uk", "", "test");
			GAFiles.MAILTEXT="";
			MailFiles.sendMail(GAFiles.MAILTEXT, configr.getProperty("mnotifMail"), "", "Notification Mail","",);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public static void sendMail2(String EmailBodyText, String toList, String ccList, String subject,String filepath,Log myLog) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "tc2-smtp-relay-pri.twutil.net");
			Session session = Session.getInstance(props, null);

			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("format", "flowed");
			msg.setSubject(subject, "UTF-8");

			//msg.setContent(EmailBodyText, "text/html; charset=utf-8");

			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setSentDate(new Date());

			msg.setFrom(new InternetAddress("wmsbulksubmission@thameswater.co.uk", "BULK UPLOAD -PLZ DONT REPLY"));
			msg.setReplyTo(InternetAddress.parse("wmsbulksubmission@thameswater.co.uk", false));
			//msg.setFrom(new InternetAddress("Abhishek.Singh2@thameswater.co.uk", "BULK UPLOAD -PLZ DONT REPLY"));
			//msg.setReplyTo(InternetAddress.parse("Abhishek.Singh2@thameswater.co.uk", false));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList, false));		
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccList, false));

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(EmailBodyText, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.attachFile(filepath);
			multipart.addBodyPart(attachPart);

			msg.setContent(multipart);
			Transport.send(msg);
		}catch (AddressException e) {
			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}catch(Exception e){
			myLog.logger.info("ERROR: Error while sending mail" + e);
			e.printStackTrace();
		}
	}
}

