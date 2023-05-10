package kagupta.hw4;

import algs.days.day20.DepthFirstPaths;
import algs.hw4.map.Information;
import algs.hw4.map.FlightMap;
import algs.hw4.map.FilterAirport;

public class Connected {
    public static void main(String[] args) {
        FilterAirport justLower48 = new FilterLower48();

        Information delta = FlightMap.undirectedGraphFromResources("delta.json", justLower48);
        Information southwest = FlightMap.undirectedGraphFromResources("southwest.json", justLower48);

        int deltaBoston= 0;
        for (int airportNum = 0; airportNum < delta.labels.size(); airportNum++) {
            if (delta.labels.get(airportNum).equals("KBOS")) {
                deltaBoston = airportNum;
            }
        }

        int southwestBoston = 0;
        for (int airportNum = 0; airportNum < delta.labels.size(); airportNum++) {
            if (delta.labels.get(airportNum).equals("KBOS")) {
                deltaBoston = airportNum;
            }
        }


        DepthFirstPaths deltaPath = new DepthFirstPaths(delta.graph, deltaBoston);
        DepthFirstPaths southwestPath = new DepthFirstPaths(southwest.graph, southwestBoston);



        int counterSouthwest = 0;

        for (int i = 0; i < southwest.labels.size(); i++) {
            if(!southwestPath.hasPathTo(i)) {
                counterSouthwest++;

                if(counterSouthwest == 1) {
                    System.out.println("The name of the airline is southwest");
                    System.out.println("The airports that cannot be reached from KBOS using southwest are:");
                }

                System.out.println(southwest.labels.get(i));
            }
        }


        int counterDelta = 0;

        for (int i = 0; i < delta.labels.size(); i++) {
            if(!deltaPath.hasPathTo(i)) {
                counterDelta++;

                if(counterDelta == 1) {
                    System.out.println("The name of the airline is delta");
                    System.out.println("The airports that cannot be reached from KBOS using delta are:");
                }

                System.out.println(delta.labels.get(i));
            }
        }
    }
}

