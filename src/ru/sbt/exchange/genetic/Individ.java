package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.genetic.chromosome.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 05.12.16.
 */
public class Individ {

    public static final int DEFAULT_SIZE = 5;

    private Float fitness;
    private Chromosome[] genes;
    private int curSize;
    private float[] weights;

    public Individ() {
        this.genes = new Chromosome[DEFAULT_SIZE];
        this.weights = new float[DEFAULT_SIZE];
        this.curSize = 0;
    }

    public Chromosome getRandom() {
        calcWeights();
        float sum = getFitness();
        float rand = Algorithm.random.nextFloat() * sum;
        float curSum = 0.0f;
        for (int i = 0; i < curSize - 1; i++) {
            curSum += weights[i];
            if (curSum <= rand && curSum + weights[i + 1] > rand) {
                return genes[i];
            }
            if (i == curSize - 2) {
                return genes[i];
            }
        }
        return null;
    }

    public void add(Chromosome chromosome) {
        genes[curSize++] = chromosome;
    }

    public void generate() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = getRandomChromosome();
        }
    }

    public float getFitness() {
        if (fitness == null) {
            fitness = 0.0f;
            for (float f : weights) {
                fitness += f;
            }
        }
        return fitness;
    }

    public void calcWeights(){
        for (int i = 0 ; i < curSize; i++) {
            weights[i] = FitnessCalc.calcGen(genes[i]);
        }
    }

    public void run(ExchangeEvent event, Broker broker) {
        for (int i = 0; i < curSize; i++) {
            genes[i].run(event, broker);
        }
    }

    public Chromosome getRandomChromosome() {
        List<Class<? extends  Chromosome>> list = new ArrayList<>();
        list.add(DummyChromosome.class);
        list.add(FixedSellerChromosome.class);
        list.add(ZeroBuyerChromosome.class);
        list.add(ZeroSellerChromosome.class);
        try {
            return list.get(Algorithm.random.nextInt(list.size())).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ZeroBuyerChromosome();
    }
}
