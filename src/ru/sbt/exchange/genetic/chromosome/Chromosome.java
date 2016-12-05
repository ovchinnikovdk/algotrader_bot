package ru.sbt.exchange.genetic.chromosome;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;

/**
 * Created by dmitry on 05.12.16.
 */
public interface Chromosome {
    void run(ExchangeEvent event, Broker broker);
    String getId();
}
