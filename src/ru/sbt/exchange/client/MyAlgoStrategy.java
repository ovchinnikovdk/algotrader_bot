package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.Random;

public class MyAlgoStrategy implements AlgoStrategy {
    int counter = 0;

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        Random random = new Random();
        broker.addOrder(Order.buy(Instruments.zeroCouponBond()).withPrice(random.nextInt(500)).withQuantity(10).order());
        broker.addOrder(Order.sell(Instruments.zeroCouponBond()).withPrice(random.nextInt(500)).withQuantity(10).order());
    }
}