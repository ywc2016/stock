package com.ywc.stock.entity;

import com.ywc.stock.util.Utils;

import java.util.ArrayList;

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

    /**
     * 根据id查找stock
     *
     * @param stockId
     * @return
     */
    public Stock findStockById(int stockId) {
        for (Stock stock : this) {
            if (stock.getId() == stockId) {
                return stock;
            }
        }
        return null;
    }
}
