package ru.sbt.exchange;

import ru.sbt.exchange.client.AlgoStrategy;
import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.client.MyAlgoStrategy;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;
import ru.sbt.exchange.domain.OrderBuilder;
import ru.sbt.exchange.domain.instrument.Instruments;

/**
 * Created by dmitry on 06.12.16.
 */
public class Main {
    public static void main(String[] args) {
        /*AlgoLocalRunner runner = new AlgoLocalRunner();
        runner.runServer();
        runner.runClient("ddd", new MyAlgoStrategy());
        runner.runClient("ddd1", new MyAlgoStrategy());
        runner.runClient("ddd2", new AlgoStrategy() {
            @Override
            public void onEvent(ExchangeEvent event, Broker broker) {
                System.out.println(event.getExchangeEventType().toString());
                if (event.getExchangeEventType() == ExchangeEventType.STRATEGY_START) {
                    broker.addOrder(OrderBuilder.sell(Instruments.fixedCouponBond()).withPrice(100).withQuantity(1).order());
                }
                if (event.getExchangeEventType() == ExchangeEventType.ORDER_NEW) {
                    broker.addOrder(event.getOrder().opposite());
                }
            }
        });*/
    }
}
