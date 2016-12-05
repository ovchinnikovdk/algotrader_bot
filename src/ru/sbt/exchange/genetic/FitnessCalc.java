package ru.sbt.exchange.genetic;


import ru.sbt.exchange.genetic.chromosome.Chromosome;

/**
 * Created by dmitry on 05.12.16.
 */
public class FitnessCalc {


    public static float calcGen(Chromosome gene) {
        return Algorithm.random.nextFloat() % 5;
    }
}
