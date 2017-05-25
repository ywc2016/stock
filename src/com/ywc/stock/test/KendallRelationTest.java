package com.ywc.stock.test;

import com.ywc.stock.relation.KendallRelation;
import com.ywc.stock.relation.SpearmanRelation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.junit.Test;

/**
 * Created by ywcrm on 2017/5/24.
 */
public class KendallRelationTest {
    @Test
    public void KendallTest() {
        double[][] matrix = {{1.0, 4.0}, {2.0, 5}, {3, 6}};
        KendallsCorrelation kendallsCorrelation = new KendallsCorrelation(matrix);
        System.out.println(kendallsCorrelation.getCorrelationMatrix());
    }

    /**
     * 输出kendall相关系数矩阵到文件
     */
    @Test
    public void outputSpearmanRelationMatrixToCsvTest() {

        new KendallRelation().outputRelationMatrixToCsv();
    }
}
