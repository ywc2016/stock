package com.ywc.stock.community;

import com.ywc.stock.entity.DateAndPrice;
import com.ywc.stock.entity.DateAndPriceList;
import com.ywc.stock.entity.Stock;
import com.ywc.stock.entity.StockList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ywcrm on 2017/6/16.
 */
public class Community extends ArrayList<Stock> {
    /**
     * 社团的标号
     */
    private int communityId;

    public Community() {

    }

    public Community(int communityId) {
        this.communityId = communityId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public List<DateAndPrice> calculateMeanPrice(StockList allStocks) {
        if (this.size() == 0) {
            System.out.println("社团中没有股票,无法计算均值.");
            return null;
        }
        DateAndPriceList dateAndPriceList = new DateAndPriceList();
        for (Date date : this.get(0).getDateArrayList()) {
            dateAndPriceList.add(new DateAndPrice(date, getMeanPriceByDate(date, allStocks)));
        }
        return dateAndPriceList;
    }

    public double getMeanPriceByDate(Date date, StockList allStocks) {
        double total = 0f;
        for (Stock stock : this) {

            Stock stock1 = allStocks.findStockById(stock.getId());
            total += stock1.getPriceByDate(date);
        }
        return total / this.size();
    }
}
