package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;
import ru.sbt.exchange.domain.instrument.Instruments;
import ru.sbt.exchange.genetic.Algorithm;
import ru.sbt.exchange.genetic.FitnessCalc;

public class MyAlgoStrategy implements AlgoStrategy {

    private Algorithm algorithm;
    private int noMyDeals;

    public MyAlgoStrategy() {
        noMyDeals = 1;
        algorithm = new Algorithm();
    }

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        FitnessCalc.setBroker(broker);
        if (event.getExchangeEventType() == ExchangeEventType.NEW_PERIOD_START) {
            broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
            broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
            broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
            algorithm.nextPopulation(event);
        }
        if (event.getExchangeEventType() == ExchangeEventType.ORDER_EXECUTION) {
            if (FitnessCalc.containsMyOrder(event.getOrder().getOrderId())) {
                FitnessCalc.addSuccessfulOrder(event.getOrder().getOrderId());
            }
            else {
                noMyDeals++;
                if (noMyDeals % 15 == 0) {
                    algorithm.nextPopulation(event);
                }
            }
        }
        algorithm.runPopulation(event, broker);
    }
}