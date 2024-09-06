package io.github.yonigev.sfsm.demo.flight;

import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightState;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightTrigger;
import io.github.yonigev.sfsm.demo.flight.guard.*;
import io.github.yonigev.sfsm.uml.annotation.Uml;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import io.github.yonigev.sfsm.definition.StateMachineDefiner;
import io.github.yonigev.sfsm.definition.state.StatesDefiner;
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner;
import io.github.yonigev.sfsm.guard.Guard;

import java.util.Arrays;

import static io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightState.*;
import static io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightTrigger.*;

@Slf4j
@Uml
public class FlightStateMachineDefiner extends StateMachineDefiner<FlightState, FlightTrigger> {

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

    @Override
    protected void defineStates(@NotNull StatesDefiner<FlightState, FlightTrigger> definer) {
        definer.setInitial(BOARDING);
        definer.terminal(FlightState.LANDED);
        Arrays.stream(FlightState.values()).forEach(definer::simple);
    }

    @Override
    protected void defineTransitions(@NotNull TransitionsDefiner<FlightState, FlightTrigger> definer) {
        definer.add(BOARDING, BOARDING_COMPLETE, PASSENGERS_BOARDED, boardingGuard, (context) -> log.info("Boarding is complete"));
        definer.add(BOARDING_COMPLETE, EN_ROUTE_TO_RUNWAY, LEAVE_GATE, alwaysTrueGuard, (context) -> log.info("En Route to the Runway"));
        definer.add(EN_ROUTE_TO_RUNWAY, WAITING_AT_RUNWAY, LOCATION_UPDATE, runwayGuard, (context) -> log.info("Waiting for clearance"));
        definer.add(WAITING_AT_RUNWAY, TAKING_OFF, DEPARTURE_CLEARED, alwaysTrueGuard, (context) -> log.info("Beginning takeoff"));
        definer.add(TAKING_OFF, CRUISING, LOCATION_UPDATE, cruisingGuard, (context) -> log.info("Cruising altitude reached"));
        definer.add(CRUISING, LANDING, LOCATION_UPDATE, landingGuard, (context) -> log.info("Beginning landing"));
        definer.add(LANDING, LANDED, LOCATION_UPDATE, arrivalGuard, (context) -> log.info("The plane has landed, welcome to Somewhere!"));
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
