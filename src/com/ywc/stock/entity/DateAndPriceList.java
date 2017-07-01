package com.ywc.stock.entity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ywcrm on 2017/6/25.
 */
public class DateAndPriceList extends ArrayList<DateAndPrice> {


    public DateAndPrice findDateAndPriceByDate(Date date) {
        for (DateAndPrice dateAndPrice : this) {
            if (dateAndPrice.getDate().equals(date)) {
                return dateAndPrice;
            }
        }
        return null;
    }
}
