package kagupta.hw4;

import algs.hw4.map.*;

import javax.swing.*;

/** 
 * COPY this class TWICE into your USERID.hw4 package -- once as Delta.java and once as Southwest.java
 * 
 * In each of these respective classes, be sure to change the main file to load up either "delta.json"
 * or "southwest.json"
 * 
 * If you want to inspect the "test.json" data set, then you can create another Test file if you'd like.
 * 
 * Standalone JFrame in which airport flight information is visualized.
 */
public class Delta extends JFrame {

	public final Visualizer visualizer;

	public Delta(Information info) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1014, 800);
		setResizable(false);
		visualizer = new Visualizer(info);
		setContentPane(visualizer);
	}
	
	public static void main(String[] args) throws Exception {
		
		// Note: You will complete this filter implementation
		FilterAirport justLower48 = new FilterLower48();
		
		Information info = FlightMap.undirectedGraphFromResources("delta.json", justLower48);
		
		Delta ma = new Delta(info);
		ma.setVisible(true);
		
		System.out.println("Number of airports: " + info.graph.V() + " airports");
	}
}