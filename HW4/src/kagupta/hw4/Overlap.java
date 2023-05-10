package kagupta.hw4;

import algs.hw4.map.Information;
import algs.hw4.map.FlightMap;
import algs.hw4.map.FilterAirport;

public class Overlap {
    public static void main(String[] args) {
        FilterAirport justLower48 = new FilterLower48();

        Information delta = FlightMap.undirectedGraphFromResources("delta.json", justLower48);
        Information southwest = FlightMap.undirectedGraphFromResources("southwest.json", justLower48);


        int count = 0;

        for (int i = 0; i < southwest.labels.size(); i++) {
            count = 0;

            for (int j = 0; j < delta.labels.size(); j++) {
                count++;

                if (southwest.labels.get(i).equals(delta.labels.get(j))) {
                    break;
                }
                else if (count == delta.labels.size()) {
                    System.out.println(southwest.labels.get(i));
                }

            }


        }
    }
}
