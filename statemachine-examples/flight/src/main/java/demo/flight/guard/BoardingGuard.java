package demo.flight.guard;

import demo.flight.FlightStateMachineApplication;
import demo.flight.FlightStateMachineDefinition;
import demo.flight.model.FlightInformation;
import demo.flight.trigger.PassengerUpdateTrigger;
import org.jetbrains.annotations.NotNull;
import statemachine.context.StateMachineContext;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

import java.util.Objects;

/**
 * Guards the transition to BOARDING_COMPLETE by receiving a {@link PassengerUpdateTrigger} and allowing transition
 * only when the flight is at/above capacity.
 */
public class BoardingGuard implements Guard<FlightStateMachineDefinition.FlightState, FlightStateMachineDefinition.FlightTrigger> {

    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineDefinition.FlightState, FlightStateMachineDefinition.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());

        PassengerUpdateTrigger passengerUpdate = (PassengerUpdateTrigger) transitionContext.getTrigger();
        StateMachineContext<FlightStateMachineDefinition.FlightState, FlightStateMachineDefinition.FlightTrigger> context = transitionContext.getStateMachineContext();
        FlightInformation flightInfo = (FlightInformation) context.getProperty(FlightStateMachineApplication.FLIGHT_INFO);

        int additionalPassengers = passengerUpdate.getAdditionalPassengers();
        flightInfo.addPassengers(additionalPassengers);
        return flightInfo.getCurrentPassengerCount() >= flightInfo.getBookedPassengers();
    }
}
