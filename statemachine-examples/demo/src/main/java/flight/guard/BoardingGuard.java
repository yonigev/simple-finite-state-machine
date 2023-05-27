package flight.guard;

import flight.model.FlightInformation;
import flight.FlightStateMachineConfiguration.*;
import flight.trigger.PassengerUpdateTrigger;
import org.jetbrains.annotations.NotNull;
import statemachine.context.StateMachineContext;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

import java.util.Objects;

import static flight.FlightStateMachineApplication.FLIGHT_INFO;

/**
 * Guards the transition to BOARDING_COMPLETE by receiving a {@link PassengerUpdateTrigger} and allowing transition
 * only when the flight is at/above capacity.
 */
public class BoardingGuard implements Guard<FlightState, FlightTrigger> {

    @Override
    public boolean transition(@NotNull TransitionContext<FlightState, FlightTrigger> transitionContext) {
        Objects.requireNonNull(transitionContext.getTrigger());

        PassengerUpdateTrigger passengerUpdate = (PassengerUpdateTrigger) transitionContext.getTrigger();
        StateMachineContext<FlightState, FlightTrigger> context = transitionContext.getStateMachineContext();
        FlightInformation flightInfo = (FlightInformation) context.getProperty(FLIGHT_INFO);

        int additionalPassengers = passengerUpdate.getAdditionalPassengers();
        flightInfo.addPassengers(additionalPassengers);
        return flightInfo.getCurrentPassengerCount() >= flightInfo.getBookedPassengers();
    }
}
