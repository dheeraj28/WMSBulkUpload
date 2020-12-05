import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.model.InternalSheet;

public class ExcelTest {
		public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException {
		
		
		try {
			
/*			String filePath2 ="C:\\Bulk Upload\\t_013 form.xlsx";
			File exportfile = new File(filePath2);
			exportfile.createNewFile(); 

			InputStream inp0 = new FileInputStream(new File(filePath2));
			Workbook wb_xssf0 = new XSSFWorkbook(inp0);
			Sheet sheet0 = wb_xssf0.getSheetAt(0);
			Workbook wb=sheet0.getWorkbook();
			Row row0 = sheet0.getRow(3);
			*/
			FileInputStream fis=new FileInputStream("C:\\Bulk Upload\\t_013 form.xlsx");    
		    HSSFWorkbook hWorkbook = (HSSFWorkbook) WorkbookFactory.create(fis);
		    HSSFSheet hSheet = hWorkbook.getSheetAt(0);
		    Class c = org.apache.poi.hssf.usermodel.HSSFSheet.class;
		    Field field = c.getDeclaredField("_sheet");
		    field.setAccessible(true);
		    Object internalSheet = field.get(hSheet);
		    InternalSheet is = (InternalSheet) internalSheet;
		    DataValidityTable dvTable = is.getOrCreateDataValidityTable();
		    Class c2 = org.apache.poi.hssf.record.aggregates.DataValidityTable.class;
		    Field field2 = c2.getDeclaredField("_validationList");
		    field2.setAccessible(true);
		    Object records = field2.get(dvTable);
		    ArrayList<DVRecord> dvRecords = (ArrayList<DVRecord>) records;
		    
		    for (DVRecord dvr : dvRecords) { 		      
		           System.out.println(dvr); 		
		      } 


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void testExcel2() {
		//public static void main(String[] args) {	
			Name namedRange;
		try {
			String filePath2 ="C:\\Bulk Upload\\H03 Std form.xlsx";
			File exportfile = new File(filePath2);
			exportfile.createNewFile();

			InputStream inp0 = new FileInputStream(new File(filePath2));
			Workbook wb_xssf0 = new XSSFWorkbook(inp0);
			Sheet sheet0 = wb_xssf0.getSheetAt(0);
			Row row0 = sheet0.getRow(0);
			int lastrowno=sheet0.getLastRowNum();
			
			for(int n=0;n<65;n++){	
				//row0 = sheet0.getRow(n);
				System.out.println(sheet0.getRow(n).getCell(0).getRichStringCellValue().getString());
				
/*				namedRange = workbook.createName();
				namedRange.setNameName(key);
				reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
				namedRange.setRefersToFormula(reference);*/
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
		public void testExcel() throws IOException {
		//some data
		Map<String, String[]> categoryItems = new HashMap<String, String[]>();
		categoryItems.put("Countries", new String[]{"France", "Germany", "Italy"});
		categoryItems.put("Capitals", new String[]{"Paris", "Berlin", "Rome"});
		categoryItems.put("Fruits", new String[]{"Apple", "Peach", "Banana", "Orange"});

		Workbook workbook = new XSSFWorkbook();

		//hidden sheet for list values
		Sheet sheet = workbook.createSheet("test form.xlsx");

		Row row;  
		Name namedRange;
		String colLetter;
		String reference;

		int c = 0;
		//put the data in
		for (String key : categoryItems.keySet()) {
			int r = 0;
			row = sheet.getRow(r); 
			if (row == null) row = sheet.createRow(r); 
			r++;
			row.createCell(c).setCellValue(key);
			String[] items = categoryItems.get(key);
			for (String item : items) {
				row = sheet.getRow(r); 
				if (row == null) row = sheet.createRow(r); 
				r++;
				row.createCell(c).setCellValue(item);
				System.out.println("#"+item);
			}

			//create names for the item list constraints, each named from the current key
			colLetter = CellReference.convertNumToColString(c);
			namedRange = workbook.createName();
			namedRange.setNameName(key);
			reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
			namedRange.setRefersToFormula(reference);
			System.out.println(reference);
			c++;
		}

		//create name for Categories list constraint
		colLetter = CellReference.convertNumToColString((c-1));
		namedRange = workbook.createName();
		namedRange.setNameName("Categories");
		reference = "ListSheet!$A$1:$" + colLetter + "$1";
		namedRange.setRefersToFormula(reference);

		//unselect that sheet because we will hide it later
		sheet.setSelected(false);


		//visible data sheet
		sheet = workbook.createSheet("Sheet1");

		sheet.createRow(0).createCell(0).setCellValue("Select Category");
		sheet.getRow(0).createCell(1).setCellValue("Select item from that category");

		//sheet.setActiveCell(new CellAddress("A2"));

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

		//data validations
		DataValidationHelper dvHelper = sheet.getDataValidationHelper();
		//data validation for categories in A2:
		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Categories");
		CellRangeAddressList addressList = new CellRangeAddressList(1, 1, 0, 0);            
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		sheet.addValidationData(validation);

		//data validation for items of the selected category in B2:
		dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT($A$2)");
		addressList = new CellRangeAddressList(1, 1, 1, 1);            
		validation = dvHelper.createValidation(dvConstraint, addressList);
		sheet.addValidationData(validation);

		//hide the ListSheet
		workbook.setSheetHidden(0, true);
		//set Sheet1 active
		workbook.setActiveSheet(1);

		try {
			FileOutputStream out = new FileOutputStream("test123.xlsx");
			workbook.write(out);
			workbook.close();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
