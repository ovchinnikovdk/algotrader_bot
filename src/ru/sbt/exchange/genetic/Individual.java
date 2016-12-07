package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.OrderBuilder;
import ru.sbt.exchange.domain.instrument.Bond;
import ru.sbt.exchange.domain.instrument.Instrument;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry on 05.12.16.
 */
public class Individual {

    private Float fitness;
    private Map<String, Float> genesMap;
    private String id;

    public Individual() {
        this.genesMap = new HashMap<>();
    }


    public void generate() {
        //sell or buy
        float type = Algorithm.random.nextFloat();
        boolean sell = type < 0.5;
        genesMap.put("type", type);
        //zero, floating or fixed
        float coupon = Algorithm.random.nextFloat() * 3;
        genesMap.put("coupon", coupon);
        //price
        float price;
        switch ((int)Math.ceil(coupon)) {
            case 0:
                double nominal = Instruments.zeroCouponBond().getNominal();
                if (sell) {
                    price = (float) (nominal * 0.9 + 0.5 * nominal * Algorithm.random.nextFloat());
                }
                else {
                    price = (float) (nominal * 0.7 - 0.4 * nominal * Algorithm.random.nextFloat());
                }
                break;
            case 1:
                nominal = Instruments.fixedCouponBond().getNominal();
                double fixed = Instruments.fixedCouponBond().getCouponInPercents().getMax() / 100.0d;
                if (sell) {
                    price = (float) (nominal + 8 * fixed * nominal * Algorithm.random.nextFloat());
                }
                else {
                    price = (float) (nominal + (Algorithm.random.nextBoolean() ? 1 : -1)
                            * 2 * fixed * nominal * Algorithm.random.nextFloat());
                }
                break;
            case 2:
                nominal = Instruments.floatingCouponBond().getNominal();
                Bond.Coupon floating = Instruments.fixedCouponBond().getCouponInPercents();
                if (sell) {
                    price = (float) (nominal + 2 * floating.getMax() / 100.0f * nominal * Algorithm.random.nextFloat());
                }
                else {
                    price = (float) (nominal + (Algorithm.random.nextBoolean() ? 1 : -1)
                    * 2 * floating.getMin() / 100.0f * nominal * Algorithm.random.nextFloat());
                }
                break;
            default:
                price = sell ? 1000.0f : 50.0f;
        }
        genesMap.put("price", price);

        //quantity
        float quantity = (float)Algorithm.random.nextInt(40);
        genesMap.put("quantity", quantity);
    }

    public float get(String key) {
        return genesMap.get(key);
    }

    public void put(String key, float value) {
        this.genesMap.put(key, value);
    }

    public String getId() {
        return id;
    }

    public float getFitness() {
        if (fitness == null) {
            fitness = FitnessCalc.calcIndividual(this);
        }
        return fitness;
    }


    public void run(ExchangeEvent event, Broker broker) {
        OrderBuilder orderBuilder;
        if (genesMap.get("type") < 0.5) {
            float coupon = genesMap.get("coupon");
            if (coupon >= 0.0f && coupon < 1.0f) {
                orderBuilder = OrderBuilder.sell(Instruments.zeroCouponBond());
            }
            else if (coupon >= 1.0f && coupon < 2.0f) {
                orderBuilder = OrderBuilder.sell(Instruments.fixedCouponBond());
            }
            else {
                orderBuilder = OrderBuilder.sell(Instruments.floatingCouponBond());
            }
        }
        else {
            float coupon = genesMap.get("coupon");
            if (coupon >= 0.0f && coupon < 1.0f) {
                orderBuilder = OrderBuilder.buy(Instruments.zeroCouponBond());
            }
            else if (coupon >= 1.0f && coupon < 2.0f) {
                orderBuilder = OrderBuilder.buy(Instruments.fixedCouponBond());
            }
            else {
                orderBuilder = OrderBuilder.buy(Instruments.floatingCouponBond());
            }
        }
        orderBuilder = orderBuilder.withPrice(genesMap.get("price"));
        int quantity = Math.round(genesMap.get("quantity"));
        orderBuilder = orderBuilder.withQuantity(quantity);
        Order order = orderBuilder.order();
        this.id = order.getOrderId();
        FitnessCalc.addMyOrderId(order.getOrderId());
        broker.addOrder(order);
    }

}
