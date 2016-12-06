package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.*;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by mika on 06.12.16.
 */
public class FixedSellerChromosome implements Chromosome {
    private Order order;

    @Override
    public void run(ExchangeEvent event, Broker broker) {
        double nominal = Instruments.fixedCouponBond().getNominal();
        order = OrderBuilder.sell(Instruments.fixedCouponBond())
                .withPrice(nominal +
                        Instruments.fixedCouponBond().getCouponInPercents().getMax() * 0.1 * nominal)
                .withQuantity(broker.getMyPortfolio().getCountByInstrument().get(Instruments.fixedCouponBond()) * 5)
                .order();
        broker.addOrder(order);
    }

    @Override
    public String getId() {
        return order.getOrderId();
    }
}
