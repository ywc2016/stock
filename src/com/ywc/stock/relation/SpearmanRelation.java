package com.ywc.stock.relation;

import com.ywc.stock.inter.RelationInter;
import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;

public class SpearmanRelation implements RelationInter {
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

    public SpearmanRelation() {
        readRelationMatrixFromFile();
        initialSimpleGraph();
    }

    public SpearmanRelation(double threshold) {
        this.threshold = threshold;
        readRelationMatrixFromFile();
        initialSimpleGraph();
    }

    /**
     * 将相关系数矩阵输出到csv文件
     */
    public void outputRelationMatrixToCsv() {
        double[][] matrix = Utils.readlogYieldMatrix();
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
        RealMatrix correlationMatrix = spearmansCorrelation.computeCorrelationMatrix(matrix);
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "spearmanCorrelationMatrix.csv");
        if (!file.getParentFile().exists()) {
            System.out.println("目录 " + file.getParent() + " 不存在.将创建");
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            System.out.println("spearmanCorrelationMatrix.csv已经存在,将删除.");
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeMatrixToFile(correlationMatrix, file);
    }

    @Override
    public void readRelationMatrixFromFile() {
        File file = new File(Constant.SOURCE_DATA_FOLDER + Constant.SH_FOLDER + "spearmanCorrelationMatrix.csv");
        if (!file.exists()) {
            System.out.println("源文件spearmanCorrelationMatrix.csv不存在.");
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
        if (relationMatrix == null) {
            return;
        }

        simpleGraph = new SimpleGraph<>(DefaultEdge.class);
        double[][] filteredMatrix = Utils.filterMatrixWithThreshold(this.relationMatrix, threshold);
        // 加边
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
                Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER + Constant.SPEARMAN_FOLDER + "id");
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
            fileWriter.write("source,target,weight\r\n");
            Set<DefaultEdge> edges = simpleGraph.edgeSet();
            Iterator<DefaultEdge> iterator = edges.iterator();
            for (; iterator.hasNext(); ) {
                DefaultEdge defaultEdge = iterator.next();
                // 默认权重都为1
                fileWriter.write(simpleGraph.getEdgeSource(defaultEdge) + "," + simpleGraph.getEdgeTarget(defaultEdge)
                        + "," + 1 + "\r\n");
            }
            System.out.println(file.getName() + "写入完成.");
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
                Constant.RESULT_FOLDER + Constant.SH_FOLDER + Constant.EDGES_FOLDER + Constant.SPEARMAN_FOLDER + "id");
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

        File file = new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER + "numOfVertexesAndEdges/spearman/" + Double.parseDouble(df.format(threshold)) + ".csv");
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

    public static void main(String[] args) {
        SpearmanRelation spearmanRelation = new SpearmanRelation();

//        输出关系矩阵到csv
//        spearmanRelation.outputRelationMatrixToCsv();

//        输出边文件
//        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
//            spearmanRelation.updateGraph(threshold);
//            spearmanRelation.writeEdgesToCsv();
//        }

//        将社团id文件排序
//        Utils.sortCommunityCsvByCommunityId(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
//                + Constant.COMMUNITY_FOLDER + Constant.SPEARMAN_FOLDER + "id"));

//        将id文件转换成name文件
//        Utils.convertCommunityIdFilesToNameFiles(new File(Constant.RESULT_FOLDER + Constant.SH_FOLDER
//                + Constant.COMMUNITY_FOLDER + Constant.SPEARMAN_FOLDER + "id"));

        //        输出网络的节点数和边数
        for (double threshold = 0.05; threshold <= 1; threshold += 0.05) {
            spearmanRelation.updateGraph(threshold);
            spearmanRelation.outputVertexesAndEdges();
        }
    }
}
