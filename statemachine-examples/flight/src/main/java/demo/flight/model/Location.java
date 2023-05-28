package demo.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    double latitude;
    double longitude;
    double altitude;
}
