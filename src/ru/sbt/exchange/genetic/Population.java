package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;

/**
 * Created by dmitry on 05.12.16.
 */
public class Population {
    public static final int DEFAULT_SIZE = 5;

    private Individ[] individs;
    private int curSize;
    private float[] weights;

    public Population(boolean init) {
        this.individs = new Individ[DEFAULT_SIZE];
        this.weights = new float[DEFAULT_SIZE];
        this.curSize = 0;
        if (init) {
            for (int i = 0; i < individs.length; i++) {
                Individ individ = new Individ();
                individ.generate();
                individs[i] = individ;
            }
            curSize = individs.length;
        }
    }

    public Individ getFittest() {
        Individ fittest = individs[0];
        for (int i = 1; i < curSize; i++) {
            if (fittest.getFitness() < individs[i].getFitness()) {
                fittest = individs[i];
            }
        }
        return fittest;
    }

    public void add(Individ ind) {
        individs[curSize++] = ind;
    }

    public Individ get(int index) {
        if (index < curSize) {
            return individs[index];
        }
        return null;
    }

    public Individ getRandom() {
        calcWeights();
        float sum = 0.0f;
        for (float f : weights) {
            sum += f;
        }
        float rand = Algorithm.random.nextFloat() * sum;
        float curSum = 0.0f;
        for (int i = 0; i < curSize - 1; i++) {
            curSum += weights[i];
            if (curSum <= rand && curSum + weights[i + 1] > rand) {
                return individs[i];
            }
            if (i == curSize - 2) {
                return individs[i + 1];
            }
        }
        return null;
    }

    public void calcWeights(){
        for (int i = 0; i < curSize; i++) {
            weights[i] = individs[i].getFitness();
        }
    }

    public void run(ExchangeEvent event, Broker broker) {
        for (int i = 0; i < curSize; i++) {
            individs[i].run(event, broker);
        }
    }
}
