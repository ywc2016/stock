package com.ywc.stock.entity;

import java.util.ArrayList;
import java.util.Date;

public class Stock {

	private String wincode;// 股票代码
	private String name;// 股票名称
	private int id;// 股票id号
	private ArrayList<Date> dateArrayList = new ArrayList<Date>();// 日期列表
	private ArrayList<Double> closePriceArrayList = new ArrayList<Double>();// 收盘价列表

	public Stock() {
	}

	public Stock(String wincode, String name, int id, ArrayList<Date> dateArrayList,
			ArrayList<Double> closePriceArrayList) {
		super();
		this.wincode = wincode;
		this.name = name;
		this.id = id;
		this.dateArrayList = dateArrayList;
		this.closePriceArrayList = closePriceArrayList;
	}

	public String getWincode() {
		return wincode;
	}

	public void setWincode(String wincode) {
		this.wincode = wincode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Date> getDateArrayList() {
		return dateArrayList;
	}

	public void setDateArrayList(ArrayList<Date> dateArrayList) {
		this.dateArrayList = dateArrayList;
	}

	public ArrayList<Double> getClosePriceArrayList() {
		return closePriceArrayList;
	}

	public void setClosePriceArrayList(ArrayList<Double> closePriceArrayList) {
		this.closePriceArrayList = closePriceArrayList;
	}

	// 插入一条收盘价和日期记录
	public void add(Date date, double closePrice) {
		this.dateArrayList.add(date);
		this.closePriceArrayList.add(new Double(closePrice));
	}

	/**
	 * 给缺省值赋值
	 */
	public void completeDefaultValue() {
		for (int i = 0; i < this.closePriceArrayList.size(); i++) {
			if (closePriceArrayList.get(i) == 0) {
				if (i == 0) {
					closePriceArrayList.set(i, 0D);
				} else {
					closePriceArrayList.set(i, closePriceArrayList.get(i - 1));
				}
			}
		}
	}
}
