package io.github.yonigev.sfsm.demo.flight;

import io.github.yonigev.sfsm.demo.flight.model.FlightInformation;
import io.github.yonigev.sfsm.demo.flight.model.Location;
import io.github.yonigev.sfsm.demo.flight.trigger.PassengerUpdateTrigger;
import io.github.yonigev.sfsm.demo.flight.trigger.PlaneLocationUpdate;
import io.github.yonigev.sfsm.StateMachine;
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory;
import io.github.yonigev.sfsm.trigger.Trigger;

public class FlightStateMachineApplication {
    public static final String FLIGHT_INFO = "FLIGHT_INFO";

    public static void main(String[] args) {
        FlightInformation flight = mockFlightInfo();
        FlightStateMachineDefiner flightStateMachineDefiner = new FlightStateMachineDefiner();
        DefaultStateMachineFactory<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> factory = new FlightStateMachineFactory(flight);
        StateMachine<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> sm = factory.createStarted("TEST_SM", flightStateMachineDefiner.getDefinition());

        PassengerUpdateTrigger additionalFortyPassengers = new PassengerUpdateTrigger(40);
        PlaneLocationUpdate planeLocation = locationAwayFromGate(flight);

        // Test the boarding updates
        sm.trigger(additionalFortyPassengers);
        sm.trigger(additionalFortyPassengers);
        sm.trigger(additionalFortyPassengers);
        // All 120 passengers should be boarded by now.

        // Trigger a LEAVE_GATE event.
        sm.trigger(Trigger.Companion.ofId(FlightStateMachineDefiner.FlightTrigger.LEAVE_GATE));
        // Sends a location ping that's away from the gate
        sm.trigger(planeLocation);
        // Allow plane departure
        sm.trigger(Trigger.Companion.ofId(FlightStateMachineDefiner.FlightTrigger.DEPARTURE_CLEARED));

        // Send plane location updates indicating it is climbing
        while (planeLocation.getUpdatedLocation().getAltitude() < 10000) {
            sm.trigger(planeLocation);
            addAltitude(planeLocation, 2000);
        }

        // Plane altitude should be cruising at altitude ~10,000 - 12,000 meters
        sm.trigger(planeLocation);

        // Start descent
        while (planeLocation.getUpdatedLocation().getAltitude() > 400) {
            sm.trigger(planeLocation);
            addAltitude(planeLocation, -100);
        }
        // Below 400 the Flight State machine should move to the LANDING state
        sm.trigger(planeLocation);

        // Send a location update indicating the plane has arrived at its destination
        sm.trigger( new PlaneLocationUpdate(flight.getDestination()));
    }

    static FlightInformation mockFlightInfo() {
        Location source = new Location(-37.004644, 174.785943, 0);
        Location destination = new Location(64.131437, -21.945257, 0);
        int passengers = 120;
        return new FlightInformation(source, destination, passengers);
    }

    static PlaneLocationUpdate locationAwayFromGate(FlightInformation flight) {
        Location location = new Location(flight.getSource().getLatitude(),
                flight.getSource().getLongitude(),
                flight.getSource().getAltitude());

        location.setLatitude(location.getLatitude() + 1);
        location.setLongitude(location.getLongitude() + 1);
        return new PlaneLocationUpdate(location);
    }

    static void addAltitude(PlaneLocationUpdate planeLocationUpdate, int toAdd) {
        Location location = planeLocationUpdate.getUpdatedLocation();
        location.setAltitude(location.getAltitude() + toAdd);
    }
}
