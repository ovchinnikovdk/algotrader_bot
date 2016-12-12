package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;

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
        try {
            this.population.run(broker);
        } catch (TooLongDoingNothingException e) {
            nextPopulation(event);
        }
    }

    public void nextPopulation(ExchangeEvent event) {
        Population population1 = new Population(false);
        Individual fittest = this.population.getFittest();
        population1.add(fittest);
        for (int i = 1; i < this.population.DEFAULT_SIZE; i++) {
            Individual ind1 = this.population.getRandom();
            Individual ind2 = this.population.getRandom();
            float rand = random.nextFloat();
            if (rand < 0.7)  {
                population1.add(crossover(ind1, ind2));
            }
            else if (rand < 0.9) {
                population1.add(mutate(crossover(ind1, ind2)));
            }
            else {
                if (event.getExchangeEventType() == ExchangeEventType.ORDER_NEW) {
                    Individual individual = new Individual(event.getOrder().opposite());
                    population1.add(individual);
                }
                else {
                    population1.add(crossover(ind1, ind2));
                }
            }
        }
        population1.reCalcWeights();
        this.population = population1;
    }

    private Individual crossover(Individual ind1, Individual ind2) {
        float sumFitness = ind1.getFitness() + ind2.getFitness();
        /*Individual individual = new Individual();
        if (random.nextFloat() * sumFitness > ind1.getFitness()) {
            individual.put("type", ind1.get("type"));
        }
        else {
            individual.put("type", ind2.get("type"));
        }
        if (random.nextFloat() * sumFitness > ind1.getFitness()) {
            individual.put("coupon", ind1.get("coupon"));
        }
        else {
            individual.put("coupon", ind2.get("coupon"));
        }
        if (random.nextFloat() * sumFitness > ind1.getFitness()) {
            individual.put("price", ind1.get("price"));
        }
        else {
            individual.put("price", ind2.get("price"));
        }
        if (random.nextFloat() * sumFitness > ind1.getFitness()) {
            individual.put("quantity", ind1.get("quantity"));
        }
        else {
            individual.put("quantity", ind2.get("quantity"));
        } */
        return random.nextFloat() * sumFitness > ind1.getFitness() ? ind1 : ind2;
    }


    private Individual mutate(Individual individual) {
        Individual individual1 = new Individual();
        individual1.generate();
        return crossover(individual1, individual);
    }
}
