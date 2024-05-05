package io.github.yonigev.sfsm.demo.flight.trigger;

import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner;
import io.github.yonigev.sfsm.trigger.Trigger;

public class PassengerUpdateTrigger implements Trigger<FlightStateMachineDefiner.FlightTrigger> {
    int additionalPassengers;
    public PassengerUpdateTrigger(int additionalPassengers) {
        this.additionalPassengers = additionalPassengers;
    }

    @Override
    public FlightStateMachineDefiner.FlightTrigger getTriggerId() {
        return FlightStateMachineDefiner.FlightTrigger.PASSENGERS_BOARDED;
    }

    public int getAdditionalPassengers() {
        return additionalPassengers;
    }
}
