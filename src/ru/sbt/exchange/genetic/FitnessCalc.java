package ru.sbt.exchange.genetic;


import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;
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
        PeriodInfo info = broker.getPeriodInfo();
        Portfolio portfolio = broker.getMyPortfolio();
        float result;
        float money = individual.get("price") * individual.get("quantity");
        if (portfolio.getMoney() < 0 && individual.get("type") >= 0.5) {
            money += portfolio.getMoney();
        }

        int n = info.getEndPeriodNumber() - info.getCurrentPeriodNumber();
        float benefit = (float) (money * Math.pow(1.0f + portfolio.getPeriodInterestRate() / 100.0f, n));

        float nominal;
        float coupon;
        float couponType = individual.get("coupon");
        if (couponType >= 0.0f && couponType < 1.0f) {
            nominal = (float) Instruments.zeroCouponBond().getNominal();
            coupon = 0.0f;
        }
        else if (couponType >= 1.0f && couponType < 2.0f) {
            nominal = (float) Instruments.fixedCouponBond().getNominal();
            coupon = (float) Instruments.fixedCouponBond().getCouponInPercents().getMin();
        }
        else {
            nominal = (float) Instruments.floatingCouponBond().getNominal();
            coupon = (float) Instruments.floatingCouponBond().getCouponInPercents().getMin();
        }
        float loss = individual.get("quantity") * nominal * (1 + n * coupon / 100.0f);

        float tax  = (float) (individual.get("quantity") * individual.get("price") *
                        portfolio.getBrokerFeeInPercents() / 100.f);

        if (individual.get("type") < 0.5) {
            result = benefit - loss - tax;
        }
        else {
            result = loss - benefit - tax;
        }
        return Math.max(1.0f, result);
    }
}
