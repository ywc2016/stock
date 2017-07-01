package com.ywc.stock.entity;

import java.util.Date;

/**
 * Created by ywcrm on 2017/6/25.
 */
public class DateAndPrice implements Comparable<DateAndPrice> {
    private Date date;
    private double price;

    public DateAndPrice(Date date, double meanPriceByDate) {
        this.date = date;
        this.price = meanPriceByDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(DateAndPrice o) {
        return this.date.compareTo(o.getDate());
    }
}
