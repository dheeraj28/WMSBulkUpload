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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.*;
public class MailFiles {
	public static void main(String[] args) {
		sendMail4();
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
	public static void sendMail(String EmailBodyText, String toList, String ccList, String subject,String filepath,Log myLog)
			throws MessagingException, UnsupportedEncodingException {
		
		Config configr= new Config();
		String UID=configr.getProperty("mMailuid");
		String MPWD=configr.getProperty("mMailpwd");
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", "tc2-smtp-relay-pri.twutil.net");
		props.put("mail.smtp.port", 587);
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
		{
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(UID,MPWD);
			}} );


		MimeMessage msg = new MimeMessage(session);
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.setSubject(subject, "UTF-8");

		msg.setContent(EmailBodyText, "text/html; charset=utf-8");

		//msg.setText(text, "UTF-8");
		//msg.addHeader("Content-Transfer-Encoding", "8bit");
		msg.setSentDate(new Date());

		msg.setFrom(new InternetAddress("dheeraj.joshi@thameswater.co.uk"));
		//msg.setReplyTo(InternetAddress.parse("dheeraj.joshi@thameswater.co.uk", false));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList));		
		msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccList));

		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(EmailBodyText);
		Multipart multipart = new MimeMultipart();
		
		multipart.addBodyPart(messageBodyPart);
		String fname="Bulk Status File.txt";
		messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filepath);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fname);
		multipart.addBodyPart(messageBodyPart);

		Transport.send(msg);
		System.out.println("sent");
	}
	public static void sendMail2(String EmailBodyText, String toList, String ccList, String subject,String filepath,Log myLog)
			throws MessagingException, UnsupportedEncodingException 
	{
		try {
			Config configr= new Config();
			String UID=configr.getProperty("mMailuid");
			String MPWD=configr.getProperty("mMailpwd");
			
			Properties props = System.getProperties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.host", "tc2-smtp-relay-pri.twutil.net");
			props.put("mail.smtp.port", 587);
			Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(UID,MPWD);
				}} );
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(UID));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toList));
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(ccList));
			message.setSubject(subject);
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(EmailBodyText);
			
			Multipart multipart = new MimeMultipart();
			
			multipart.addBodyPart(messageBodyPart);
			String fname="Bulk Uploaded File.xlsx";
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filepath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fname);
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			
			Transport.send(message);
		} catch (Exception e) {
			myLog.logger.info("ERROR: Error while sending mail" + e);
			e.printStackTrace();
		}

        System.out.println("Sent message successfully....");
	}
	public static void sendMail3(String EmailBodyText, String toList, String ccList, String subject,String filepath,Log myLog)
			throws MessagingException, UnsupportedEncodingException 
	{
		try {
			Config configr= new Config();
			String UID=configr.getProperty("mMailuid");
			String MPWD=configr.getProperty("mMailpwd");
			
			Properties props = System.getProperties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.host", "tc2-smtp-relay-pri.twutil.net");
			props.put("mail.smtp.port", 587);
			Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {					
					return new javax.mail.PasswordAuthentication(UID,MPWD);
				}} );
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("dheeraj.joshi@thameswater.co.uk"));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toList));
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(ccList));
			message.setSubject(subject);
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(EmailBodyText);
			
			Multipart multipart = new MimeMultipart();
			
			multipart.addBodyPart(messageBodyPart);
			String fname="Bulk Status.txt";
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filepath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fname);
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			
			Transport.send(message);
		} catch (Exception e) {
			myLog.logger.info("ERROR: Error while sending mail" + e);
			e.printStackTrace();
		}

        System.out.println("Sent message successfully....");
	}
	public static void sendMail4() {
		try {
			Config configr= new Config();
			String UID=configr.getProperty("mMailuid");
			String MPWD=configr.getProperty("mMailpwd");
			
			System.out.println("1");
			Properties props = System.getProperties();
			props.setProperty("mail.imaps.auth.plain.disable", "true");
			props.setProperty("mail.imaps.auth.ntlm.disable", "true");
			Session session = Session.getInstance(props, null);
			Store store = session.getStore("imaps");
			System.out.println(session.getClass());
			System.out.println("2");
			store.connect("outlook.office365.com", 993, "twutil\\dheeraj.joshi@thameswater.co.uk\\wmsbulksubmission",MPWD);
			System.out.println("connected");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
