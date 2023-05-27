package flight;

import flight.FlightStateMachineConfiguration.FlightState;
import flight.FlightStateMachineConfiguration.FlightTrigger;
import flight.guard.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import statemachine.configuration.DefaultStateMachineConfiguration;
import statemachine.configuration.state.StatesConfiguration;
import statemachine.configuration.transition.TransitionsConfiguration;
import statemachine.guard.Guard;

import java.util.Arrays;

import static flight.FlightStateMachineConfiguration.FlightState.*;
import static flight.FlightStateMachineConfiguration.FlightTrigger.*;
import static statemachine.action.Action.createAction;

@Slf4j
public class FlightStateMachineConfiguration extends DefaultStateMachineConfiguration<FlightState, FlightTrigger> {

    // Allows transition only when boarding is complete.
    BoardingGuard boardingGuard = new BoardingGuard();
    // Allows transition only when the plane is away from the gate location.
    RunwayGuard runwayGuard = new RunwayGuard();
    // Allows transition only when the plane is at the proper height.
    CruisingGuard cruisingGuard = new CruisingGuard();

    // Allows transition only when the plane is at the proper height.
    LandingGuard landingGuard = new LandingGuard();

    // Allows transition only when the plane has arrived at the destination.
    ArrivalGuard arrivalGuard = new ArrivalGuard();

    Guard<FlightState, FlightTrigger> alwaysTrueGuard = Guard.Companion.ofPredicate(() -> true);

    @NotNull
    @Override
    public StatesConfiguration<FlightState, FlightTrigger> configureStates() {
        StatesConfiguration<FlightState, FlightTrigger> statesConfig = super.configureStates();
        statesConfig.setInitial(BOARDING);
        statesConfig.setTerminal(FlightState.LANDED);
        Arrays.stream(FlightState.values()).forEach(statesConfig::add);
        return statesConfig;
    }

    @NotNull
    @Override
    public TransitionsConfiguration<FlightState, FlightTrigger> configureTransitions() {
        TransitionsConfiguration<FlightState, FlightTrigger> transitionsConfig = super.configureTransitions();
        transitionsConfig.add(BOARDING, BOARDING_COMPLETE, PASSENGERS_BOARDED, boardingGuard, createAction(() -> {
            log.info("Boarding is complete");
            return null;
        }));
        transitionsConfig.add(BOARDING_COMPLETE, EN_ROUTE_TO_RUNWAY, LEAVE_GATE, alwaysTrueGuard, createAction(() -> {
            log.info("En Route to the Runway");
            return null;
        }));
        transitionsConfig.add(EN_ROUTE_TO_RUNWAY, WAITING_AT_RUNWAY, LOCATION_UPDATE, runwayGuard, createAction(() -> {
            log.info("Waiting for clearance");
            return null;
        }));
        transitionsConfig.add(WAITING_AT_RUNWAY, TAKING_OFF, DEPARTURE_CLEARED, alwaysTrueGuard, createAction(() -> {
            log.info("Beginning takeoff");
            return null;
        }));
        transitionsConfig.add(TAKING_OFF, CRUISING, LOCATION_UPDATE, cruisingGuard, createAction(() -> {
            log.info("Cruising altitude reached");
            return null;
        }));
        transitionsConfig.add(CRUISING, LANDING, LOCATION_UPDATE, landingGuard, createAction(() -> {
            log.info("Beginning landing");
            return null;
        }));
        transitionsConfig.add(LANDING, LANDED, LOCATION_UPDATE, arrivalGuard, createAction(() -> {
            log.info("The plane has landed, welcome to Somewhere!");
            return null;
        }));
        return transitionsConfig;
    }

    public enum FlightState {
        BOARDING,
        BOARDING_COMPLETE,
        EN_ROUTE_TO_RUNWAY,
        WAITING_AT_RUNWAY,
        TAKING_OFF,
        CRUISING,
        LANDING,
        LANDED
    }

    public enum FlightTrigger {
        PASSENGERS_BOARDED,
        LEAVE_GATE,
        LOCATION_UPDATE,
        DEPARTURE_CLEARED
    }
}
