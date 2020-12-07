import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FileFormStatus {
	public static void getBulkNewFormsReport(Connection conn,String Subject,String FormName,Log myLog) {
		//ArrayList<String> rlist=new ArrayList<String>();
		try {
			int i=1;
			String newforms="";
			QuerySt oquery=new QuerySt();
			Statement st1=conn.createStatement();
			ResultSet rs1=st1.executeQuery(oquery.getNewRegBulkForms(Subject,FormName));
			while(rs1.next()) {
				String error="";
				String testerror=rs1.getString("ERROR_TEXT");
				System.out.println(testerror);
				//if(rs1.getString("ERROR_TEXT"))
				if(rs1.getString("ERROR_TEXT")!=null && rs1.getString("ERROR_TEXT")!="" && (!"null".equalsIgnoreCase(rs1.getString("ERROR_TEXT")))) {
					String ET=testerror;
					String err0[]=ET.split("<ROW>");					
					for(int j=0;j<err0.length;j++) {
						if(!err0[j].contains("<ROOT>")&&(err0[j].contains("<FIELD>")) ) {
							error+=err0[j].substring(err0[j].indexOf("<FIELD>")+7,err0[j].indexOf("</FIELD>"))+"-";
							error+=err0[j].substring(err0[j].indexOf("<ERROR_MESSAGE>")+15,err0[j].indexOf("</ERROR_MESSAGE>"))+"||";
						}	
					}				
					newforms+="\r\n"+(i++)+","+"REQUEST_NO-"+rs1.getString("REQUEST_NO")+",REQUEST_STATUS-"+rs1.getString("REQUEST_STATUS")+",ERROR REASON-"+error+".";			
				}else {
					newforms+="\r\n"+(i++)+","+"REQUEST_NO-"+rs1.getString("REQUEST_NO")+",REQUEST_STATUS-"+rs1.getString("REQUEST_STATUS");
				}
			}
			System.out.println(newforms);	
			myLog.logger.info(newforms);
			GAFiles.MAILTEXT+=newforms;
		} catch (SQLException e) {			
			myLog.logger.severe("message - "+e.getMessage());
			e.printStackTrace();
		}
	}
}
