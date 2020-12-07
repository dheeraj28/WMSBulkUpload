import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.Replace;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BulkUpload {
	public String CreateBulk(String bulkfilename,String FormName, String CurrentFilename, Log myLog) throws SQLException {
		String result="";
		String FormTypeValid="";
		Workbook wb_xssf = null;
		Workbook wb_xssf0 = null;
		Workbook wb_xssf2 = null;
		Sheet sheet0 = null;
		Sheet sheet = null;
		Sheet sheet2 = null;
		Row row = null;
		Row row0 = null;
		Row rownew = null;	
		String bulkFile=bulkfilename;
		SimpleDateFormat sdf= new SimpleDateFormat("MMM");
		SimpleDateFormat sdf2= new SimpleDateFormat("ddMMMyyyyHHmmss");
		Calendar calendar = Calendar.getInstance();
		String fname=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" "+String.valueOf(sdf.format(calendar.getTime()));
		String currdt2=String.valueOf(sdf2.format(calendar.getTime()));

		String SubjectMail="Bulk Upload request : "+CurrentFilename+"-"+currdt2;
		//String SubjectMail="Gap_Bulk_Upload_c3K_13Sep";

		try {

			//String filePath=null;
			String filePath2=null;
			String bulkFilePath=null;
			System.out.println(fname);
			String fnme="";
			String fnmeArc="";

			int initrow=0;
			int initrowNew=0;
			int lastrowno0=0;

			Config configr= new Config();
			String EmailBodyText=null;
			String toList=configr.getProperty("mToMail");
			String ccList="";

			EmailBodyText="uploadbulktests"+CurrentFilename;
			int bulkHeaderComparisons=0;
			int HeaderComparisons=0;

			/*
			 * 1) Based on the form type in parameter passed - compare the form validation
			 * 2) Return validation result as true or false. (based on it the file will be moved in GA folders to processed ot rejected)
			 * 3) In case of true continue the bulk form process 
			 * 4) In case of false break the bulk form process.
			 * 5) Capture the details in Log file.
			 * 
			 */

			if("H03".equalsIgnoreCase(FormName)) {
				filePath2=configr.getProperty("mStandrdPathH03");
				//filePath2 ="C:\\Bulk Upload\\H03 Std form.xlsx";
				//bulkFilePath="C:\\Bulk Upload\\Form H03 Bulk template.xlsx"; //location where the bulk file is downloaded

				initrow=2;		//It is the value from which shows the actual rows index after header in bulk File.
				initrowNew=3;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=65; // It is number of rows in standard form.

				FormTypeValid="Form H03";				
				fnme+=configr.getProperty("mnewForms")+"\\NewH03form_";		
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\NewH03form_";
				bulkHeaderComparisons=44;	//number of columns in header
			}
			if("wsled".equalsIgnoreCase(FormName)||"ws-led".equalsIgnoreCase(FormName)){
				filePath2=configr.getProperty("mStandrdPathRegWS");
				//filePath2 ="C:\\Bulk Upload\\Wsled Std form.xlsx";
				initrow=2;		//It is the value that shows the actual rows index after which header in bulk File.
				initrowNew=2;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=18; // It is number of rows in standard form.

				FormTypeValid="Form WSled";

				fnme+=configr.getProperty("mnewForms")+"\\New-WSLEDform_";
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\New-WSLEDform_";
				bulkHeaderComparisons=16;	//number of columns in header - Used to validate bulk form header submitted.
			}
			if("C04".equalsIgnoreCase(FormName)) {
				filePath2=configr.getProperty("mStandrdPathC04");
				//filePath2 ="C:\\Bulk Upload\\C04 Std form.xlsx";
				//bulkFilePath="C:\\Bulk Upload\\Form C04 Bulk template.xlsx"; //location where the bulk file is downloaded

				initrow=2;		//It is the value from which shows the actual rows index after header in bulk File.
				initrowNew=3;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=63; // It is number of rows in standard form.

				FormTypeValid="Form C04";				
				fnme+=configr.getProperty("mnewForms")+"\\NewC04form_";		
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\NewC04form_";
				bulkHeaderComparisons=43;	//number of columns in header
			}
			if("C01".equalsIgnoreCase(FormName)) {
				filePath2=configr.getProperty("mStandrdPathC01");
				//filePath2 ="C:\\Bulk Upload\\C04 Std form.xlsx";
				//bulkFilePath="C:\\Bulk Upload\\Form C04 Bulk template.xlsx"; //location where the bulk file is downloaded

				initrow=2;		//It is the value from which shows the actual rows index after header in bulk File.
				initrowNew=3;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=90; // It is number of rows in standard form.

				FormTypeValid="Form C01";				
				fnme+=configr.getProperty("mnewForms")+"\\NewC01form_";		
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\NewC01form_";
				bulkHeaderComparisons=50;	//number of columns in header
			}
			if("B01".equalsIgnoreCase(FormName)) {
				filePath2=configr.getProperty("mStandrdPathB01");
				//filePath2 ="C:\\Bulk Upload\\C04 Std form.xlsx";
				//bulkFilePath="C:\\Bulk Upload\\Form C04 Bulk template.xlsx"; //location where the bulk file is downloaded

				initrow=2;		//It is the value from which shows the actual rows index after header in bulk File.
				initrowNew=3;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=97; // It is number of rows in standard form.

				FormTypeValid="Form B01";				
				fnme+=configr.getProperty("mnewForms")+"\\NewB01form_";		
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\NewB01form_";
				bulkHeaderComparisons=70;	//number of columns in header
			}
			if("C03".equalsIgnoreCase(FormName)) {
				filePath2=configr.getProperty("mStandrdPathC03");
				//filePath2 ="C:\\Bulk Upload\\C04 Std form.xlsx";
				//bulkFilePath="C:\\Bulk Upload\\Form C04 Bulk template.xlsx"; //location where the bulk file is downloaded

				initrow=2;		//It is the value from which shows the actual rows index after header in bulk File.
				initrowNew=3;	//It is the value from where the first value will be tested/compared in a standard form
				lastrowno0=306; // It is number of rows in standard form.

				FormTypeValid="Form C03";				
				fnme+=configr.getProperty("mnewForms")+"\\NewC03form_";		
				fnmeArc+=configr.getProperty("mnewForms")+"\\Archive\\NewC03form_";
				bulkHeaderComparisons=80;	//number of columns in header
			}
			bulkFilePath=bulkfilename;

			MailFiles mailobj=new MailFiles();

			InputStream inp0 = new FileInputStream(new File(filePath2));
			wb_xssf0 = new XSSFWorkbook(inp0);
			sheet0 = wb_xssf0.getSheetAt(0);
			row0 = sheet0.getRow(0);

			InputStream inp = new FileInputStream(new File(bulkFilePath));
			wb_xssf = new XSSFWorkbook(inp);
			sheet = wb_xssf.getSheetAt(0);
			row = sheet.getRow(2);

			int lastrowno=sheet.getLastRowNum();
			int lastcolno=row.getLastCellNum();



			String keyValue = row.getCell(0).getRichStringCellValue().getString();
			String excel_ini=sheet.getRow(0).getCell(1).getRichStringCellValue().getString();

			for(int n=0;n<lastcolno;n++){										//Loop till last column Number in bulk file
				keyValue = row.getCell(n).getRichStringCellValue().getString();	//Value of Bulk File Header Field
				for(int i=initrowNew;i<lastrowno0;i++) {						//Loop through standard form Row Header
					if(keyValue.equalsIgnoreCase(sheet0.getRow(i).getCell(0).getRichStringCellValue().getString())) {	//match bulk Header with the Standard form. 										
						HeaderComparisons++;
						//System.out.println(sheet0.getRow(i).getCell(0).getRichStringCellValue().getString());
						break;
					}					
				}
			}

			System.out.println(bulkHeaderComparisons+"_COMPARISONS_"+HeaderComparisons);


			//GET ACTUAL LAST ROW - DISCARD BLANK ROWS FOR EXCEL START 
			int actualBulkSheetlastRow=0;
			int lastRowCounter=0;
			int blankrowcount=0;
			String blankrow="";
			System.out.print("DHEERAJ"+lastrowno);
			for(int n=initrowNew;n<lastrowno;n++) {
				System.out.println(n);

				if(lastRowCounter==10){
					actualBulkSheetlastRow=n-10;
					break;
				}

				if(BulkUpload.isRowEmpty(sheet.getRow(n))){
					System.out.println("DHEERAJ"+n);
					lastRowCounter++;
					blankrow+=n+",";
					blankrowcount++;

					//blankrowcount++;
				}
				else if(!BulkUpload.isRowEmpty(sheet.getRow(n)) && (lastRowCounter>0)){
					lastRowCounter=0;
				}


				System.out.println();
			}

			//String blankRowsString[] = blankrow.split(",");
			//int blankRowsInt[]=new int[blankRowsString.length];
			int[] blankRowsInt = Arrays.stream(blankrow.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
			//CONVERTING BLANK ROWS STRING TO INTEGER ARRAY
			/*
		System.out.println(blankRowsString.length+"@@@@"+blankRowsInt.length);
		for(int k=0;k<blankRowsString.length;k++) {
			blankRowsInt[k]=Integer.parseInt(blankRowsString[k]);
			System.out.println(blankRowsInt[k]+"####");
		}*/

			if(actualBulkSheetlastRow==0) {actualBulkSheetlastRow=lastrowno;}
			else {
				lastrowno=actualBulkSheetlastRow;
			}
			System.out.println("DHEERAJ-AAAAAAAAAAAAAAAAAAAAAAAAAAA- "+lastrowno+"$BRC$"+blankrowcount);
			//GET LAST ROW FOR EXCEL END

			//BULK FILE VALIDATION
			int actuallastRow=lastrowno-initrow;
			
			int actuallastRow2=lastrowno-initrow-blankrowcount;
			if(blankrowcount>=10) {actuallastRow2=lastrowno-initrow-blankrowcount+9;}
			else if(actuallastRow2==-9) {actuallastRow2=0;}
			else {actuallastRow2=lastrowno-initrow-blankrowcount;}
			int currentRecordCount = GAFiles.newFormsCreated;
			currentRecordCount=currentRecordCount+actuallastRow2;
			System.out.println("&&&&&&&&&&&&&&&TEST-COUNT&&&&&&&&&&&&&&&"+currentRecordCount);
			if (!FormTypeValid.equalsIgnoreCase(sheet.getRow(0).getCell(0).getRichStringCellValue().getString())) {
				result+="Fail$Invalid Bulk Form Template$Form Name : "+FormName;
				GAFiles.MAILTEXT+="Invalid Bulk Form Template,Form Name : "+FormName;
				return result;
			}
			else if(HeaderComparisons!=bulkHeaderComparisons) {
				result+="Fail$Invalid "+FormName+" Standard Bulk Form Template : Header Row Mismatch in file "+CurrentFilename+"$Rows Mismatched :  "+(bulkHeaderComparisons-HeaderComparisons);
				GAFiles.MAILTEXT+="\r\n Invalid "+FormName+" Standard Bulk Form Template : Header Row Mismatch in file "+CurrentFilename+"$Rows Mismatched :  "+(bulkHeaderComparisons-HeaderComparisons);
				return result;
			}
			else if(actuallastRow2==0){
				result+="Fail$ Empty Rows- $"+actuallastRow2;
				GAFiles.MAILTEXT+="\r\n Empty Bulk File-"+CurrentFilename+", Rows : "+actuallastRow2;
				//GAFiles.MAILTEXT+="\r\nForms Generated till now - "+GAFiles.newFormsCreated;
				return result;				
			}
			else if(actuallastRow>2000 || currentRecordCount>2000) {
				result+="Success$Bulk Forms/day Exceeded - $"+actuallastRow2;
				GAFiles.MAILTEXT+="\r\nBulk Forms/day Exceeded - in Bulk File-"+CurrentFilename+", with no. of rows :"+actuallastRow2;
				GAFiles.MAILTEXT+="\r\nForms Generated till now - "+GAFiles.newFormsCreated;
				return result;				
			}
			else
			{
				result+="Success$New Excel forms Created$"+actuallastRow2;
			}

			System.out.println(keyValue +" ~~~ "+ excel_ini+"---"+lastrowno+"---"+lastcolno);

			for(int formno=1;formno<=actuallastRow;formno++) {
				int formNoNew=formno+initrow;
				int blankflag=0;
				fnme+=formno+"_"+fname+".xlsx";
				fnmeArc+=formno+"_"+fname+String.valueOf(sdf2.format(calendar.getTime()))+".xlsx";
				for(int blnktest : blankRowsInt) {
					if (blnktest==formNoNew) {
						System.out.println(blnktest+"@@@@"+blnktest);
						blankflag=1;
						fnme=fnme.substring(0, fnme.indexOf("_")+1);
						fnmeArc=fnmeArc.substring(0, fnmeArc.indexOf("_")+1);
					}
				}
				
				if(blankflag==0) {
					System.out.println("~~~~~~"+fnme);
					FileUtils.copyFile(new File(filePath2), new File(fnme));
				}
				if(blankflag==0) {
					System.out.println("~~~~~~"+fnme);
					FileUtils.copyFile(new File(filePath2), new File(fnme));
					int newformRowCounter=0;		//DUPLICATES EXCEPTION HANDLE
					InputStream inp2 = new FileInputStream(new File(fnme));
					wb_xssf2 = new XSSFWorkbook(inp2);
					sheet2 = wb_xssf2.getSheetAt(0);
					rownew = sheet2.getRow(3);
					for(int n=0;n<lastcolno;n++){
						keyValue = row.getCell(n).getRichStringCellValue().getString();

						System.out.println(n+"---"+keyValue+"---");	
						for(int i=initrowNew;i<lastrowno0;i++) {							
							if(keyValue.equals(sheet0.getRow(i).getCell(0).getRichStringCellValue().getString())) {
								if ((i>=newformRowCounter)) {
									if(sheet.getRow(formNoNew).getCell(n)!=null && !"".equals(sheet.getRow(1).getCell(n))) {
										if(sheet.getRow(formNoNew).getCell(n).getCellType()==XSSFCell.CELL_TYPE_NUMERIC) {
											System.out.println("%%%"+formno+"@@@"+keyValue+"***"+sheet.getRow(formNoNew).getCell(n).getNumericCellValue());
											//System.out.println(i+"$$$"+sheet2.getRow(i).getCell(1).getRichStringCellValue());
											//Cell cell=sheet2.getRow(i).getCell(1);
											sheet2.getRow(i).getCell((short) 1).setCellValue(sheet.getRow(formNoNew).getCell(n).getNumericCellValue());
										}
										else {
											//System.out.println(i+"$$$"+sheet2.getRow(i).getCell((short) 1).getRichStringCellValue());
											//Cell cell=sheet2.getRow(i).getCell(1);
											//cell.setCellValue("aa");
											sheet2.getRow(i).getCell((short) 1).setCellValue(sheet.getRow(formNoNew).getCell(n).getRichStringCellValue());
										}
									}else {
										//System.out.println("%%%"+formno+"@@@"+keyValue+"***"+"NA");
										System.out.println(i+"$$$"+sheet2.getRow(i).getCell((short) 1).getRichStringCellValue());
										//Cell cell=sheet2.getRow(i).getCell(1);
										//cell.setCellValue("NA");								
										sheet2.getRow(i).getCell((short) 1).setCellValue("");
									}

									System.out.println("THIS IS newformRowCounter - "+newformRowCounter);									
									
									if(FormName=="C03"){
										if(newformRowCounter==79){i=226;}
										else if (newformRowCounter==229){i=260;}
									}
									newformRowCounter=i;
									break;
								}
							}
							
							//newformRowCounter=i;
						}

					}


					FileOutputStream outFile =new FileOutputStream(new File(fnme));
					wb_xssf2.write(outFile);
					outFile.close();
					myLog.logger.info("Attachment file created - " + fnme);				
					MailFiles.sendMail2(EmailBodyText, toList, ccList, SubjectMail,fnme,myLog);
					movefiles(fnme, fnmeArc);	//move new created forms to arcive after sending mail
					fnme=fnme.substring(0, fnme.indexOf("_")+1);
					fnmeArc=fnmeArc.substring(0, fnmeArc.indexOf("_")+1); // Archive files name ISSUES FIX
					System.out.println("here"+SubjectMail);
					System.out.println("wait for 10 seconds"); 
					Thread.sleep(10000);
					System.out.println("Wait over");
					if(formno%10==0) {
						Thread.sleep(10000);
					}
				}
			}
		}catch (Exception e) {
			myLog.logger.severe("Error while generating/validating Bulk excel" + e);
			e.printStackTrace();
		}
		
		  try { 
			  Thread.sleep(120000);
			  //Thread.sleep(10);
			  } catch (InterruptedException e) {
		  myLog.logger.severe("Error while Thread Sleep" + e); e.printStackTrace(); }
		 
		System.out.println("b4 DBCONN");
		DBConn dbc=new DBConn();
		Connection oconn=dbc.getConncetionObj();
		FileFormStatus.getBulkNewFormsReport(oconn,SubjectMail,FormName,myLog);
		oconn.close();


		return result;
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
		System.out.println("p2"+p2);
		System.out.println(result);
		return result; 
	}
	public static boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getStringCellValue() !="" ) {	        	
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) throws SQLException, SecurityException, IOException {
		Config configr= new Config();
		String Logs_dir=configr.getProperty("mlogs");
		BulkUpload bulkobj=new BulkUpload();
		SimpleDateFormat sdf2= new SimpleDateFormat("ddMMMyyyyHHmmss");
		Calendar calendar = Calendar.getInstance();
		//GAFiles.logFilePath=String.valueOf(sdf2.format(calendar.getTime()));
		String logfilename="MyLog"+String.valueOf(sdf2.format(calendar.getTime()))+".txt";
		String logFilePath=Logs_dir+"\\"+logfilename;
		Log myLog=new Log(logFilePath);
		String t=bulkobj.CreateBulk("F:\\Bulk Upload Batch\\WMSBulkUpload\\New Bulk Files\\B01_WAVE-R_24202020.xlsx","B01","B01_WAVE-R_24202020",myLog);
		System.out.println(t);
		//CeateBulk(String bulkfilename,String FormName, String CurrentFilename, Log myLog)

	}


}