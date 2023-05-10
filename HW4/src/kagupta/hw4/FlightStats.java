package kagupta.hw4;

import algs.hw4.map.Information;
import algs.hw4.map.FlightMap;
import algs.hw4.map.FilterAirport;


public class FlightStats {


    public static void main(String[] args) {
        FilterAirport justLower48 = new FilterLower48();

        Information delta = FlightMap.undirectedGraphFromResources("delta.json", justLower48);
        Information southwest = FlightMap.undirectedGraphFromResources("southwest.json", justLower48);


        Histogram deltaHistogram = new Histogram();
        Histogram southwestHistogram = new Histogram();


        double currDistance = 0;

        double longestDist = 0;
        int longestV = -1;
        int longestAdj = -1;

        double shortestDist = Integer.MAX_VALUE;
        int shortestV = -1;
        int shortestAdj = -1;

        double count = 0;
        double total = 0;


        for (int i = 0; i < delta.graph.V(); i++) {
            for (int edges : delta.graph.adj(i)) {
                if (i > edges) {

                    currDistance = delta.positions.get(i).distance(delta.positions.get(edges));

                    total += (int) currDistance;
                    count++;
                    deltaHistogram.record((int) currDistance);

                    if (currDistance > longestDist) {
                        longestDist = currDistance;
                        longestV = i;
                        longestAdj = edges;
                    }

                    if (currDistance < shortestDist) {
                        shortestDist = currDistance;
                        shortestV = i;
                        shortestAdj = edges;
                    }
                }
            }
        }

        double average = total / count;

        System.out.println("Shortest flight for Delta is from" + " " + delta.labels.get(shortestV) + " " + "to" + " " + delta.labels.get(shortestAdj) + " " + (int) shortestDist + " " + "miles");
        System.out.println("Longest flight for Delta is from" + " " + delta.labels.get(longestV) + " " + "to" + " " + delta.labels.get(longestAdj) + " " + (int) longestDist + " " + "miles");
        System.out.println("Average Delta flight distance = " + " " + average);

        // reset count and total for southwest airlines
        count = 0;
        total = 0;
        longestDist = 0;
        shortestDist = Integer.MAX_VALUE;


        for (int i = 0; i < southwest.graph.V(); i++) {
            for (int edges : southwest.graph.adj(i)) {
                if (i > edges) {
                    currDistance = southwest.positions.get(i).distance(southwest.positions.get(edges));

                    total += (int) currDistance;
                    count++;
                    southwestHistogram.record((int) currDistance);

                    if (currDistance > longestDist) {
                        longestDist = currDistance;
                        longestV = i;
                        longestAdj = edges;
                    }

                    if (currDistance < shortestDist) {
                        shortestDist = currDistance;
                        shortestV = i;
                        shortestAdj = edges;
                    }
                }
            }
        }

        average = total / count;

        System.out.println();
        System.out.println("Shortest flight for Southwest is from" + " " + southwest.labels.get(shortestV) + " " + "to" + " " + southwest.labels.get(shortestAdj) + " " + (int) shortestDist + " " + "miles");
        System.out.println("Longest flight for Southwest is from" + " " + southwest.labels.get(longestV) + " " + "to" + " " + southwest.labels.get(longestAdj) + " " + (int) longestDist + " " + "miles");
        System.out.println("Average Delta flight distance = " + " " + average);


        System.out.println();

        System.out.println("Delta Airlines:");
        deltaHistogram.report(500);

        System.out.println();
        System.out.println("Southwest Airlines:");
        southwestHistogram.report(500);

    }
}

