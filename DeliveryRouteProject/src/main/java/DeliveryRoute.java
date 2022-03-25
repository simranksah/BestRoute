import java.util.List;

public interface DeliveryRoute {

  /**
   * Method to create adj matrix from different geo-locations
   *
   * @param geoLocationList
   * @param n
   */
  int[][] createAdjMatrix(int n, List<GeoLocation> geoLocationList);

  /**
   * Impl of dijkstra algorithm
   *
   * @param adjMatrix
   * @param src
   */
  void dijkstra(int[][] adjMatrix, int src);

}
