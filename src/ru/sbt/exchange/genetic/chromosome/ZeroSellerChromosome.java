package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.OrderBuilder;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by mika on 06.12.16.
 */
public class ZeroSellerChromosome implements Chromosome {
    private Order order;

    @Override
    public void run(ExchangeEvent event, Broker broker) {
        double nominal = Instruments.zeroCouponBond().getNominal();
        order = OrderBuilder.sell(Instruments.zeroCouponBond())
                .withPrice(nominal * 0.8)
                .withQuantity(broker.getMyPortfolio().getCountByInstrument().get(Instruments.zeroCouponBond()) * 5)
                .order();
        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
