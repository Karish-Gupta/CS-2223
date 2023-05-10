package kagupta.hw4;

import algs.hw4.map.FilterAirport;
import algs.hw4.map.FlightMap;
import algs.hw4.map.GPS;
import algs.hw4.map.Information;
import edu.princeton.cs.algs4.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.FloydWarshall;
import edu.princeton.cs.algs4.Graph;

public class LongestOfShortest {


    public static void main(String[] args) {
        FilterAirport justLower48 = new FilterLower48();

        Information delta = FlightMap.undirectedGraphFromResources("delta.json", justLower48);


        Information southwest = FlightMap.undirectedGraphFromResources("southwest.json", justLower48);


        AdjMatrixEdgeWeightedDigraph weightedGraphDelta = new AdjMatrixEdgeWeightedDigraph(delta.graph.V());
        AdjMatrixEdgeWeightedDigraph weightedGraphSouthwest = new AdjMatrixEdgeWeightedDigraph(southwest.graph.V());


        for (int i = 0; i < delta.graph.V(); i++) {
            for (int edge : delta.graph.adj(i)) {
                double distanceWeight = delta.positions.get(i).distance(delta.positions.get(edge));
                weightedGraphDelta.addEdge( new DirectedEdge(i, edge, distanceWeight));
            }
        }

        for (int i = 0; i < southwest.graph.V(); i++) {
            for (int edge : southwest.graph.adj(i)) {
                double distanceWeight = southwest.positions.get(i).distance(southwest.positions.get(edge));
                weightedGraphSouthwest.addEdge( new DirectedEdge(i, edge, distanceWeight));
            }
        }

        // Initialize FloydWarshall
        FloydWarshall allPairsDelta = new FloydWarshall(weightedGraphDelta);
        FloydWarshall allPairsSouthwest = new FloydWarshall(weightedGraphSouthwest);

        // delta
        double greatestLeastDistance = 0;
        double currDistance = 0;
        double longestDistance = 0;

        DirectedEdge greatestEdge = new DirectedEdge(0, 0, 0);

        int numPaths = 0;
        double currRatio = 0;

        for (int i = 0; i < weightedGraphDelta.V(); i++) {
            for (int j = 0; j < weightedGraphDelta.V(); j++) {
                // Ensure that the vertices are not the same and that a path between them exists
                if (i != j && allPairsDelta.hasPath(i, j)) {

                    currDistance = allPairsDelta.dist(i, j);

                    if (currDistance > greatestLeastDistance) {
                        greatestEdge = new DirectedEdge(i, j, currDistance);
                        greatestLeastDistance = currDistance;
                        longestDistance = delta.positions.get(greatestEdge.from()).distance(delta.positions.get(greatestEdge.to()));

                    }
                    // Ratio calculations
                    numPaths++;
                    currRatio +=  allPairsDelta.dist(i, j) / (delta.positions.get(i).distance(delta.positions.get(j)));


                }
            }
        }


        // Final report for delta
        System.out.println("Delta:");
        System.out.println("Test : Total Flight Distance is " + greatestLeastDistance + " but airports are only");
        System.out.println(longestDistance + "miles apart.");
        for (DirectedEdge edge : allPairsDelta.path(greatestEdge.from(), greatestEdge.to())) {
            System.out.println(delta.labels.get(edge.from()) + " -> " + delta.labels.get(edge.to()) + " for " + delta.positions.get(edge.from()).distance(delta.positions.get(edge.to())));
        }
        System.out.println("Average Efficiency: " + currRatio/numPaths);


        // southwest
        greatestLeastDistance = 0;
        currDistance = 0;
        longestDistance = 0;

        DirectedEdge greatestEdge2 = new DirectedEdge(0, 0, 0);

        numPaths = 0;
        currRatio = 0;

        for (int i = 0; i < weightedGraphSouthwest.V(); i++) {
            for (int j = 0; j < weightedGraphSouthwest.V(); j++) {
                // Ensure that the vertices are not the same and that a path between them exists
                if (i != j && allPairsSouthwest.hasPath(i, j)) {

                    currDistance = allPairsSouthwest.dist(i, j);

                    if (currDistance > greatestLeastDistance) {
                        greatestEdge2 = new DirectedEdge(i, j, currDistance);
                        greatestLeastDistance = currDistance;
                        longestDistance = southwest.positions.get(greatestEdge2.from()).distance(southwest.positions.get(greatestEdge2.to()));

                    }
                    // Ratio calculations
                    numPaths++;
                    currRatio +=  allPairsSouthwest.dist(i, j) / (southwest.positions.get(i).distance(southwest.positions.get(j)));


                }
            }
        }

        // Final report for southwest
        System.out.println();
        System.out.println("Southwest:");
        System.out.println("Test : Total Flight Distance is " + greatestLeastDistance + " but airports are only");
        System.out.println(longestDistance + "miles apart.");
        for (DirectedEdge edge : allPairsSouthwest.path(greatestEdge2.from(), greatestEdge2.to())) {
            System.out.println(southwest.labels.get(edge.from()) + " -> " + southwest.labels.get(edge.to()) + " for " + southwest.positions.get(edge.from()).distance(southwest.positions.get(edge.to())));
        }
        System.out.println("Average Efficiency: " + currRatio/numPaths);


    }
}