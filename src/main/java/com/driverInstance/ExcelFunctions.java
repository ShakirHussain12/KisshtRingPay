package com.driverInstance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFunctions {

	// Generic method to return the number of rows in the sheet.
	public static int getRowCount(String xlPath, String sheet) {
		int rc = 0;
		try {
			FileInputStream fis = new FileInputStream(xlPath);
			Workbook wb = WorkbookFactory.create(fis);

			Sheet s = wb.getSheet(sheet);
			rc = s.getLastRowNum();
		} catch (Exception e) {

			System.out.println(e);
		}

		return rc;
	}

	// Generic method to return the column values in the sheet.
	public static String getCellValue(String xlPath, String sheet, int row, int col) {
		String data = "";
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlPath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			data = myExcelSheet.getRow(row).getCell(col).toString();
		}
		catch (Exception e) {
//				System.out.println(e);
		}
		return data;
	}

	public static void writeData(String xlpath, String sheet, int row, int col, String data) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			myExcelSheet.createRow(row).createCell(col).setCellValue(data);
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static int columncount(String xlPath, String sheet, int rownum) throws FileNotFoundException, IOException {
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlPath));
		XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
		XSSFRow myExcelRow = myExcelSheet.getRow(rownum);
		int colNum = myExcelRow.getLastCellNum();
		return colNum;
	}

	

}
