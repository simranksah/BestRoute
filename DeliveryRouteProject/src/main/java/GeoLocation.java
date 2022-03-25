import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoLocation {
  LocationType locationType;
  double lat;
  double lon;
}