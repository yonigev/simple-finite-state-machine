package io.github.yonigev.sfsm.demo.flight.guard;

import io.github.yonigev.sfsm.demo.flight.FlightStateMachineApplication;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner;
import io.github.yonigev.sfsm.demo.flight.model.FlightInformation;
import io.github.yonigev.sfsm.demo.flight.trigger.PassengerUpdateTrigger;
import org.jetbrains.annotations.NotNull;
import io.github.yonigev.sfsm.context.StateMachineContext;
import io.github.yonigev.sfsm.guard.Guard;
import io.github.yonigev.sfsm.transition.TransitionContext;

import java.util.Objects;

/**
 * Guards the transition to BOARDING_COMPLETE by receiving a {@link PassengerUpdateTrigger} and allowing transition
 * only when the flight is at/above capacity.
 */
public class BoardingGuard implements Guard<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> {

    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());

        PassengerUpdateTrigger passengerUpdate = (PassengerUpdateTrigger) transitionContext.getTrigger();
        StateMachineContext<FlightStateMachineDefiner.FlightState, FlightStateMachineDefiner.FlightTrigger> context = transitionContext.getStateMachineContext();
        FlightInformation flightInfo = (FlightInformation) context.getProperty(FlightStateMachineApplication.FLIGHT_INFO);

        int additionalPassengers = passengerUpdate.getAdditionalPassengers();
        flightInfo.addPassengers(additionalPassengers);
        return flightInfo.getCurrentPassengerCount() >= flightInfo.getBookedPassengers();
    }
}
