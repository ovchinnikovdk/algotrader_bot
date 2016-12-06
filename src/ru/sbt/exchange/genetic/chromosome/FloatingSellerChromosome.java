package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.OrderBuilder;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by mika on 06.12.16.
 */
public class FloatingSellerChromosome implements Chromosome {
    private Order order;
    @Override
    public void run(ExchangeEvent event, Broker broker) {
        double nominal = Instruments.floatingCouponBond().getNominal();
        order = OrderBuilder.sell(Instruments.floatingCouponBond())
                .withPrice(broker.getTopOrders(Instruments.floatingCouponBond()).getBuyOrders().get(0).getPrice() * 1.05)
                .withQuantity(broker.getMyPortfolio().getMoney() > 0 ?
                        1 : broker.getMyPortfolio().getCountByInstrument().get(Instruments.floatingCouponBond()) * 5)
                .order();
        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
