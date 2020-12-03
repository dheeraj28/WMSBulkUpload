import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
	public Connection getConncetionObj () {
		Connection conn=null;
		try {
			Config configr= new Config();
			
			//Commented Below is the line of code for production.
			
/*			String urlpath=configr.getProperty("mconnurl");	
			String Dbuser=configr.getProperty("mdbuser");	
			String Dbpsw=configr.getProperty("mdbpwd");	
			//System.out.println(urlpath+"\n"+Dbuser+"\n"+Dbpsw);
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");*/
			
			//Below lines of code are for UAT
			
			Class.forName("net.sourceforge.jtds.jdbc.Driver");			
			String urlpath="jdbc:jtds:sqlserver://CORDBWT02:3322/WMST_AppDB;useCursors=true";
			String Dbuser=configr.getProperty("mdbuser");	
			String Dbpsw=configr.getProperty("mdbpwd");	
			
			conn=DriverManager.getConnection(urlpath,Dbuser,Dbpsw);
			
			System.out.println("Success2");
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void main(String[] args) throws SecurityException, IOException {
		try {
			DBConn obj=new DBConn();
			Connection test=obj.getConncetionObj();
			//FileFormStatus.getBulkNewFormsReport(test, "bulk upload request : h03_castle-r_16092020.xlsx-160282020");			
			test.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
