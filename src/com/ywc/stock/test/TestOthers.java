package com.ywc.stock.test;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.geometry.spherical.twod.Edge;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.GraphTests;
import org.jgrapht.alg.flow.PushRelabelMFImpl.VertexExtension;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import com.ywc.stock.util.Constant;
import com.ywc.stock.util.Utils;

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

}
