package ru.sbt.exchange.genetic;


import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;
import ru.sbt.exchange.domain.instrument.Instruments;


/**
 * Created by dmitry on 05.12.16.
 */
public class FitnessByPrice implements FitnessFunction {

    private Broker broker;

    public void setBroker(Broker broker1){
        broker = broker1;
    }


    public Float calcIndividual(Individual individual) {
        PeriodInfo info = broker.getPeriodInfo();
        Portfolio portfolio = broker.getMyPortfolio();
        float result;
        float price = individual.get("price");
        float quantity = individual.get("quantity");
        float rest = (float) portfolio.getMoney();
        int n = info.getEndPeriodNumber() - info.getCurrentPeriodNumber();

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

        float wantedPrice = 0.0f;
        float r;

        if (individual.get("type") < 0.5f) {
            r = (float) (portfolio.getPeriodInterestRate() / 100.0f + 0.1f);
        }
        else {
            r = (float) (portfolio.getPeriodInterestRate() / 100.0f + 0.3f);
        }

        for (int i = 1; i <= n; i++) {
            wantedPrice += coupon / Math.pow(1 + r, i);
        }
        wantedPrice += nominal / Math.pow(1 + r, n);

        if (individual.get("type") < 0.5) {
            result = wantedPrice / price;
        }
        else {
            result = price / wantedPrice;
        }
        individual.put("quantity", (float) (portfolio.getMoney() / 2.0f / price));
        return Math.max(0.1f, result);
    }
}
