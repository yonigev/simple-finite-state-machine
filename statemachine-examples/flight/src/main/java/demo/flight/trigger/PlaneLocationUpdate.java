package demo.flight.trigger;

import demo.flight.model.Location;
import demo.flight.FlightStateMachineConfiguration.FlightTrigger;
import lombok.Getter;
import statemachine.trigger.Trigger;

public class PlaneLocationUpdate implements Trigger<FlightTrigger> {
    FlightTrigger triggerId = FlightTrigger.LOCATION_UPDATE;

    @Getter
    Location updatedLocation;

    public PlaneLocationUpdate(Location location) {
        this.updatedLocation = new Location(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    @Override
    public FlightTrigger getTriggerId() {
        return triggerId;
    }
}
