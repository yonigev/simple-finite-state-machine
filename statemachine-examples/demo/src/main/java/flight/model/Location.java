package flight.model;

import lombok.Value;

@Value
public class Location {
    double latitude;
    double longitude;
    double altitude;
}
