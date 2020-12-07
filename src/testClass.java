import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

public class TestClass {
	public static void main5(String[] args) {
		//File f1=new File("F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\H03_Castle-r_25092020.xlsx");
		//File f2=new File("F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\Archive\\H03_Castle-r_25092020.xlsx");
		String p1="F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\WSLED_Castle-r_25092020.xlsx";
		String p2="F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\Archive\\WSLED_Castle-r_25092020.xlsx";
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTime());
	}
	public static String movefiles(String p1,String p2) {
		String result="Fail";
		try {
			FileUtils.copyFile(new File(p1), new File(p2));
			File f1=new File(p1);
			f1.delete();
			result="Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result; 
	}
	public static void main6(String[] args) {
		Config configr= new Config();
		String notiffile=configr.getProperty("mnotifFiles");
		System.out.println(configr.getProperty("mnotifFiles")); 
		System.out.println(configr.getProperty("mnewForms"));
	}
	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
		String EmailBodyText="TESTING MAILS";
		String toList="Subodh.Aggarwal@thameswater.co.uk";
		//String ccList="wmsbulksubmission@thameswater.co.uk";
		String ccList="";
		String subject="TESTING MAIL";
		sendMail(EmailBodyText,toList,ccList,subject);

	}
	public static void sendMail(String EmailBodyText, String toList, String ccList, String subject)
			throws MessagingException, UnsupportedEncodingException {

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

			msg.setFrom(new InternetAddress("wmsbulksubmission@thameswater.co.uk", "BULK UPLOAD -plz dont reply"));
			msg.setReplyTo(InternetAddress.parse("wmsbulksubmission@thameswater.co.uk", false));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList, false));		
			//msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccList, false));

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(EmailBodyText, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.attachFile("F:\\Test Image.PNG");
			multipart.addBodyPart(attachPart);

			msg.setContent(multipart);

			/*
			 * Multipart multipart = new MimeMultipart();
			 * 
			 * multipart.addBodyPart(messageBodyPart); String fname="Bulk Status.txt";
			 * messageBodyPart = new MimeBodyPart(); DataSource source = new
			 * FileDataSource(filepath); messageBodyPart.setDataHandler(new
			 * DataHandler(source)); messageBodyPart.setFileName(fname);
			 * multipart.addBodyPart(messageBodyPart);
			 */

			Transport.send(msg);
		} catch (AddressException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
