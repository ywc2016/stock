package com.ywc.stock.entity;

import com.ywc.stock.util.Constant;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ywcrm on 2017/6/23.
 */
public class DegreeAndFrequencytList extends ArrayList<DegreeAndFrequency> {

    private double threshold;

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public DegreeAndFrequency findVertexAndDegreeByDegree(int degree) {
        for (DegreeAndFrequency degreeAndFrequency : this) {
            if (degreeAndFrequency.getDegree() == degree) {
                return degreeAndFrequency;
            }
        }
        return null;
    }

    public void addDegree(int degree) {
        DegreeAndFrequency degreeAndFrequency = findVertexAndDegreeByDegree(degree);
        if (degreeAndFrequency != null) {
            degreeAndFrequency.setFrequency(degreeAndFrequency.getFrequency() + 1);
        } else {
            DegreeAndFrequency degreeAndFrequency1 = new DegreeAndFrequency(degree, 1);
            this.add(degreeAndFrequency1);
        }
    }

    public void writeTocsv() {
        DecimalFormat df = new DecimalFormat("#.##");
        File file = new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + "distributionOfDegree/" + Double.parseDouble(df.format(threshold)) + ".csv");
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.writeStringToFile(file, "degree,frequency\r\n", true);
            for (DegreeAndFrequency degreeAndFrequency : this) {
                FileUtils.writeStringToFile(file, degreeAndFrequency.getDegree() + ","
                        + degreeAndFrequency.getFrequency() + "\r\n", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file.getName() + "写入完成.");
    }
}
