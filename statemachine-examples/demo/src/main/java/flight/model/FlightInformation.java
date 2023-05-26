package flight.model;

import lombok.Data;

@Data
public class FlightInformation {
    Location source;
    Location destination;
    Location planeLocation;
    int bookedPassengers;
    int currentPassengerCount;

    public void addPassengers(int add) {
        this.currentPassengerCount += add;
    }

    public void updatePlaneLocation(Location updated) {
        this.planeLocation = updated;
    }
}
