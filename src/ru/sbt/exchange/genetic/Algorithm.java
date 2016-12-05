package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;

import java.util.Random;

/**
 * Created by dmitry on 05.12.16.
 */
public class Algorithm {
    public static Random random = new Random();

    private Population population;

    public Algorithm() {
        population = new Population(true);
    }

    public void runPopulation(ExchangeEvent event, Broker broker){
        this.population.run(event, broker);
    }

    public void nextPopulation() {
        Population population1 = new Population(false);
        Individ fittest = this.population.getFittest();
        population1.add(fittest);
        for (int i = 1; i < this.population.DEFAULT_SIZE; i++) {
            Individ ind1 = this.population.getRandom();
            Individ ind2 = this.population.getRandom();
            population1.add(mutate(crossover(ind1, ind2)));
        }
        this.population = population1;
    }

    private Individ crossover(Individ ind1, Individ ind2) {
        Individ individ = new Individ();
        for (int i = 0; i < individ.DEFAULT_SIZE; i++) {
            individ.add(random.nextBoolean() ? ind1.getRandom() : ind2.getRandom());
        }
        return individ;
    }


    private Individ mutate(Individ individ) {
        return individ;
    }
}
