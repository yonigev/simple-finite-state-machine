package io.github.yonigev.sfsm.demo.flight.trigger;

import io.github.yonigev.sfsm.demo.flight.model.Location;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightTrigger;
import lombok.Getter;
import io.github.yonigev.sfsm.trigger.Trigger;

public class PlaneLocationUpdate implements Trigger<FlightTrigger> {
    FlightTrigger triggerId = FlightTrigger.LOCATION_UPDATE;

    @Getter
    Location updatedLocation;

    public PlaneLocationUpdate(Location location) {
        this.updatedLocation = new Location(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    @Override
    public FlightTrigger getId() {
        return triggerId;
    }
}
