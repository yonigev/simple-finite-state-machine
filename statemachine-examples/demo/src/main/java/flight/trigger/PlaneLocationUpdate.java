package flight.trigger;

import flight.FlightStateMachineConfiguration.FlightTrigger;
import flight.model.Location;
import lombok.Getter;
import statemachine.trigger.Trigger;

public class PlaneLocationUpdate implements Trigger<FlightTrigger> {
    FlightTrigger triggerId = FlightTrigger.LOCATION_UPDATE;

    @Getter
    Location updatedLocation;

    public PlaneLocationUpdate(Location location) {
        this.updatedLocation = location;
    }

    @Override
    public FlightTrigger getTriggerId() {
        return triggerId;
    }
}
