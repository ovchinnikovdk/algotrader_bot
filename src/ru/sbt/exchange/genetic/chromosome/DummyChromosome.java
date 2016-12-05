package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.*;
import ru.sbt.exchange.domain.instrument.Instruments;
import ru.sbt.exchange.genetic.Algorithm;

/**
 * Created by dmitry on 05.12.16.
 */
public class DummyChromosome implements Chromosome {

    private int dummy_number;
    private Order order;

    public DummyChromosome() {
        dummy_number = Algorithm.random.nextInt(ExchangeEventType.values().length);
    }

    @Override
    public void run(ExchangeEvent event, Broker broker) {
        order = OrderBuilder.sell(Instruments.floatingCouponBond())
                .withPrice(Instruments.floatingCouponBond().getCouponInPercents().getMax() - 0.001)
                .withQuantity(1)
                .order();
        broker.addOrder(order);
        Portfolio portfolio = broker.getMyPortfolio();
        portfolio.setMoney(portfolio.getMoney() + 10000);
    }

    @Override
    public String getId() {
        if (order == null) {
            return null;
        }
        return order.getOrderId();
    }
}
