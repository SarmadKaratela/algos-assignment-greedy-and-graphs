/**
 * Public Transit
 * Author: Sarmad Karatela and Carolyn Yao
 * Does this compile? Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest pathStations algorithm. As it is, you can run this class and get the solutions
 * from the existing shortest pathStation algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S
   * to a station T given various tables of information about each edge (u,v)
   *
   * @param S the s th vertex/station in the transit map, start From
   * @param T the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths lengths[u][v] The time it takes for a train to get between two adjacent stations u and v
   * @param first first[u][v] The time of the first train that stops at u on its way to v, int in minutes from 5:30am
   * @param freq freq[u][v] How frequently is the train that stops at u on its way to v
   * @return shortest travel time between S and T
   */
  public int myShortestTravelTime(
    int S,
    int T,
    int startTime,
    int[][] lengths,
    int[][] first,
    int[][] freq
  ) {
    int pathStation[] = shortestDijiks(lengths, S, T); //finds the shortest pathStation from starting station to destination
    int index= first[0].length-1;
     //back track to find the source
    while(index>0) {
      if (pathStation[index]== S){
        break;
      }else{
      index--;
      }
    }

    int shortestTime=0; //keeps track of the shortest time it takes to get from starting station to destination station
    int nextTrainTime;  //finds the nextTrainTime plus the time it takes for the train to travel that edge
    int currentTime=startTime;

    for(int i=index; i>=1; i--) {
      int stationNum = 0;
      int currentStation = pathStation[i];
      int arrivingStation = pathStation[i-1];
      int trainTimes= first[currentStation][arrivingStation];
      
        while (trainTimes < currentTime) {
          trainTimes = first[currentStation][arrivingStation] + (stationNum * freq[currentStation][arrivingStation]);
          stationNum++;
      }

      nextTrainTime = trainTimes + lengths[currentStation][arrivingStation];
      shortestTime = shortestTime + (nextTrainTime - currentTime);
      currentTime=nextTrainTime;
    }
    return shortestTime;
  }

  // Modified Dijkstra's shortest pathStation from https://www.geeksforgeeks.org/dijkstras-shortest pathStation-algorithm-greedy-algo-7/
  // needs to return shortest pathStation to target instead of all shortest pathStation.

  public int[] shortestDijiks(int[][] graph, int src, int target) {
    int previousNode = graph[0].length;    // number of vertices
    Boolean currentNode[] = new Boolean[previousNode]; // currentNode[i] will true if vertex i is included in shortest
    int[] timeTo = new int[previousNode];   // shortest time from src to i
    int[] prevStation = new int[previousNode];    // stations visited
    int[] pathStation = new int[previousNode]; //path from each station
    prevStation[src] = -1;    // Sets the previous station of source to -1 since there isnt a previous station from starting point.


    for (int i = 0; i < previousNode; i++)
    {
      timeTo[i] = Integer.MAX_VALUE;
     pathStation[i] = -1;
      currentNode[i] = false;
    }
    // Time of source vertex from itself is always 0
    timeTo[src] = 0;

    for (int count = 0; count < previousNode - 1; count++) { 		// Find shortest pathStation to all the stations
      
      int u = findNextToProcess(timeTo, currentNode);
      currentNode[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < previousNode; v++) { //iterate through stations
        
        if (!currentNode[v] && graph[u][v]!=0 && timeTo[u] != Integer.MAX_VALUE && timeTo[u]+graph[u][v] < timeTo[v]) {
          timeTo[v] = timeTo[u] + graph[u][v];
          prevStation[v] = u; 
        }
      }
    }
    //Finds the shortest time between T and source.
    int i = 0; //starting station
    int t = target; // target station
   // Using the modified Dijkstra's algorithm, to find the shortest path to find to set the array we are using the shortest path that
            // the shortest path to the station from the starting station
           
    while (t != src) {
      
     pathStation[i++] = t;
      t = prevStation[t];
    }
   pathStation[i] = t;
    return pathStation;
  }

  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * @param timeTo The shortest timeTo from the source
   * @param processed boolean array tells you which vertices have been fully processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] timeTo, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < timeTo.length; i++) {
      if (processed[i] == false && timeTo[i] <= min) {
        min = timeTo[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int timeTo[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < timeTo.length; i++)
        System.out.println(i + ": " + timeTo[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * @param graph The connected, directed graph in an adjacency matrix where
   *              if graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest timeTo
    int[] timeTo = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      timeTo[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    timeTo[source] = 0;

    // Find shortest pathStation to all the vertices
    for (int count = 0; count < numVertices - 1 ; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(timeTo, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of pathStation from source to v through u is smaller than current value of time[v]
        if (!processed[v] && graph[u][v]!=0 && timeTo[u] != Integer.MAX_VALUE && timeTo[u]+graph[u][v] < timeTo[v]) {
          timeTo[v] = timeTo[u] + graph[u][v];
        }
      }
    }
    printShortestTimes(timeTo);
  }

  public static void main (String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][]{
      {0, 4, 0, 0, 0, 0, 0, 8, 0},
      {4, 0, 8, 0, 0, 0, 0, 11, 0},
      {0, 8, 0, 7, 0, 4, 0, 0, 2},
      {0, 0, 7, 0, 9, 14, 0, 0, 0},
      {0, 0, 0, 9, 0, 10, 0, 0, 0},
      {0, 0, 4, 14, 10, 0, 2, 0, 0},
      {0, 0, 0, 0, 0, 2, 0, 1, 6},
      {8, 11, 0, 0, 0, 0, 1, 0, 7},
      {0, 0, 2, 0, 0, 0, 6, 7, 0}
    };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    t.shortestTime(lengthTimeGraph, 0);

    // You can create a test case for your implemented method for extra credit below
   
    System.out.println("Test Case");

    int length[][] = new int[][] {
         
            { 1,  0,  5,  0,  0,  3,  0,  5, 0 },  
            { 0,  0,  7,  0,  10, 0,  0,  0,  0 },  
            { 6,  8,  0,  7,  1,  4,  0,  0,  6 }, 
            { 0,  0,  7,  0,  12, 4,  0,  2,  0 },  
            { 0,  13, 0,  10, 0,  12,  7,  0,  0 }, 
            { 2,  0,  4,  4,  0,  0,  5,  0,  0 },  
            { 0,  0,  1,  1,  0,  6,  3,  5,  2 },  
            { 9, 0,  0,  2,  0,  0,  5,  0,  0 },  
            { 0,  0,  8,  0,  0,  0,  2,  0,  0 } };

    int first[][] = new int[][] {
         
            { 0,  0,  9,  0,  0,  8,  0,  15, 0 },  
            { 0,  0,  2,  0,  22, 0,  0,  0,  0 },  
            { 2,  8,  0,  7,  0,  4,  0,  0,  6 },  
            { 0,  0,  7,  0,  1, 4,  0,  6,  0 },  
            { 0,  13, 0,  19, 0,  10,  1,  8,  0 },  
            { 8,  0,  4,  9,  10,  0,  8,  0,  0 },  
            { 0,  3,  0,  9,  0,  4,  10,  9,  6 },  
            { 7, 0,  0,  14,  3,  0,  4, 0,  0 },  
            { 0,  0,  4,  0,  0,  0,  9,  0,  0 } };

    int freq[][] = new int[][] {
        
            { 3,  0, 3,  1,  0,  4,  1,  5,  0 }, 
            { 5,  0,  0,  0,  12,  0,  1,  1,  0 },  
            { 5,  6,  0,  3,  3,  3,  0,  1,  6 }, 
            { 0,  0,  7,  0,  10, 4,  0,  2,  0 }, 
            { 0,  8,  0,  5, 0,  0,  10,  12,  1 }, 
            { 3,  0,  4,  6,  1,  0,  5,  0,  0 },  
            { 0,  0,  1,  0,  0,  0,  3,  8,  6 },  
            { 5,  0,  0,  2,  0,  0,  8,  0,  0 }, 
            { 0,  0,  8,  0,  1,  3,  12, 1,  1 } };


    int shortestTime;
    int startStation = 3;
    int targetStation = 8;

    shortestTime = t.myShortestTravelTime(startStation, targetStation, 7, length, first, freq);
    System.out.println( "Shortest time from station " + startStation + " to " + "station " +targetStation + " : " + shortestTime + " minutes.");
  }
}
