package com.ywc.stock.test;

import com.ywc.stock.relation.PearsonRelation;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.apache.commons.math3.analysis.function.Log;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import javax.swing.*;
import java.io.File;

public class TestOthers {
    @Test
    public void testLog() {
        Log log = new Log();
        System.out.println(log.value(2.71) + " " + log.value(1));

    }

    @Test
    public void graphTests() {
        SimpleGraph<Integer, DefaultEdge> simpleGraph = new SimpleGraph<>(DefaultEdge.class);
        simpleGraph.addVertex(1);
        simpleGraph.addVertex(2);
        simpleGraph.addVertex(3);
        int n = simpleGraph.vertexSet().size();
        System.out.println(n);
        simpleGraph.addEdge(new Integer(1), new Integer(2));
        simpleGraph.addEdge(2, 1);
        int m = simpleGraph.edgesOf(1).size();
        System.out.println(m);
    }

    @Test
    public void JGraphTest() {
        JGraph jGraph = new JGraph();
        jGraph.add(new JOptionPane());
        jGraph.doLayout();
        jGraph.setVisible(true);
    }

    @Test
    public void convertIdFileToNameFileTest() {
        Utils.convertIdFileToNameFile(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER
                + Constant.PEARSON_FOLDER + "id/0.05.csv"));
    }

    @Test
    public void convertIdFilesToNameFilesTest() {
        Utils.convertIdFilesToNameFiles(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER
                + Constant.PEARSON_FOLDER + "id/csv"));
    }


    /**
     * 测试把价格矩阵转换成对数收益率矩阵
     */
    @Test
    public void convertPriceMatrixTologyieldMatrixTest() {
        Utils.convertPriceMatrixTologyieldMatrix(Utils.readPriceMatrix());

    }

    /**
     * 测试过滤矩阵
     */
    @Test
    public void filterMatrixWithThresholdTest() {
        double d[][] = Utils.filterMatrixWithThreshold(new PearsonRelation().getRelationMatrix(), 0.1);
        Utils.printMatrix(d);
    }

    /**
     * 测试初始化图
     */
    @Test
    public void initialSimpleGraphTest() {
        PearsonRelation pearsonRelation = new PearsonRelation();
        SimpleGraph simpleGraph = pearsonRelation.getSimpleGraph();
        System.out.println("节点数:" + simpleGraph.vertexSet().size());
        System.out.println("边数:" + simpleGraph.edgeSet().size());
    }

    /**
     * 将边文件写到txt方面使用infomap算法
     */
    @Test
    public void WriteEdgesToTxtTest() {
        PearsonRelation pearsonRelation = new PearsonRelation();
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            pearsonRelation.updateGraph(threshold);
            pearsonRelation.writeEdgesToTxt();
        }

    }

    @Test
    public void WriteEdgesToAllTest() {
        PearsonRelation pearsonRelation = new PearsonRelation();
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            pearsonRelation.updateGraph(threshold);
            pearsonRelation.writeEdgesToTxt();
            pearsonRelation.writeEdgesToCsv();
        }

    }

    @Test
    public void logTest() {
        Log log = new Log();
        System.out.println(log.value(2.7));
    }

}
