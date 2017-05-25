package com.ywc.stock.test;

import com.ywc.stock.relation.PearsonRelation;
import com.ywc.stock.util.Utils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

public class TestRelation {
    @Test
    public void testPearson() {
        double[][] matrix = {{1.0, 4.0}, {2.0, 5.0}, {3, 6}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        System.out.println(pearsonsCorrelation.getCorrelationStandardErrors());
    }

    @Test
    public void TestMatrix() {
        double[][] matrix = {{1.0, 4.0}, {2.0, 5.0}, {3, 6}};
        System.out.println(matrix[0][1]);

    }


    /**
     * 输出pearson相关系数矩阵到文件
     */
    @Test
    public void testOutputPearsonRelationMatrixToCsv() {
        new PearsonRelation().outputRelationMatrixToCsv();
    }




    /**
     * 将边文件写到csv
     */
    @Test
    public void WriteEdgesToCsvTest() {
        PearsonRelation pearsonRelation = new PearsonRelation();
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            pearsonRelation.updateGraph(threshold);
            pearsonRelation.writeEdgesToCsv();
        }

    }



}
