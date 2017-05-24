package com.ywc.stock.test;

import com.ywc.stock.entity.StockList;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class TestReadFile {

	@Test
	public void testReadXlsx() {
		Utils.readXslxToCsv();
	}

	@Test
	public void testPOI() {
		File exelFile = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "/1.xlsx");
		if (exelFile.isFile() && exelFile.exists()) {
			System.out.println("is file");
		}
		try {
			FileInputStream fip = new FileInputStream(exelFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fip);
			XSSFSheet sheet = workbook.getSheetAt(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDate() {
		Date date = new Date();
		System.out.println(date.getMonth());
	}

	@Test
	public void testReadCsvToStockList() {
		Utils.readCsvToStockList();
	}

	@Test
	public void testBuildIndex() {
		Utils.buildIndex();
	}

	@Test
	public void testAddIndex() {
		StockList stockList = Utils.readCsvToStockList();
		Utils.addIndex(stockList);
		Utils.printStockListBriefly(stockList);
	}

	@Test
	public void testCompleteDefaultValue() {
		StockList stockList = Utils.readCsvToStockList();
		stockList.completeDefaultValue();
		Utils.addIndex(stockList);
		Utils.printStockListInDetail(stockList);
	}

	@Test
	public void testReadPriceMatrix() {
		Utils.readPriceMatrix();
	}



	@Test
	public void readMatrixTest() {
		Utils.readMatrix(new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "logYieldMatrix.csv"));
	}

}
