package demo.guard;

import demo.FlightStateMachineConfiguration.*;
import org.jetbrains.annotations.NotNull;
import statemachine.context.StateMachineContext;
import statemachine.guard.Guard;
import statemachine.transition.TransitionContext;

public class BoardingGuard implements Guard<State, Trigger> {
    private final Integer planeCapacity = 189;
    private static final String PASSENGER_COUNT = "PASSENGER_COUNT";
    private static final String PASSENGER_BOOKED = "PASSENGER_BOOKED";
    @Override
    public boolean evaluate(@NotNull TransitionContext<State, Trigger> transitionContext) {
        StateMachineContext<State, Trigger> context = transitionContext.getStateMachineContext();
        Integer passengerCount = (Integer) context.getProperty(PASSENGER_COUNT);
        Integer requiredPassengerCount = (Integer) context.getProperty(PASSENGER_BOOKED);



    }
}
