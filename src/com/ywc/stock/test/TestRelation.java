package com.ywc.stock.test;

import java.io.File;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jgraph.graph.DefaultGraphCell;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import com.ywc.stock.inter.RelationInter;
import com.ywc.stock.relation.PearsonRelation;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;

public class TestRelation {
	@Test
	public void testPearson() {
		double[][] matrix = { { 1.0, 4.0 }, { 2.0, 5.0 }, { 3, 6 } };
		PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
		System.out.println(pearsonsCorrelation.getCorrelationStandardErrors());
	}

	@Test
	public void TestMatrix() {
		double[][] matrix = { { 1.0, 4.0 }, { 2.0, 5.0 }, { 3, 6 } };
		System.out.println(matrix[0][1]);

	}

	@Test
	public void testWritePriceMatrixToCsv() {
		Utils.writePriceMatrixToCsv();
	}

	/**
	 * 输出pearson相关系数矩阵到文件
	 */
	@Test
	public void testOutputPearsonRelationMatrixToCsv() {
		new PearsonRelation().outputRelationMatrixToCsv();
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
	 * 
	 */
	@Test
	public void filterMatrixWithThresholdTest() {
		double d[][] = Utils.filterMatrixWithThreshold(new PearsonRelation().getRelationMatrix(), 0.1);
		Utils.printMatrix(d);
	}

	@Test
	public void initialSimpleGraphTest() {
		PearsonRelation pearsonRelation = new PearsonRelation();
		SimpleGraph simpleGraph = pearsonRelation.getSimpleGraph();
		System.out.println("节点数:" + simpleGraph.vertexSet().size());
		System.out.println("边数:" + simpleGraph.edgeSet().size());
	}

	/**
	 * 将边文件写到csv
	 * 
	 */
	@Test
	public void WriteEdgesToCsvTest() {
		PearsonRelation pearsonRelation = new PearsonRelation();
		for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
			pearsonRelation.updateGraph(threshold);
			pearsonRelation.writeEdgesToCsv();
		}

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

}
