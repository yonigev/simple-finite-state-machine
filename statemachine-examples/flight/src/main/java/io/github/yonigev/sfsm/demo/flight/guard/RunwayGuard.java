package io.github.yonigev.sfsm.demo.flight.guard;

import io.github.yonigev.sfsm.demo.flight.FlightStateMachineApplication;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner;
import io.github.yonigev.sfsm.demo.flight.model.FlightInformation;
import io.github.yonigev.sfsm.demo.flight.trigger.PlaneLocationUpdate;
import org.jetbrains.annotations.NotNull;
import io.github.yonigev.sfsm.guard.Guard;
import io.github.yonigev.sfsm.transition.TransitionContext;

import java.util.Objects;

public class RunwayGuard implements Guard<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> {
    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());
        // Get the current extended flight state
        FlightInformation flightInfo = (FlightInformation) transitionContext.getStateMachineContext().getProperty(FlightStateMachineApplication.FLIGHT_INFO);
        PlaneLocationUpdate planeLocationUpdate = (PlaneLocationUpdate) transitionContext.getTrigger();
        // Update the flight info
        flightInfo.updatePlaneLocation(planeLocationUpdate.getUpdatedLocation());
        // Obviously not a real-life example, but will suffice for the sake of the demo.
        return flightInfo.getPlaneLocation().getLatitude() != flightInfo.getSource().getLatitude()
                 && flightInfo.getPlaneLocation().getLongitude() != flightInfo.getSource().getLongitude();
    }
}
