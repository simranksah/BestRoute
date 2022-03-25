import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainApplication {

  public static void main(String[] args) throws IOException {
    List<GeoLocation> geoLocationList = new ArrayList<>();
    int n = getInputFromUser(geoLocationList);
    DeliveryRoute deliveryRoute = new DeliveryRouteImpl(n);
    int[][] adjMatrix = deliveryRoute.createAdjMatrix(n, geoLocationList);
    deliveryRoute.printAdjMatrix(n, adjMatrix);
    deliveryRoute.dijkstra(adjMatrix, 0);
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
    return n;
  }
}
