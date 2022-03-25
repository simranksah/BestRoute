import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRouteImpl implements DeliveryRoute {

  private static int V = 3;
  private final int avgSpeed = 20;

  @Override
  public int[][] createAdjMatrix(int n, List<GeoLocation> geoLocationList) {
    int[][] adjMatrix = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        //as start point can't directly connect to customer's geo-location
        if ((i == 0 && j % 2 == 0) || (j == 0 && i % 2 == 0)) {
          continue;
        }
        adjMatrix[i][j] = haversine(geoLocationList.get(i).lat, geoLocationList.get(i).lon, geoLocationList.get(j).lat,
            geoLocationList.get(j).lon);
      }
    }
    return adjMatrix;
  }

  private static int haversine(double lat1, double lon1, double lat2, double lon2) {
    // distance between latitudes and longitudes
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    // convert to radians
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // apply formulae
    double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    double rad = 6371;
    double c = 2 * Math.asin(Math.sqrt(a));
    return (int) (rad * c);
  }

  @Override
  public void dijkstra(int[][] graph, int src) {
    int[] dist = new int[V]; // The output array. dist[i] will hold
    // the shortest distance from src to i

    // traverseCheck[i] will true if vertex i is included in shortest
    // path tree or shortest distance from src to i is finalized
    Boolean[] traverseCheck = new Boolean[V];
    List<Integer> traverseOrder = new ArrayList<>();

    // Initialize all distances as INFINITE and stpSet[] as false
    for (int i = 0; i < V; i++) {
      dist[i] = Integer.MAX_VALUE;
      traverseCheck[i] = false;
    }

    // Distance of source vertex from itself is always 0
    dist[src] = 0;

    // Find shortest path for all vertices
    for (int count = 0; count < V; count++) {
      // Pick the minimum distance vertex from the set of vertices
      // not yet processed. u is always equal to src in first
      // iteration.
      int u = minDistance(dist, traverseCheck);

      // Mark the picked vertex as processed
      traverseCheck[u] = true;
      traverseOrder.add(u);

      // Update dist value of the adjacent vertices of the picked vertex.
      for (int v = 0; v < V; v++) {

        // Update dist[v] only if is not in sptSet, there is an
        // edge from u to v, and total weight of path from src to
        // v through u is smaller than current value of dist[v]
        if (!traverseCheck[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
          //it should not traverse customer before the respective restaurant
          if ((v != 0 && v % 2 == 0 && traverseCheck[v - 1]) || v % 2 == 1) {
            dist[v] = dist[u] + graph[u][v];
          }
        }
      }

    }

    System.out.println("\ntraverseOrder : " + traverseOrder);
    printFinalSolution(graph, traverseOrder);
  }

  int minDistance(int[] dist, Boolean[] sptSet) {
    int min = Integer.MAX_VALUE, min_index = -1;

    for (int v = 0; v < V; v++)
      if (!sptSet[v] && dist[v] <= min) {
        min = dist[v];
        min_index = v;
      }

    return min_index;
  }

  void printFinalSolution(int[][] graph, List<Integer> traverseOrder) {
    int sum = 0;
    for (int i = 0; i < traverseOrder.size() - 1; i++) {
      sum = sum + graph[traverseOrder.get(i)][traverseOrder.get(i + 1)];
    }
    System.out.println("\nShortest distance in km. : " + sum);
    System.out.println("Shortest possible time in hours : " + sum / avgSpeed);
  }

  private static void printAdjMatrix(int n, int[][] adjMatrix) {
    System.out.println("\nAdjacency matrix of distance between locations :");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.print(adjMatrix[i][j] + " ");
      }
      System.out.println();
    }
  }

  private static int getInputFromUser(List<GeoLocation> geoLocationList) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter total orders to be delivered : ");
    int orderCount = Integer.parseInt(br.readLine().trim());
    int n = 2 * orderCount + 1;

    for (int i = 0; i < n; i++) {
      GeoLocation node;
      if (i == 0) {
        System.out.println("Enter the start geo-location : ");
        String[] inputLine = br.readLine().trim().split(" ");
        node = new GeoLocation(LocationType.START, Double.parseDouble(inputLine[0]), Double.parseDouble(inputLine[1]));
      } else if (i % 2 == 1) {
        System.out.println("Enter the restaurant " + ((i / 2) + 1) + " geo-location : ");
        String[] inputLine = br.readLine().trim().split(" ");
        node = new GeoLocation(LocationType.RESTAURANT, Double.parseDouble(inputLine[0]),
            Double.parseDouble(inputLine[1]));
      } else {
        System.out.println("Enter the customer " + (i / 2) + " geo-location : ");
        String[] inputLine = br.readLine().trim().split(" ");
        node =
            new GeoLocation(LocationType.CUSTOMER, Double.parseDouble(inputLine[0]), Double.parseDouble(inputLine[1]));
      }
      geoLocationList.add(node);
    }
    V = n;
    return n;
  }

  public static void main(String[] args) throws IOException {
    DeliveryRouteImpl deliveryRoute = new DeliveryRouteImpl();
    List<GeoLocation> geoLocationList = new ArrayList<>();
    int n = getInputFromUser(geoLocationList);
    int[][] adjMatrix = deliveryRoute.createAdjMatrix(n, geoLocationList);
    printAdjMatrix(n, adjMatrix);
    deliveryRoute.dijkstra(adjMatrix, 0);
  }

}
