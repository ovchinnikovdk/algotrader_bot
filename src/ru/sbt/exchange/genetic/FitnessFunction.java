package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;

public interface FitnessFunction {
    void setBroker(Broker broker);
    Float calcIndividual(Individual individual);
}
