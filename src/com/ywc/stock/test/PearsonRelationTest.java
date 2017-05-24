package com.ywc.stock.test;

import com.ywc.stock.relation.PearsonRelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

/**
 * Created by ywcrm on 2017/5/24.
 */
public class PearsonRelationTest {
    @Test
    public void PearsonTest() {
        double[][] matrix = {{1.0, 4.0}, {2.0, 5.0}, {3, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        System.out.println(pearsonsCorrelation.getCorrelationMatrix());
    }

    /**
     * 输出pearson相关系数矩阵到文件
     */
    @Test
    public void outputPearsonRelationMatrixToCsvTest() {
        new PearsonRelation().outputRelationMatrixToCsv();
    }


}
