package demo.flight.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FlightInformation {
    private final Location source;
    private final Location destination;
    private final int bookedPassengers;
    Location planeLocation;
    int currentPassengerCount;

    public void addPassengers(int add) {
        this.currentPassengerCount += add;
    }

    public void updatePlaneLocation(Location updated) {
        this.planeLocation = updated;
    }
}
