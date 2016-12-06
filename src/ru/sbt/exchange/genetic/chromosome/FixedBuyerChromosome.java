package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by mika on 06.12.16.
 */
public class FixedBuyerChromosome implements Chromosome {
    private Order order;

    @Override
    public void run(ExchangeEvent event, Broker broker) {
        if (broker.getMyPortfolio().getMoney() < 0) return;
        if (broker.getMyLiveOrders().size() > 2) return;
        order =  broker.getTopOrders(Instruments.fixedCouponBond()).getSellOrders().get(0)
                .opposite();
        double price = order.getPrice() * 0.85;
        order = order.withPrice(price);
        order = order.withQuantity((int) Math.max(broker.getMyPortfolio().getMoney() / 2 * price, 1));

        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
