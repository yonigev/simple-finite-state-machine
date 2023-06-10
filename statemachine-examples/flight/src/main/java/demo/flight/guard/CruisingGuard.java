package demo.flight.guard;

import demo.flight.FlightStateMachineApplication;
import demo.flight.FlightStateMachineConfiguration;
import demo.flight.model.FlightInformation;
import demo.flight.trigger.PlaneLocationUpdate;
import org.jetbrains.annotations.NotNull;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

import java.util.Objects;

public class CruisingGuard implements Guard<FlightStateMachineConfiguration.FlightState, FlightStateMachineConfiguration.FlightTrigger> {
    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineConfiguration.FlightState, FlightStateMachineConfiguration.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());
        // Get the current extended flight state
        FlightInformation flightInfo = (FlightInformation) transitionContext.getStateMachineContext().getProperty(FlightStateMachineApplication.FLIGHT_INFO);
        PlaneLocationUpdate planeLocationUpdate = (PlaneLocationUpdate) transitionContext.getTrigger();
        // Update the flight info
        flightInfo.updatePlaneLocation(planeLocationUpdate.getUpdatedLocation());
        double planeAltitude = flightInfo.getPlaneLocation().getAltitude();
        // Obviously not a real-life example, but will suffice for the sake of the demo.
        return planeAltitude >= 10000 && planeAltitude <= 12000;
    }
}
