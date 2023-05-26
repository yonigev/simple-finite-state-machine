package flight.guard;

import flight.FlightStateMachineConfiguration.FlightState;
import flight.FlightStateMachineConfiguration.FlightTrigger;
import flight.model.FlightInformation;
import flight.trigger.PlaneLocationUpdate;
import org.jetbrains.annotations.NotNull;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

import java.util.Objects;

import static flight.FlightStateMachineApplication.FLIGHT_INFO;

public class RunwayGuard implements Guard<FlightState, FlightTrigger> {
    @Override
    public boolean transition(@NotNull TransitionContext<FlightState, FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());
        // Get the current extended flight state
        FlightInformation flightInfo = (FlightInformation) transitionContext.getStateMachineContext().getProperty(FLIGHT_INFO);
        PlaneLocationUpdate planeLocationUpdate = (PlaneLocationUpdate) transitionContext.getTrigger();
        // Update the flight info
        flightInfo.updatePlaneLocation(planeLocationUpdate.getUpdatedLocation());
        // Obviously not a real-life example, but will suffice for the sake of the demo.
        return flightInfo.getPlaneLocation().getLatitude() != flightInfo.getSource().getLatitude()
                 && flightInfo.getPlaneLocation().getLongitude() != flightInfo.getSource().getLongitude();
    }
}
