package com.ywc.stock.relation;

import com.ywc.stock.entity.DegreeAndFrequency;
import com.ywc.stock.entity.DegreeAndFrequencytList;
import com.ywc.stock.inter.RelationInter;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class PearsonRelation implements RelationInter {
    private double[][] relationMatrix;

    private SimpleGraph<Integer, DefaultEdge> simpleGraph;

    private double threshold;

    public double getThreshold() {
        return threshold;
    }

    public double[][] getRelationMatrix() {
        return relationMatrix;
    }

    public SimpleGraph<Integer, DefaultEdge> getSimpleGraph() {
        return simpleGraph;
    }

    public PearsonRelation() {
        readRelationMatrixFromFile();
        initialSimpleGraph();
    }

    public PearsonRelation(double threshold) {
        this.threshold = threshold;
        readRelationMatrixFromFile();
        initialSimpleGraph();
    }

    /**
     * 将pearson相关系数矩阵输出到csv文件
     */
    public void outputRelationMatrixToCsv() {
        double[][] matrix = Utils.readlogYieldMatrix();
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix pValuesMatrix = pearsonsCorrelation.getCorrelationPValues();
        RealMatrix standardErrorsMatrix = pearsonsCorrelation.getCorrelationStandardErrors();
        // System.out.println("相关性矩阵:");
        // System.out.println(correlationMatrix);
        // System.out.println("P-Value:");
        // System.out.println(pValuesMatrix);
        // System.out.println("标准误差矩阵:");
        // System.out.println(standardErrorsMatrix);
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "pearsonCorrelationMatrix.csv");
        if (file.exists()) {
            System.out.println("pearsonCorrelationMatrix.csv已经存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Utils.writeMatrixToFile(correlationMatrix, file);
    }

    @Override
    public void readRelationMatrixFromFile() {
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "pearsonCorrelationMatrix.csv");
        if (!file.exists()) {
            System.out.println("源文件pearsonCorrelationMatrix.csv不存在.");
            return;
        }
        this.relationMatrix = Utils.readMatrix(file);
    }

    @Override
    public void initialSimpleGraph() {
        initialSimpleGraph(this.threshold);
    }

    @Override
    public void initialSimpleGraph(double threshold) {
        simpleGraph = new SimpleGraph<>(DefaultEdge.class);
        double[][] filteredMatrix = Utils.filterMatrixWithThreshold(this.relationMatrix, threshold);
        // 加节点
        for (int i = 0; i < filteredMatrix.length; i++) {
            if (!simpleGraph.containsVertex(i + 1)) {
                this.simpleGraph.addVertex(i + 1);
            }
        }
        // 加边
        for (int i = 0; i < filteredMatrix.length; i++) {
            for (int j = 0; j < filteredMatrix[0].length && i > j; j++) {
                if (filteredMatrix[i][j] == 1) {
                    simpleGraph.addEdge(i + 1, j + 1);
                }
            }
        }
    }

    public void writeEdgesToCsv() {
        File dir = new File(
                Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER + Constant.PEARSON_FOLDER + "id");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double d = Double.parseDouble(df.format(threshold));
        File file = new File(dir.getPath() + "/csv/" + d + ".csv");
        if (file.exists()) {
            System.out.println(dir.getPath() + "/csv/" + d + ".csv" + "已经存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("source,target,weight,type\r\n");
            Set<DefaultEdge> edges = simpleGraph.edgeSet();
            Iterator<DefaultEdge> iterator = edges.iterator();
            for (; iterator.hasNext(); ) {
                DefaultEdge defaultEdge = iterator.next();
                // 默认权重都为1
                fileWriter.write(simpleGraph.getEdgeSource(defaultEdge) + "," + simpleGraph.getEdgeTarget(defaultEdge)
                        + "," + 1 + "," + "undirected" + "\r\n");
            }
            System.out.println(file.getName() + "已创建.");
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 把变文件写到txt
     */
    public void writeEdgesToTxt() {
        File dir = new File(
                Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER + Constant.PEARSON_FOLDER + "id");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double d = Double.parseDouble(df.format(threshold));
        File file = new File(dir.getPath() + "/txt/" + d + ".txt");
        if (file.exists()) {
            System.out.println(dir.getPath() + "/txt/" + d + ".txt" + "已经存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            Set<DefaultEdge> edges = simpleGraph.edgeSet();
            Iterator<DefaultEdge> iterator = edges.iterator();
            for (; iterator.hasNext(); ) {
                DefaultEdge defaultEdge = iterator.next();
                // 默认权重都为1
                fileWriter.write(simpleGraph.getEdgeSource(defaultEdge) + " " + simpleGraph.getEdgeTarget(defaultEdge)
                        + " " + 1 + "\r\n");
            }
            System.out.println(file.getName() + "已创建.");
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void updateGraph(double threshold) {
        // TODO Auto-generated method stub
        this.threshold = threshold;
        simpleGraph = null;
        initialSimpleGraph(threshold);
    }

    /**
     * 输出网络中的边数和节点数
     */
    public void outputVertexesAndEdges() {
        DecimalFormat df = new DecimalFormat("#.##");

        File file = new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + "numOfVertexesAndEdges/pearson/" + Double.parseDouble(df.format(threshold)) + ".csv");
        try {
            FileUtils.writeStringToFile(file, "numOfVertexes" + "," + "numOfEdges" + "\r\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int numOfVertexes = this.simpleGraph.vertexSet().size();
        int numOfEdges = this.simpleGraph.edgeSet().size();
        try {
            FileUtils.writeStringToFile(file, numOfVertexes + "," + numOfEdges + "\r\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出度分布
     */
    public void outPutDistributionOfDegree() {
        DegreeAndFrequencytList degreeAndFrequencylList = new DegreeAndFrequencytList();
        degreeAndFrequencylList.setThreshold(this.threshold);
        Set<Integer> set = this.simpleGraph.vertexSet();
        //加度为0的点
        for (int i = 0; i < this.simpleGraph.vertexSet().size(); i++) {
            degreeAndFrequencylList.add(new DegreeAndFrequency(i, 0));
        }
        for (Integer v : set) {
            degreeAndFrequencylList.addDegree(this.simpleGraph.degreeOf(v));
        }

        Collections.sort(degreeAndFrequencylList);
        degreeAndFrequencylList.writeTocsv();
    }


    public static void main(String[] args) {
        PearsonRelation pearsonRelation = new PearsonRelation();

//        输出关系矩阵到csv
//        pearsonRelation.outputRelationMatrixToCsv();

//        输出边文件
//        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
//            pearsonRelation.updateGraph(threshold);
//            pearsonRelation.writeEdgesToCsv();
//        }

//        将社团id文件排序
//        Utils.sortCommunityCsvByCommunityId(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
//                + Constant.COMMUNITY_FOLDER + Constant.PEARSON_FOLDER + "id"));

//        将id文件转换成name文件
//        Utils.convertCommunityIdFilesToNameFiles(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
//                + Constant.COMMUNITY_FOLDER + Constant.PEARSON_FOLDER + "id"));

        //        输出网络的节点数和边数
//        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
//            pearsonRelation.updateGraph(threshold);
//            pearsonRelation.outputVertexesAndEdges();
//        }

        //        输出网络的度分布
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            pearsonRelation.updateGraph(threshold);
            pearsonRelation.outPutDistributionOfDegree();
        }

    }
}
