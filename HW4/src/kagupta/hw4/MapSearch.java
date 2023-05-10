package kagupta.hw4;

import algs.hw4.map.GPS;
import algs.hw4.map.HighwayMap;
import algs.hw4.map.Information;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import algs.days.day20.DepthFirstSearchNonRecursive;
import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;


/**
 * Copy this class into USERID.hw4 and make changes.
 */
public class MapSearch {
	
	/** 
	 * This helper method returns the western-most vertex id in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int westernMostVertex(Information info) {
		int leastLong = Integer.MAX_VALUE;
		int westernMostID = 0;
		for (int i = 0; i < info.graph.V(); i++) {
			if (info.positions.get(i).longitude < info.positions.get(westernMostID).longitude) {
				westernMostID = i;
			}
		}
		return westernMostID;
	}

	/** 
	 * This helper method returns the eastern-most vertex id in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int easternMostVertex(Information info) {
		int greatestLong = Integer.MIN_VALUE;
		int easternMostID = 0;
		for (int i = 0; i < info.graph.V(); i++) {
			if (info.positions.get(i).longitude > info.positions.get(easternMostID).longitude) {
				easternMostID = i;
			}
		}
		return easternMostID;
	}

	public static void main(String[] args) {
		Information info = HighwayMap.undirectedGraphFromResources("USA-lower48-natl-traveled.tmg");
		int west = westernMostVertex(info);
		int east = easternMostVertex(info);



		// Calculate total distance between east and west shortest path in BFS
		BreadthFirstPaths BFS = new BreadthFirstPaths(info.graph, west);

		double totalDistance = 0;
		int last = -1;
		for (int v : BFS.pathTo(east)) {
			if (last == -1) { last = v;}

			totalDistance = totalDistance + info.positions.get(last).distance(info.positions.get(v));
			last = v;
		}

		// Calculate total distance between east and west path in DFS
		DepthFirstSearchNonRecursive DFS = new DepthFirstSearchNonRecursive(info.graph, west);

		double totalDistance2 = 0;
		int edges = 0;
		int last2 = -1;
		for (int v : DFS.pathTo(east)) {

			if (last2 == -1) {
				last2 = v;
			}
			else {
				totalDistance2 = totalDistance2 + info.positions.get(last2).distance(info.positions.get(v));
				last2 = v;
				edges++;
			}

		}

		//Calculate distance of the shortest path using Dijkstra's algorithm

		// Populate EdgeWeightedGraph with distances between vertices


		EdgeWeightedGraph EWG = new EdgeWeightedGraph(info.graph.V());


		for (int i = 0; i < info.graph.V(); i++) {
			for (int edge : info.graph.adj(i)) {
				double distanceWeight = info.positions.get(i).distance(info.positions.get(edge));
				EWG.addEdge( new Edge(i, edge, distanceWeight));
			}
		}

		DijkstraUndirectedSP Dijkstra = new DijkstraUndirectedSP(EWG, west);


		// Count number of edges in Dijkras algorithm
		int numEdges =0;
		for ( Edge edge : Dijkstra.pathTo(east)) {
			numEdges++;
		}




		// DO SOME WORK HERE and have the output include things like this
		System.out.println("BreadthFirst Search from West to East:");
		System.out.println("BFS: " + info.labels.get(west) + " to " + info.labels.get(east) + " has " + BFS.distTo(east) + " edges.");
		System.out.println("BFS provides answer that is : " + totalDistance + " miles.");

		System.out.println();
		System.out.println("DFS provides answer that is : " + totalDistance2 + " miles with " + edges + " total edges.");

		System.out.println();
		System.out.println("Shortest Distance via Dikstra: " + Dijkstra.distTo(east) +" miles with " + numEdges + " total edges.");
	}
}
