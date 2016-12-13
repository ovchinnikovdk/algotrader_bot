package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;
import ru.sbt.exchange.domain.instrument.Instruments;
import ru.sbt.exchange.genetic.Algorithm;

import java.util.HashSet;
import java.util.Set;

public class MyAlgoStrategy implements AlgoStrategy {

    private Algorithm algorithm;
    private int dealsCount;

    public MyAlgoStrategy() {
        dealsCount = 1;
        algorithm = new Algorithm();
    }

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        algorithm.getFitnessFunction().setBroker(broker);
        if (event.getExchangeEventType() == ExchangeEventType.ORDER_EXECUTION) {
            dealsCount++;
            if (dealsCount % 15 == 0) {
                broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
                broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
                broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
                algorithm.nextPopulation(event);
            }
            else if (dealsCount % 7 == 0) {
                broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
                broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
                broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
            }
        }
        algorithm.runPopulation(event, broker);
        if (event.getExchangeEventType() == ExchangeEventType.NEW_PERIOD_START) {
            broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
            broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
            broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
            algorithm.nextPopulation(event);
        }
    }
}