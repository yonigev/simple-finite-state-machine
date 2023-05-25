package demo;

import demo.guard.BoardingGuard;
import org.jetbrains.annotations.NotNull;
import statemachine.configuration.DefaultStateMachineConfiguration;
import statemachine.configuration.state.StatesConfiguration;
import statemachine.configuration.transition.TransitionsConfiguration;
import demo.FlightStateMachineConfiguration.Trigger;
import demo.FlightStateMachineConfiguration.State;
import java.util.Arrays;

public class FlightStateMachineConfiguration extends DefaultStateMachineConfiguration<State, Trigger> {

    BoardingGuard boardingGuard = new BoardingGuard();
    @NotNull
    @Override
    public StatesConfiguration<State, Trigger> configureStates() {
        StatesConfiguration<State, Trigger> statesConfig = super.configureStates();
        statesConfig.setInitial(State.BOARDING);
        statesConfig.setTerminal(State.LANDED);
        Arrays.stream(State.values()).forEach(statesConfig::add);
        return statesConfig;
    }

    @NotNull
    @Override
    public TransitionsConfiguration<State, Trigger> configureTransitions() {
        TransitionsConfiguration<State, Trigger> transitionsConfig = super.configureTransitions();
        transitionsConfig.add(State.BOARDING, State.BOARDING_COMPLETE, Trigger.PASSENGERS_BOARDED, boardingGuard, null);
        transitionsConfig.add(State.BOARDING_COMPLETE, State.EN_ROUTE_TO_RUNWAY, Trigger.LOCATION_UPDATE, boardingGuard, null);
        return transitionsConfig;
    }

   public enum State {
        BOARDING,
        BOARDING_COMPLETE,
        EN_ROUTE_TO_RUNWAY,
        WAITING_AT_RUNWAY,
        TAKING_OFF,
        EN_ROUTE_TO_DESTINATION, LANDING,
        LANDED
    }
    public enum Trigger {
       PASSENGERS_BOARDED,
        LEAVE_GATE,
        LOCATION_UPDATE,
        DEPARTURE_CLEARED,
        ALTITUDE_UPDATE;
    }
}
