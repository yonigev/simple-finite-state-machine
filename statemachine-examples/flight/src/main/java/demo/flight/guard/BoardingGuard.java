package demo.flight.guard;

import demo.flight.FlightStateMachineApplication;
import demo.flight.FlightStateMachineConfiguration;
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
public class BoardingGuard implements Guard<FlightStateMachineConfiguration.FlightState, FlightStateMachineConfiguration.FlightTrigger> {

    @Override
    public boolean allow(@NotNull TransitionContext<FlightStateMachineConfiguration.FlightState, FlightStateMachineConfiguration.FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());

        PassengerUpdateTrigger passengerUpdate = (PassengerUpdateTrigger) transitionContext.getTrigger();
        StateMachineContext<FlightStateMachineConfiguration.FlightState, FlightStateMachineConfiguration.FlightTrigger> context = transitionContext.getStateMachineContext();
        FlightInformation flightInfo = (FlightInformation) context.getProperty(FlightStateMachineApplication.FLIGHT_INFO);

        int additionalPassengers = passengerUpdate.getAdditionalPassengers();
        flightInfo.addPassengers(additionalPassengers);
        return flightInfo.getCurrentPassengerCount() >= flightInfo.getBookedPassengers();
    }
}
