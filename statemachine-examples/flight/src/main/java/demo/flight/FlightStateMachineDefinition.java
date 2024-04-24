package demo.flight;

import demo.flight.FlightStateMachineDefinition.FlightState;
import demo.flight.FlightStateMachineDefinition.FlightTrigger;
import demo.flight.guard.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import statemachine.definition.DefaultStateMachineDefinition;
import statemachine.definition.state.StatesDefinition;
import statemachine.definition.transition.TransitionsDefinition;
import statemachine.guard.Guard;

import java.util.Arrays;

import static demo.flight.FlightStateMachineDefinition.FlightState.*;
import static demo.flight.FlightStateMachineDefinition.FlightTrigger.*;
import static statemachine.action.TransitionAction.create;

@Slf4j
public class FlightStateMachineDefinition extends DefaultStateMachineDefinition<FlightState, FlightTrigger> {

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
    public StatesDefinition<FlightState, FlightTrigger> defineStates() {
        StatesDefinition<FlightState, FlightTrigger> statesDefinition = super.defineStates();
        statesDefinition.setInitial(BOARDING);
        statesDefinition.terminal(FlightState.LANDED);
        Arrays.stream(FlightState.values()).forEach(statesDefinition::simple);
        return statesDefinition;
    }

    @NotNull
    @Override
    public TransitionsDefinition<FlightState, FlightTrigger> defineTransitions() {
        TransitionsDefinition<FlightState, FlightTrigger> transitionsDefinition = super.defineTransitions();
        transitionsDefinition.add(BOARDING, BOARDING_COMPLETE, PASSENGERS_BOARDED, boardingGuard, create((context) -> {
            log.info("Boarding is complete");
            return null;
        }));
        transitionsDefinition.add(BOARDING_COMPLETE, EN_ROUTE_TO_RUNWAY, LEAVE_GATE, alwaysTrueGuard, create((context) -> {
            log.info("En Route to the Runway");
            return null;
        }));
        transitionsDefinition.add(EN_ROUTE_TO_RUNWAY, WAITING_AT_RUNWAY, LOCATION_UPDATE, runwayGuard, create((context) -> {
            log.info("Waiting for clearance");
            return null;
        }));
        transitionsDefinition.add(WAITING_AT_RUNWAY, TAKING_OFF, DEPARTURE_CLEARED, alwaysTrueGuard, create((context) -> {
            log.info("Beginning takeoff");
            return null;
        }));
        transitionsDefinition.add(TAKING_OFF, CRUISING, LOCATION_UPDATE, cruisingGuard, create((context) -> {
            log.info("Cruising altitude reached");
            return null;
        }));
        transitionsDefinition.add(CRUISING, LANDING, LOCATION_UPDATE, landingGuard, create((context) -> {
            log.info("Beginning landing");
            return null;
        }));
        transitionsDefinition.add(LANDING, LANDED, LOCATION_UPDATE, arrivalGuard, create((context) -> {
            log.info("The plane has landed, welcome to Somewhere!");
            return null;
        }));
        return transitionsDefinition;
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
