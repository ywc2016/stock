package com.ywc.stock.test;

import com.ywc.stock.relation.SpearmanRelation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.junit.Test;

/**
 * Created by ywcrm on 2017/5/24.
 */
public class SpearmanRelationTest {
    @Test
    public void SpearmanTest() {
        double[][] matrix = {{1.0, 4.0}, {2.0, 7}, {1.5, 9}};
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
        RealMatrix realMatrix = spearmansCorrelation.computeCorrelationMatrix(matrix);
        System.out.println(realMatrix);
    }

    /**
     * 输出spearman相关系数矩阵到文件
     */
    @Test
    public void outputSpearmanRelationMatrixToCsvTest() {
        new SpearmanRelation().outputRelationMatrixToCsv();
    }

    /**
     * 将边文件写到csv
     */
    @Test
    public void WriteEdgesToCsvTest() {
        SpearmanRelation spearmanRelation = new SpearmanRelation();
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            spearmanRelation.updateGraph(threshold);
            spearmanRelation.writeEdgesToCsv();
        }

    }

}
