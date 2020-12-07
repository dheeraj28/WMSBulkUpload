
public class QuerySt {
	public String getRetailers() {
		StringBuffer QueryText=new StringBuffer("");
		QueryText.append("select distinct id as RetailerID from PARTICIPANT_MASTER where name!='dummy' ");
		String Query=QueryText.toString();
		return Query;		
	}
	public String getNewRegBulkForms(String Subject,String Formname) {
		System.out.println("here");
		StringBuffer QueryText=new StringBuffer("");
		if("B01".equalsIgnoreCase(Formname)) {
			QueryText.append("select REQUEST_NO,REQUEST_STATUS,ERROR_TEXT from METERING where REQUEST_NO in (select REQUEST_NO from CTP_EMAILS_PROCESSED_INFO where MAIL_SUBJECCT = '"+Subject+"')");
		}else {
			QueryText.append("select REQUEST_NO,REQUEST_STATUS,ERROR_TEXT from REGISTRATION where REQUEST_NO in (select REQUEST_NO from CTP_EMAILS_PROCESSED_INFO where MAIL_SUBJECCT = '"+Subject+"')");
		}
		//QueryText.append("(select REQUEST_NO,REQUEST_STATUS,ERROR_TEXT from REGISTRATION where REQUEST_NO in (select REQUEST_NO from CTP_EMAILS_PROCESSED_INFO where MAIL_SUBJECCT = '"+Subject+"'))UNION (select REQUEST_NO,REQUEST_STATUS,ERROR_TEXT from METERING where REQUEST_NO in (select REQUEST_NO from CTP_EMAILS_PROCESSED_INFO where MAIL_SUBJECCT = '"+Subject+"'))");
		//QueryText.append("select REQUEST_NO,REQUEST_STATUS,ERROR_TEXT from REGISTRATION where REQUEST_NO in (103639,103638)");
		String Query=QueryText.toString();
		System.out.println(Query);
		return Query;		
	}
}
