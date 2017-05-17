package com.ywc.stock.entity;

import java.util.ArrayList;

import com.ywc.stock.util.Utils;

public class StockList extends ArrayList<Stock> {

	/**
	 * 给所有stock的缺省值赋值
	 */
	public void completeDefaultValue() {
		for (Stock stock : this) {
			stock.completeDefaultValue();
		}
	}

	public void addIndex() {
		Utils.addIndex(this);
	}

}
