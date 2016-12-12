package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;

/**
 * Created by mika on 05.12.16.
 */
public class Population {
    public static final int DEFAULT_SIZE = 20;

    private Individual[] individuals;
    private int curSize;
    private float[] weights;
    private int doingNothingCount;

    public Population(boolean init) {
        this.individuals = new Individual[DEFAULT_SIZE];
        this.weights = new float[DEFAULT_SIZE];
        this.curSize = 0;
        this.doingNothingCount = 1;
        if (init) {
            for (int i = 0; i < individuals.length; i++) {
                Individual individual = new Individual();
                individual.generate();
                individuals[i] = individual;
            }
            curSize = individuals.length;
        }
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        for (int i = 1; i < curSize; i++) {
            if (fittest.getFitness() < individuals[i].getFitness()) {
                fittest = individuals[i];
            }
        }
        return fittest;
    }

    public void add(Individual ind) {
        individuals[curSize++] = ind;
    }


    public Individual getRandom() {
        float sum = 0.0f;
        for (float f : weights) {
            sum += f;
        }
        float rand = Algorithm.random.nextFloat() * sum;
        float curSum = 0.0f;
        for (int i = 0; i < curSize - 1; i++) {
            curSum += weights[i];
            if (curSum <= rand && curSum + weights[i + 1] > rand) {
                return individuals[i];
            }
            if (i == curSize - 2) {
                return individuals[i + 1];
            }
        }
        return null;
    }

    public void reCalcWeights(){
        for (int i = 0; i < curSize; i++) {
            individuals[i].recalculateFitness();
            weights[i] = individuals[i].getFitness();
        }
    }

    public void run(Broker broker) throws TooLongDoingNothingException {
        if (doingNothingCount % 10 == 0) {
            throw new TooLongDoingNothingException();
        }
        Individual individual = getRandom();
        if (FitnessCalc.calcIndividual(individual) > 1.0f) {
            individual.run(broker);
            return;
        }
        else {
            doingNothingCount++;
            run(broker);
        }
    }
}
