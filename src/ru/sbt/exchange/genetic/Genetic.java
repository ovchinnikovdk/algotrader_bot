package ru.sbt.exchange.genetic;

import java.util.Map;

public interface Genetic {
    Map<Individ, Integer> nextGeneration();
    void geneticAlgorithm();
    void genetateFirstPopulation();
}
