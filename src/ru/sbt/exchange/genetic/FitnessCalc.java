package ru.sbt.exchange.genetic;


import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmitry on 05.12.16.
 */
public class FitnessCalc {

    private static Set<String> successfulOrders = new HashSet<>();
    private static Set<String> myOrders = new HashSet<>();
    private static Broker broker;


    public static void addSuccessfulOrder(String id) {
        successfulOrders.add(id);
    }

    public static void addMyOrderId(String id){
        myOrders.add(id);
    }

    public static boolean containsMyOrder(String id) {
        return myOrders.contains(id);
    }

    public static void setBroker(Broker broker1){
        broker = broker1;
    }


    public static Float calcIndividual(Individual individual) {
        float result;
        float money = individual.get("price") * individual.get("quantity");
        if (broker.getMyPortfolio().getMoney() < 0) {
            money += broker.getMyPortfolio().getMoney();
        }

        int n = broker.getPeriodInfo().getEndPeriodNumber() - broker.getPeriodInfo().getCurrentPeriodNumber();
        float benefit = (float) (money * Math.pow(1 + broker.getMyPortfolio().getPeriodInterestRate() / 100.0f, n));
        float nominal;
        float coupon;
        float type = individual.get("type");
        if (type >= 0 && type < 1) {
            nominal = (float) Instruments.zeroCouponBond().getNominal();
            coupon = 0.0f;
        }
        else if (type >= 1 && type < 2) {
            nominal = (float) Instruments.fixedCouponBond().getNominal();
            coupon = (float) Instruments.fixedCouponBond().getCouponInPercents().getMin();
        }
        else {
            nominal = (float) Instruments.floatingCouponBond().getNominal();
            coupon = (float) Instruments.floatingCouponBond().getCouponInPercents().getMin();
        }
        float loss = individual.get("quantity") * nominal * (1 + n * coupon / 100.0f);

        if (individual.get("type") < 0.5) {
            result = benefit - loss;
        }
        else {
            result = loss - benefit;
        }
        /*if (!successfulOrders.contains(individual.getId())) {
            result /= 2;
        }*/
        return Math.max(1.0f, result);
    }
}
