package ru.sbt.exchange.genetic;

public interface Individ {
    void exec();
    void calculateWeights(FitnessStrategy strategy);
}
