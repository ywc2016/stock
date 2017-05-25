package com.ywc.stock.test;

import com.ywc.stock.relation.SpearmanRelation;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.junit.Test;

import java.io.File;

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

    /**
     * 将社团文件按id排序
     */
    @Test
    public void sortCommunityCsvByCommunityIdTest() {
        Utils.sortCommunityCsvByCommunityId(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + Constant.COMMUNITY_FOLDER + Constant.SPEARMAN_FOLDER + "id"));
    }

    /**
     * 把目录下的社团id文件转换成name文件
     */
    @Test
    public void convertCommunityIdFilesToNameFilesTest() {
        Utils.convertCommunityIdFilesToNameFiles(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + Constant.COMMUNITY_FOLDER + Constant.SPEARMAN_FOLDER + "id"));
    }
}
