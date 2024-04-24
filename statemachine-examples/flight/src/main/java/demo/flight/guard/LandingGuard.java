package demo.flight.guard;

import demo.flight.FlightStateMachineApplication;
import demo.flight.FlightStateMachineDefinition;
import demo.flight.model.FlightInformation;
import demo.flight.trigger.PlaneLocationUpdate;
import org.jetbrains.annotations.NotNull;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

import java.util.Objects;

public class LandingGuard implements Guard<FlightStateMachineDefinition.FlightState, FlightStateMachineDefinition.FlightTrigger> {
    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineDefinition.FlightState, FlightStateMachineDefinition.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());
        // Get the current extended flight state
        FlightInformation flightInfo = (FlightInformation) transitionContext.getStateMachineContext().getProperty(FlightStateMachineApplication.FLIGHT_INFO);
        PlaneLocationUpdate planeLocationUpdate = (PlaneLocationUpdate) transitionContext.getTrigger();
        // Update the flight info
        flightInfo.updatePlaneLocation(planeLocationUpdate.getUpdatedLocation());
        double planeAltitude = flightInfo.getPlaneLocation().getAltitude();
        // Obviously not a real-life example, but will suffice for the sake of the demo.
        return planeAltitude <= 400;
    }
}
