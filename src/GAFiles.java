import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import javax.mail.MessagingException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class GAFiles {
	static Config configr= new Config();
	static String Logs_dir=configr.getProperty("mlogs");
	static String MAILTEXT="";
	static int newFormsCreated=0;
	static String logFilePath="";

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf2= new SimpleDateFormat("ddMMMyyyyHHmmss");
		Calendar calendar = Calendar.getInstance();
		//GAFiles.logFilePath=String.valueOf(sdf2.format(calendar.getTime()));
		String logfilename="MyLog"+String.valueOf(sdf2.format(calendar.getTime()))+".txt";
		GAFiles.logFilePath=Logs_dir+"\\"+logfilename;
		Log myLog=new Log(logFilePath);
		myLog.logger.setLevel(Level.ALL);
		GAFiles.SFTPConnTest(myLog,logfilename);
	}
	public static void SFTPConnTest(Log myLog, String logfilename) throws JSchException {
		GAFiles GAobj=new GAFiles();
		JSch jsch = new JSch();
		Session session = null;
		int totalFormLimit=2000;
	

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");		

		try {	
			Config configr= new Config();
			String servername=configr.getProperty("mServer");
			String uname=configr.getProperty("mServeruser");
			String pass=configr.getProperty("mServerpwd");
			int ftpport=configr.getIntProperty("mportftp");

			session = jsch.getSession(uname, servername, ftpport);

			session.setConfig(config);
			session.setPassword(pass);

			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();

			session.setTimeout(20000);

			System.out.println("OK Successful");

			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.getHome();
			myLog.logger.info("Go Anywhere Connection Successful");

			sftpChannel.cd("/BulkUploadFiles//");

			ValidateFiles valid=new ValidateFiles();
			String validateResult1="";
			String ToFolder=configr.getProperty("mDownloadfolder");

			//for(int i=0; i<filelist.size();i++){

			/*
			 * First loop of 2 cycles : (loopCount - 2)
			 * cycle 1 to check names and avoid any date factor in file name while validation in case file is checked after 12:00
			 * cycle 2 will directly download the file and avoid any validation.
			 */
			for(int loopCount=0;loopCount<2;loopCount++) {
				Vector<ChannelSftp.LsEntry> filelist = sftpChannel.ls("/BulkUploadFiles//");
				for(ChannelSftp.LsEntry entry : filelist) {
					System.out.println(entry.getFilename());
					String CurrentFilename=entry.getFilename();
					if("Processed".equalsIgnoreCase(CurrentFilename) || "Rejected".equalsIgnoreCase(CurrentFilename) || "AuditLog".equalsIgnoreCase(CurrentFilename) || "Not Processed".equalsIgnoreCase(CurrentFilename)) {}
					else {
						/*
						 * 	1) Validate  file names in loop0.
						 */						

						if(loopCount==0) {	
							System.out.println("#"+loopCount+"#"+CurrentFilename);
							String nameresult= valid.validateName(CurrentFilename,myLog);							
							validateResult1=nameresult.substring(0,7);
							System.out.println("validateResult1:"+validateResult1);
						}

						/*
						 * Next Loop Details :
						 * 	1) Call BulkUpload Class to read the downloaded excel - pass the file name and form name as parameter.
						 * 	2) Validate file content (check if its name of form is same as mentioned in filename)
						 * 			a) if true : Move files to Processed Folder in GA
						 * 			b) else    : Move files to Rejected Folder in GA
						 */
						if(loopCount==1) {
							System.out.println("#"+loopCount+"#"+CurrentFilename);
							validateResult1="Success";
						}
						if("Success".equalsIgnoreCase(validateResult1)) {
							if(loopCount==1) {								
								sftpChannel.get(entry.getFilename(), new StringBuilder(ToFolder).append(File.separator).append(entry.getFilename()).toString());
								String DownloadFolder= ToFolder+"\\"+CurrentFilename;
								System.out.println("****"+DownloadFolder);
								String FormName=CurrentFilename.substring(0,CurrentFilename.indexOf("_"));
								BulkUpload BulkObj=new BulkUpload();

								String BulkResult=BulkObj.CreateBulk(DownloadFolder, FormName , CurrentFilename,myLog);						

								//MOVE FILES TO RESEPCTIVE FOLDER BASED ON RETURN MESSAGE FROM BULK FILE READ 

								String BulkResultSplit[]=BulkResult.split("[$]");
								System.out.println(BulkResult);
								System.out.println(BulkResultSplit.length);
								
								if("Success".equalsIgnoreCase(BulkResultSplit[0])){
									newFormsCreated=newFormsCreated+Integer.parseInt(BulkResultSplit[2]);
									if(newFormsCreated<=totalFormLimit) {
										GAFiles.MAILTEXT+="\r\nBulk File "+CurrentFilename+" Successfully Processed, Total Records in file :"+BulkResultSplit[2]+"\n";
										myLog.logger.info("\nBulk File "+CurrentFilename+" Successfully Processed, Total Records in file :"+BulkResultSplit[2]+"\n");
										GAobj.MoveFilesinGA(sftpChannel,"/BulkUploadFiles//"+CurrentFilename,"Processed",CurrentFilename,valid,myLog);
									}else {
										System.out.println("########"+newFormsCreated+"######"+BulkResultSplit[2]);
										myLog.logger.info("\nBulk File "+CurrentFilename+" cant be processed as number of bulk forms per day limit is exceeded\n");
										GAobj.MoveFilesinGA(sftpChannel,"/BulkUploadFiles//"+CurrentFilename,"Not Processed",CurrentFilename,valid,myLog);
									}
									
								}else {							
									myLog.logger.info("\nBulk File "+CurrentFilename+" Rejected,"+BulkResultSplit[1]+","+BulkResultSplit[2]);
									GAobj.MoveFilesinGA(sftpChannel,"/BulkUploadFiles//"+CurrentFilename,"Rejected",CurrentFilename,valid,myLog);}
							}

						}else {						
							GAobj.MoveFilesinGA(sftpChannel,"/BulkUploadFiles//"+CurrentFilename,"Rejected",CurrentFilename,valid,myLog);
						}
					}
				}
			}
			System.out.println(GAFiles.logFilePath);
			sftpChannel.put(GAFiles.logFilePath,"//BulkUploadFiles//AuditLog//"+logfilename);
			sftpChannel.exit();
			session.disconnect();
			GAobj.mailNotificaiton(myLog);
			System.out.println("OK Successful2");

		}
		catch (JSchException e) {	
			myLog.logger.severe("\n Unable to Connect to Go Anywhere to extract bulk files. Please contact Admin.\n");
			GAFiles.MAILTEXT+="\n Unable to Connect to Go Anywhere to extract bulk files. Please contact Admin.\n";
			GAobj.mailNotificaiton(myLog);
			e.printStackTrace();  
		}
		catch(Exception e) {	
			myLog.logger.severe("\n"+e.getMessage());
			GAFiles.MAILTEXT+="\n Error while batch execution. Please contact Admin.\n";
			GAobj.mailNotificaiton(myLog);
			e.printStackTrace();
		}
	}
	public void mailNotificaiton(Log myLog) {
		SimpleDateFormat sdf3= new SimpleDateFormat("ddMMMyyyyHHmmss");
		Calendar calendar = Calendar.getInstance();
		//String NotifBody="<html><body>Hi Team,<br><br>This is a notification regarding the Bulk upload reqest creation.<br><br>Please find the attached status for the requests created.<br><br>Regards,<br>WMS Cordys</body></html>";
		String NotifBody="Hi Team,\n" + 
				"\n" + 
				"This is a notification regarding the Bulk upload reqest creation.\n" + 
				"\n" + 
				"Please find the attached status for the requests created.\n" + 
				"\n" + 
				"Regards,\n" + 
				"WMS Cordys\n" + 
				"";
		GAFiles.MAILTEXT+="\r\n <EOF>";
		String notif_MailAttachmentContent=GAFiles.MAILTEXT;
		System.out.println(notif_MailAttachmentContent);
		
		try {			
			String notiffile=configr.getProperty("mnotifFile")+"NOTIFFILE"+String.valueOf(sdf3.format(calendar.getTime()))+".txt";
			File myObj = new File(notiffile);
		    System.out.println("File created: " + myObj.getName());
		    FileWriter myWriter = new FileWriter(notiffile);
		    myWriter.write(notif_MailAttachmentContent);
		    myWriter.close();
		    MailFiles.sendMail3(NotifBody, configr.getProperty("mnotifMail"), "", "Bulk Upload Notification Mail",notiffile,myLog);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}catch (MessagingException e) {
			e.printStackTrace();
		}	
	}
	public void MoveFilesinGA(ChannelSftp sftpChannel,String Source, String Destination, String CurrentFilename,ValidateFiles valid,Log myLog) {
		try {
			SftpATTRS attrs=null;
			try {
				attrs = sftpChannel.stat("/BulkUploadFiles/Processed/"+CurrentFilename);							
				if(attrs != null) {					
					sftpChannel.rename("/BulkUploadFiles//"+CurrentFilename,"/BulkUploadFiles//"+Destination+"//"+valid.getFileReName(CurrentFilename,1));
				}
			}catch(SftpException E){
				if(E.id == 2) {								
					sftpChannel.rename("/BulkUploadFiles//"+CurrentFilename,"/BulkUploadFiles//"+Destination+"//"+valid.getFileReName(CurrentFilename,0));
				}
				else {
					System.out.println(E.id+"\n"+E);
					myLog.logger.severe("\n"+E.id+"\n"+E);
					E.printStackTrace();
				}
			}
		} catch (SftpException e) {
			myLog.logger.info("\n"+e);
			e.printStackTrace();
		}
		System.out.println(CurrentFilename+" moved to "+Destination+" in Go Anywhere\n");
		GAFiles.MAILTEXT+="\r\n"+CurrentFilename+" moved to "+Destination+" in Go Anywhere\n";
		myLog.logger.info(CurrentFilename+" moved to "+Destination+" in Go Anywhere\n");		
	}

}
