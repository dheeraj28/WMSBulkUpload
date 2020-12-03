import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ValidateFiles {

	String Formname="";
	
	public static void main(String[] args) throws SecurityException, IOException {
		Config configr= new Config();
		String Logs_dir=configr.getProperty("mlogs");
		Log myLog=new Log(Logs_dir+"\\MyLog.txt");
		ValidateFiles obj=new ValidateFiles();
		String result=obj.validateName("H03_YUWATER-R_14092020.xlsx",myLog);
		System.out.println(result);
	}
	public String validateName(String fname, Log myLog) {
		String result="";
		int flag=0;
		String fnametest=fname;
		String Fnames[]=fnametest.split("_");
		SimpleDateFormat sdf1= new SimpleDateFormat("ddMMYYYY");
		Calendar calendar = Calendar.getInstance();
		String currdt1=String.valueOf(sdf1.format(calendar.getTime()));
		

		if(Fnames.length != 3) {
			flag=1;			
			result+=fname+" : File name is invalid.\nAccepted File format : \"form-name_RETAILER-ID_DDMMYYYY.xlsx\" , Ex: C01_CASTLE-R_04092020.xlsx\n";
			//myLog.logger.info(result);
		}else {	
			//System.out.println(Fnames.length);
			for(int i=0;i<Fnames.length;i++) {
				//System.out.println(Fnames[i]);
				if(i==0) {
					if("H03".equalsIgnoreCase(Fnames[i]) || "wsled".equalsIgnoreCase(Fnames[i]) || "ws-led".equalsIgnoreCase(Fnames[i]) ) {
						Formname=Fnames[i];
					}
					else {
						flag=1;
						result+=fname+" : Bulk File name is invalid.\nInvalid Form Type in file name.\n";
						//myLog.logger.info(result);
					}
				}
				else if(i==1) {
					if(!"Success".equalsIgnoreCase(retailerCheck(Fnames[i],myLog))) {					
						flag=1;
						result+=fname+" : Bulk File name is invalid.\nInvalid Retailer Type \""+Fnames[i]+"\" in file name.\n";
						//myLog.logger.info(result);
						}
				}
				else if(i==2) {
					String FileDtFormat[]=Fnames[2].split("[.]");
					//System.out.println(FileDtFormat.length);
					//System.out.println(currdt1+"@@@"+FileDtFormat[0]);
					if(FileDtFormat.length!=2) {
						flag=1;
						result+=fname+" :File name is invalid.\nInvalid Date Format \""+Fnames[i]+"\" in file name.\n Correct format : \"form-name_RETAILER-ID_DDMMYYYY.xlsx\" ";
					}
					if(!currdt1.equals(FileDtFormat[0])) {
							System.out.println("testing");
							flag=1;
							result+=fname+" - File name is invalid. Incorrect Current Date/Date Format : File date format is invalid. \nCorrect format : \"form-name_RETAILER-ID_DDMMYYYY.xlsx\" ";
							//myLog.logger.info(result);
					}
					if(!"xlsx".equalsIgnoreCase(FileDtFormat[1])) {
						flag=1;
						result+=fname+" : File format is invalid.";
						//myLog.logger.info(result);
					}

				}		
			}
		}
		
		if(flag==0) {
			result="Success-"+Formname;
			GAFiles.MAILTEXT+="\r\nName of file \""+fname+"\"is in valid format\r\n";
		}else {
			GAFiles.MAILTEXT+="\r\n"+result;
		}
		//System.out.println(result);		
		myLog.logger.info(result);
		return result;
	}
	public String retailerCheck(String retailer,Log myLog) {
		
/*
		 1) get retailer list from DB 
		 	a) Create DB Conn class.
		 	b) import jars
		 	c) write queries in java
		 2) save them in array/list etc.
		 3) compare if the parameter matches any of the retailer in list/array
		 4) if parameter value matches then return : Success else return :FAil		 

*/		 
		DBConn dbc=new DBConn();
		Connection oconn=dbc.getConncetionObj();
		Statement stmt=null;
		String result="";
		int retailerMatchFlag=0;
		try {
			ValidateFiles retobj=new ValidateFiles();
			stmt=oconn.createStatement();
			ArrayList<String> retailers=retobj.getretailerlist(oconn,myLog);
			for (int j = 0; j<retailers.size() ; j++) {
				if(retailer.equalsIgnoreCase(retailers.get(j))) {					
					retailerMatchFlag=1;
					break;
				}
			}			
			stmt.close();
			oconn.close();
		} catch (SQLException e) {
			myLog.logger.severe(e.getMessage());
			e.printStackTrace();
		}

		if(retailerMatchFlag==1) {result+="Success";}
		else result="Fail";

		return result;

	}
	public ArrayList<String> getretailerlist(Connection conn,Log myLog) {
		ArrayList<String> rlist=new ArrayList<String>();
		try {
			QuerySt oquery=new QuerySt();
			Statement st1=conn.createStatement();
			ResultSet rs1=st1.executeQuery(oquery.getRetailers());
			while(rs1.next()) {
				rlist.add(rs1.getString("RetailerID"));		
				//System.out.println(rs1.getString("RetailerID"));
			}			
		} catch (SQLException e) {
			myLog.logger.severe("message - "+e.getMessage());
			e.printStackTrace();
		}
		return rlist;
	}
	public String getFileReName(String fname,int copyflag) {
		String Result="";
		SimpleDateFormat sdf1= new SimpleDateFormat("ddMMMYYYY-HHmmss");
		Calendar calendar = Calendar.getInstance();
		String currdt1=String.valueOf(sdf1.format(calendar.getTime()));
		
		String fileRename[]=fname.split("[.]");
		if(copyflag==1) {
		Result+=fileRename[0]+"_copy-"+currdt1+"."+fileRename[1];	
		}
		else {
			Result+=fileRename[0]+"-"+currdt1+"."+fileRename[1];
		}
		System.out.println(Result);
		return Result;
	} 
}
