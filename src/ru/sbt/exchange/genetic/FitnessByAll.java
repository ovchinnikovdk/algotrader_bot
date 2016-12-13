package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;
import ru.sbt.exchange.domain.instrument.Bond;
import ru.sbt.exchange.domain.instrument.Instrument;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.Map;

/**
 * Created by dmitry on 13.12.16.
 */
public class FitnessByAll implements FitnessFunction{
    private Broker broker;


    @Override
    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    @Override
    public Float calcIndividual(Individual individual) {
        Portfolio portfolio = broker.getMyPortfolio();
        PeriodInfo info = broker.getPeriodInfo();
        float bankPercent = (float) portfolio.getBrokerFeeInPercents();
        float rest = (float) portfolio.getMoney();
        float price = individual.get("price");
        float quantity = individual.get("quantity");
        int n = info.getEndPeriodNumber() - info.getCurrentPeriodNumber();
        Instrument couponType;
        switch ((int)individual.get("coupon")) {
            case 0:
                couponType = Instruments.zeroCouponBond();
                break;
            case 1:
                couponType = Instruments.fixedCouponBond();
                break;
            case 2:
                couponType = Instruments.floatingCouponBond();
                break;
            default:
                couponType = Instruments.zeroCouponBond();
        }

        //in case order happens
        float result;
        int sign = individual.get("type") < 0.5 ? 1 : -1;
        result = rest + sign * price * quantity;
        for (int i = 1; i <= n; i++) {
            result *= (1.0f + bankPercent / 100.0f);
            for (Map.Entry<Instrument, Integer> entry : portfolio.getCountByInstrument().entrySet()) {
                int currentQuantity = entry.getValue();
                if (entry.getKey().equals(couponType)) {
                    currentQuantity += (-1.0f) * sign * quantity;
                }
                Bond bond = (Bond)entry.getKey();
                if (currentQuantity > 0) {
                    result += currentQuantity * bond.getNominal() * bond.getCouponInPercents().getMin() / 100.0f;
                }
                else if (i == 1) {
                    result += currentQuantity * bond.getNominal();
                }
            }
        }
        for (Map.Entry<Instrument, Integer> entry : portfolio.getCountByInstrument().entrySet()) {
            Bond bond = (Bond) entry.getKey();
            result += bond.getNominal() * entry.getValue();
        }

        //in case order does not happen
        for (int i = 1; i <= n; i++) {
            rest *= (1.0f + bankPercent / 100.0f);
            for (Map.Entry<Instrument, Integer> entry : portfolio.getCountByInstrument().entrySet()) {
                int currentQuantity = entry.getValue();
                Bond bond = (Bond)entry.getKey();
                if (currentQuantity > 0) {
                    rest += currentQuantity * bond.getNominal() * bond.getCouponInPercents().getMin() / 100.0f;
                }
                else if (i == 1) {
                    rest += currentQuantity * bond.getNominal();
                }
            }
        }
        for (Map.Entry<Instrument, Integer> entry : portfolio.getCountByInstrument().entrySet()) {
            Bond bond = (Bond) entry.getKey();
            rest += bond.getNominal() * entry.getValue();
        }

        //calculate benefit
        float benefit;
        if (rest < 0.0f || result < 0.0f) {
            if (result < 0.0f && rest < 0.0f) {
                benefit = rest / result;
            }
            else if (result < 0.0f) {
                benefit = 0.0f;
            }
            else {
                benefit = (result - rest) / Math.abs(rest);
            }
        }
        else {
            benefit = result / rest;
        }
        return Math.max(benefit , 0.1f);
    }
}
