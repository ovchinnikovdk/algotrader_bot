package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by dmitry on 06.12.16.
 */
public class FloatingBuyerChromosome implements Chromosome {
    private Order order;

    @Override
    public void run(ExchangeEvent event, Broker broker) {
        if (broker.getMyPortfolio().getMoney() < 0) return;
        if (broker.getMyLiveOrders().size() > 2) return;
        double nominal = Instruments.floatingCouponBond().getNominal();
        order = broker.getTopOrders(Instruments.floatingCouponBond())
                .getSellOrders().get(0).opposite()
                .withPrice(nominal);
        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
