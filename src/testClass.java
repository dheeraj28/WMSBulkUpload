import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class testClass {
public static void main(String[] args) {
	
	try {
		InputStream inp = new FileInputStream(new File("C:\\Users\\DheerajJoshi\\Desktop\\business bulk test Files\\prod2\\WSLED_Castle-r_14102020 - 3.xlsx"));
		Workbook wb_xssf = new XSSFWorkbook(inp);
		Sheet sheet = wb_xssf.getSheetAt(0);
		Row row = sheet.getRow(2);
		int lastrowno=sheet.getLastRowNum();
		System.out.println(lastrowno);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

}
