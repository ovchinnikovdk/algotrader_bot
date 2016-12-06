package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.instrument.Instruments;
import ru.sbt.exchange.genetic.Algorithm;

import java.util.Date;
import java.util.Random;

public class MyAlgoStrategy implements AlgoStrategy {

    private Algorithm algorithm;
    private Date lastRun;

    public MyAlgoStrategy() {
        algorithm = new Algorithm();
    }

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        algorithm.runPopulation(event, broker);
        if (broker.getPeriodInfo().getSecondsToNextPeriod() <= 1) {
            broker.cancelOrdersByInstrument(Instruments.fixedCouponBond());
            broker.cancelOrdersByInstrument(Instruments.floatingCouponBond());
            broker.cancelOrdersByInstrument(Instruments.zeroCouponBond());
            algorithm.nextPopulation();
        }
    }
}