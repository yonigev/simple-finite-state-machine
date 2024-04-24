package demo.flight.trigger;

import demo.flight.FlightStateMachineDefiner.FlightTrigger;
import statemachine.trigger.Trigger;

public class PassengerUpdateTrigger implements Trigger<FlightTrigger> {
    int additionalPassengers;
    public PassengerUpdateTrigger(int additionalPassengers) {
        this.additionalPassengers = additionalPassengers;
    }

    @Override
    public FlightTrigger getTriggerId() {
        return FlightTrigger.PASSENGERS_BOARDED;
    }

    public int getAdditionalPassengers() {
        return additionalPassengers;
    }
}
