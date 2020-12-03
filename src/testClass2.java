
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class testClass2 {
	public static void main(String[] args)  {
		try {
		int initrowNew=3;
		int Bulklastrowno=0;
		int actualBulkSheetlastRow=0;
		int lastRowCounter=0;
		int blankRows[]= new int[10];
		String blankrow="";
		
		Workbook wb_xssf = null;
		Sheet sheet = null;
		Row row = null;

		InputStream inp = new FileInputStream(new File("C:\\Users\\DheerajJoshi\\Desktop\\business bulk test Files\\prod2\\WSLED_Castle-r_14102020 - 3.xlsx"));
		wb_xssf = new XSSFWorkbook(inp);
		sheet = wb_xssf.getSheetAt(0);
		row = sheet.getRow(2);
		int lastcolno=row.getLastCellNum();
		Bulklastrowno=sheet.getLastRowNum();
		System.out.println(lastcolno);
		for(int n=initrowNew;n<Bulklastrowno;n++) {
			System.out.println(n);
			Cell c=sheet.getRow(n).getCell(0);
			sheet.getRow(n).getCell(0).getRichStringCellValue().getString();
			if(lastRowCounter==10){
				actualBulkSheetlastRow=n-10;
				break;
			}
			if(c==null || "".equals(c) || c.getCellType() == Cell.CELL_TYPE_BLANK){
				System.out.print("ASDF");
				for(int m=1;m<lastcolno;m++){

					Cell c1=sheet.getRow(n).getCell(m);
					System.out.print("@"+m+"@");
					if(lastRowCounter==10){
						break;
					}
					else if(c1!=null && !"".equals(c1) && c1.getCellType() != Cell.CELL_TYPE_BLANK ) {
						System.out.print(m+"@"+sheet.getRow(n).getCell(m)+"@");
						lastRowCounter=0;
						break;
					}else if(m==(lastcolno-1)){						
						lastRowCounter++;
						blankrow+=n;					
					}
				}
				System.out.println();
			}				
		}
		if(actualBulkSheetlastRow==0) {actualBulkSheetlastRow=Bulklastrowno;}
		System.out.println(actualBulkSheetlastRow);
		FileOutputStream outFile =new FileOutputStream(new File("C:\\Users\\DheerajJoshi\\Desktop\\business bulk test Files\\prod2\\WSLED_Castle-r_14102020 - 2.xlsx"));
		wb_xssf.write(outFile);
		outFile.close();
	}catch (Exception e) {
		e.printStackTrace();
	}
		}
}
