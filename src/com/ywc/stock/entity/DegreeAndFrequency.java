package com.ywc.stock.entity;

/**
 * Created by ywcrm on 2017/6/23.
 */
public class DegreeAndFrequency implements Comparable<DegreeAndFrequency> {
    private int degree;
    private int frequency;

    public DegreeAndFrequency(int degree, int frequency) {
        this.degree = degree;
        this.frequency = frequency;
    }


    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(DegreeAndFrequency o) {

        return this.getDegree() - o.getDegree();
    }
}
