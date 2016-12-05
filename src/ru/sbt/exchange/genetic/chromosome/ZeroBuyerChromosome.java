package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by dmitry on 06.12.16.
 */
public class ZeroBuyerChromosome implements Chromosome {
    private Order order;
    @Override
    public void run(ExchangeEvent event, Broker broker) {
        order = broker.getTopOrders(Instruments.zeroCouponBond()).getSellOrders().get(0).opposite();
        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
