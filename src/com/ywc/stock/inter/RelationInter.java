package com.ywc.stock.inter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public interface RelationInter {
	/**
	 * 将相关系数矩阵输出到文件
	 */
	public void outputRelationMatrixToCsv();

	/**
	 * 从文件读入相关系数矩阵
	 * 
	 * @param file
	 */
	public void readRelationMatrixFromFile();

	/**
	 * 初始化图,使用默认阈值
	 */
	public void initialSimpleGraph();

	/**
	 * 用特定阈值初始化图
	 */
	public void initialSimpleGraph(double threshold);

	/**
	 * 将社团clu文件转换成gephi可读取的csv
	 */
	/*
	 * public void convertCluToCsv(File file);
	 * 
	 *//**
		 * 将目录中的社团clu文件转换成gephi可读取的csv
		 *//*
		 * public void convertClusToCsvs(File dir);
		 */

	/**
	 * 将变文件写到csv
	 */
	public void writeEdgesToCsv();

	/**
	 * 将边文件写到txt
	 *
	 */
	public void writeEdgesToTxt();

	/**
	 * 用不同的阈值更新图
	 * 
	 * @param threshold
	 */
	public void updateGraph(double threshold);

}
