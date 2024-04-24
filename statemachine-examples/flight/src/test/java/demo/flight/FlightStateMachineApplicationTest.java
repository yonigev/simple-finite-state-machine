package demo.flight;

import demo.flight.model.FlightInformation;
import demo.flight.model.Location;
import demo.flight.trigger.PassengerUpdateTrigger;
import demo.flight.trigger.PlaneLocationUpdate;
import org.junit.jupiter.api.Test;
import statemachine.StateMachine;
import statemachine.factory.DefaultStateMachineFactory;
import demo.flight.FlightStateMachineDefinition.*;
import statemachine.trigger.Trigger;

import static demo.flight.FlightStateMachineDefinition.FlightState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightStateMachineApplicationTest {
    FlightInformation flight = mockFlightInfo();

    @Test
    public void testFlight() {
        FlightStateMachineDefinition flightStateMachineDefinition = new FlightStateMachineDefinition();
        DefaultStateMachineFactory<FlightState, FlightTrigger> factory = new FlightStateMachineFactory(flightStateMachineDefinition, flight);
        StateMachine<FlightState, FlightTrigger> sm = factory.createStarted("TEST_SM");

        PassengerUpdateTrigger additionalFortyPassengers = new PassengerUpdateTrigger(40);
        PlaneLocationUpdate planeLocation = locationAwayFromGate();

        // Test the boarding updates
        assertEquals(BOARDING, sm.getState().getId());
        sm.trigger(additionalFortyPassengers);
        assertEquals(BOARDING, sm.getState().getId());
        sm.trigger(additionalFortyPassengers);
        assertEquals(BOARDING, sm.getState().getId());
        sm.trigger(additionalFortyPassengers);
        // All 120 passengers should be boarded by now.
        assertEquals(BOARDING_COMPLETE, sm.getState().getId());

        // Trigger a LEAVE_GATE event.
        sm.trigger(Trigger.Companion.ofId(FlightTrigger.LEAVE_GATE));
        assertEquals(EN_ROUTE_TO_RUNWAY, sm.getState().getId());

        // Sends a location ping that's away from the gate
        sm.trigger(planeLocation);
        assertEquals(WAITING_AT_RUNWAY, sm.getState().getId());
        // Allow plane departure
        sm.trigger(Trigger.Companion.ofId(FlightTrigger.DEPARTURE_CLEARED));
        assertEquals(TAKING_OFF, sm.getState().getId());

        // Send plane location updates indicating it is climbing
        while (planeLocation.getUpdatedLocation().getAltitude() < 10000) {
            sm.trigger(planeLocation);
            assertEquals(TAKING_OFF, sm.getState().getId());
            addAltitude(planeLocation, 2000);
        }

        // Plane altitude should be cruising at altitude ~10,000 - 12,000 meters
        sm.trigger(planeLocation);
        assertEquals(CRUISING, sm.getState().getId());

        // Start descent
        while (planeLocation.getUpdatedLocation().getAltitude() > 400) {
            sm.trigger(planeLocation);
            assertEquals(CRUISING, sm.getState().getId());
            addAltitude(planeLocation, -100);
        }
        // Below 400 the Flight State machine should move to the LANDING state
        sm.trigger(planeLocation);
        assertEquals(LANDING, sm.getState().getId());

        // Send a location update indicating the plane has arrived at its destination
        sm.trigger( new PlaneLocationUpdate(flight.getDestination()));
        assertEquals(LANDED, sm.getState().getId());
    }

    private FlightInformation mockFlightInfo() {
        Location source = new Location(-37.004644, 174.785943, 0);
        Location destination = new Location(64.131437, -21.945257, 0);
        int passengers = 120;
        return new FlightInformation(source, destination, passengers);
    }

    private PlaneLocationUpdate locationAwayFromGate() {
        Location location = new Location(flight.getSource().getLatitude(),
                flight.getSource().getLongitude(),
                flight.getSource().getAltitude());

        location.setLatitude(location.getLatitude() + 1);
        location.setLongitude(location.getLongitude() + 1);
        return new PlaneLocationUpdate(location);
    }

    private void addAltitude(PlaneLocationUpdate planeLocationUpdate, int toAdd) {
        Location location = planeLocationUpdate.getUpdatedLocation();
        location.setAltitude(location.getAltitude() + toAdd);
    }
}
