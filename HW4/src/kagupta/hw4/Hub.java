package kagupta.hw4;

import algs.hw4.map.Information;
import algs.hw4.map.FlightMap;
import algs.hw4.map.FilterAirport;

public class Hub {
    public static void main(String[] args) {
        FilterAirport justLower48 = new FilterLower48();

        Information delta = FlightMap.undirectedGraphFromResources("delta.json", justLower48);
        Information southwest = FlightMap.undirectedGraphFromResources("southwest.json", justLower48);

        System.out.println("DELTA");
        for (int i = 0; i < delta.labels.size(); i++) {
            if (delta.graph.degree(i) > 75) {
                System.out.println(delta.labels.get(i) + "      " + delta.graph.degree(i));
            }
        }

        System.out.println("");
        System.out.println("SOUTHWEST");
        for (int i = 0; i < southwest.labels.size(); i++) {
            if (southwest.graph.degree(i) > 75) {
                System.out.println(southwest.labels.get(i) + "      " + southwest.graph.degree(i));
            }
        }

    }
}