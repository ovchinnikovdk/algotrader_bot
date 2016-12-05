package ru.sbt.exchange.genetic;

import java.util.List;

public class AlgoIndivid implements Individ {
    private List<Chromosome> chromosomes;
    private List<Float> weights;
    private Float weightSum;

    public AlgoIndivid(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    @Override
    public void calculateWeights(FitnessStrategy strategy) {

    }

    @Override
    public void exec() {
        chromosomes.forEach(Chromosome::exec);
    }
}
