package com.ywc.stock.test;

import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.junit.Test;

import java.io.File;

public class TestUtils {


    /**
     * 将clu社团文件转换成csv
     */
    @Test
    public void convertCluToCsvTest() {
        Utils.convertCluToCsv(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.COMMUNITY_FOLDER
                + Constant.PEARSON_FOLDER + "id/clu/0.8.clu"));
    }

    /**
     * 将目录中的clu转换成csv
     */
    @Test
    public void convertClusToCsvsTest() {
        Utils.convertClusToCsvs(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.COMMUNITY_FOLDER
                + Constant.PEARSON_FOLDER + "id/clu"));
    }

    /**
     * 将社团文件按id排序
     */
    @Test
    public void sortCommunityCsvByCommunityIdTest() {
        Utils.sortCommunityCsvByCommunityId(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + Constant.COMMUNITY_FOLDER + Constant.PEARSON_FOLDER + "id/csv"));
    }

    /**
     * 把目录下的社团id文件转换成name文件
     */
    @Test
    public void convertCommunityIdFilesToNameFilesTest() {
        Utils.convertCommunityIdFilesToNameFiles(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + Constant.COMMUNITY_FOLDER + Constant.PEARSON_FOLDER + "id/csv"));
    }


    /**
     * 将id社团文件转换成name
     */
    @Test
    public void convertConmmunityIdFileToNameFileTest() {
        Utils.convertConmmunityIdFileToNameFile(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
                + Constant.COMMUNITY_FOLDER + Constant.PEARSON_FOLDER + "id/csv/0.8.csv"));
    }

    /**
     * 把价格矩阵输出到csv
     */
    @Test
    public void testWritePriceMatrixToCsv() {
        Utils.writePriceMatrixToCsv();
    }

    /**
     * 把对数收益率矩阵输出到csv
     */
    @Test
    public void writeLogarithmicYieldeMatrixToCsvTest() {
        Utils.writeLogarithmicYieldeMatrixToCsv();
    }
}
