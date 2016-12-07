package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;
import ru.sbt.exchange.domain.instrument.Instruments;
import ru.sbt.exchange.genetic.Algorithm;
import ru.sbt.exchange.genetic.FitnessCalc;

public class MyAlgoStrategy implements AlgoStrategy {

    private Algorithm algorithm;

    public MyAlgoStrategy() {
        algorithm = new Algorithm();
    }

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        FitnessCalc.setBroker(broker);
        if (event.getExchangeEventType() == ExchangeEventType.ORDER_EXECUTION
                && FitnessCalc.containsMyOrder(event.getOrder().getOrderId())) {
            FitnessCalc.addSuccessfulOrder(event.getOrder().getOrderId());
        }
        algorithm.runPopulation(event, broker);
        if (broker.getPeriodInfo().getSecondsToNextPeriod() <= 1) {
            broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
            broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
            broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
            algorithm.nextPopulation();
        }
    }
}