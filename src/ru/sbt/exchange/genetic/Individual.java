package ru.sbt.exchange.genetic;

import ru.sbt.exchange.client.Broker;
import ru.sbt.exchange.domain.Direction;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.OrderBuilder;
import ru.sbt.exchange.domain.instrument.Bond;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mika on 05.12.16.
 */
public class Individual {

    private Map<String, Float> genesMap;
    private float fitness;

    public Individual() {
        this.genesMap = new HashMap<>();
    }

    public Individual(Order order) {
        this.genesMap = new HashMap<>();
        genesMap.put("price", (float)order.getPrice());
        genesMap.put("quantity", (float)order.getQuantity());
        float type;
        if (order.getInstrument().getName().equals("zeroCouponBond")) {
            type = 0.0f;
        }
        else if (order.getInstrument().getName().equals("fixedCouponBond")) {
            type = 1.0f;
        }
        else {
            type = 2.0f;
        }
        genesMap.put("coupon", type);
        if (order.getDirection() == Direction.SELL) {
            genesMap.put("type", 0.0f);
        }
        else {
            genesMap.put("type", 1.0f);
        }
    }


    public void generate() {
        float type = Algorithm.random.nextFloat();
        genesMap.put("type", type);
        //zero, floating or fixed
        float coupon = Algorithm.random.nextFloat() * 3;
        genesMap.put("coupon", coupon);
        //price
        double nominal = Instruments.fixedCouponBond().getNominal() ;
        genesMap.put("price", (float) (nominal + (Algorithm.random.nextBoolean() ? -1 : 1) * 0.6 * nominal));
        genesMap.put("quantity", (float) Algorithm.random.nextInt(500));
        /*System.out.println("AddOrder: price=" + genesMap.get("price") + " quantity=" + genesMap.get("quantity") +
                " direction=" + genesMap.get("type") + " coupon=" + genesMap.get("coupon"));*/
    }

    public float get(String key) {
        return genesMap.get(key);
    }

    public void put(String key, float value) {
        this.genesMap.put(key, value);
    }


    public void run(Broker broker) {

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
        broker.addOrder(order);
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

}
