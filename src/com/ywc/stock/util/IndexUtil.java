package com.ywc.stock.util;

import com.ywc.stock.entity.Stock;
import com.ywc.stock.entity.StockList;

public class IndexUtil {

	private StockList stockList;

	public IndexUtil() {
		stockList = Utils.readCsvToStockList();
		stockList.addIndex();
	}

	public int findIndexByName(String name) {
		for (Stock stock : stockList) {
			if (stock.getName().equals(name)) {
				return stock.getId();
			}
		}
		return -1;
	}

	public String findNameById(int id) {
		for (Stock stock : stockList) {
			if (stock.getId() == id) {
				return stock.getName();
			}
		}
		return null;
	}

}
