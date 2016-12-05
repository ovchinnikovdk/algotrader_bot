package ru.sbt.exchange.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgoGenetic implements Genetic {
    public static int POPULATION_SIZE = 5;
    public static int INDIVID_SIZE = 5;
    private List<Individ> currentGeneration;
    private List<Float> generationWeights;
    private float weightsSum;

    public AlgoGenetic() {
        currentGeneration = new ArrayList<>(POPULATION_SIZE);
        generationWeights = new ArrayList<>(POPULATION_SIZE);
        weightsSum = 0.0f;
        for (int i = 0; i < POPULATION_SIZE; i++) {

        }
    }

    @Override
    public void genetateFirstPopulation() {

    }

    @Override
    public Map<Individ, Integer> nextGeneration() {

        return null;
    }

    @Override
    public void geneticAlgorithm() {

    }
}
