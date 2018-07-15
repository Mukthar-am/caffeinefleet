package com.caffeine.fleet.handlers;

import com.caffeine.fleet.cache.Redis;
import com.caffeine.fleet.web.representations.FleetRequest;
import com.caffeine.fleet.web.representations.OrderRequest;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 */
public class FeedRequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(FeedRequestHandler.class);

    /**
     * method to handle incoming order requests
     * {
     *     "order_id": 1,
     *     "restaurant_id": 1,
     *     "restaurant_lat_lon": "(-123.1231231,12.123123)",
     *     "destination_lat_lon": "(-123.1231231,12.123123)",
     *     "state": 0
     * }
     * @param orderRequest
     */
    public void handleIncomingOrders(OrderRequest orderRequest) {
        String modulated = modulateOrderData(orderRequest);
        LOG.info(modulated);

        RMap<Long, String> ordersQ = Redis.getInstance().getOrderQ();
        ordersQ.put(orderRequest.getorder_id(), modulated);
    }


    /**
     * incoming order request object modulator
     * @param orderRequest - OrderRequest
     * @return - modulated string
     */
    private String modulateOrderData(OrderRequest orderRequest) {
        //final char ctrlA = '\u0001';
        final char ctrlA = ':';
        return String.format("%s%s%s%s%s%s%s%s%s",
                orderRequest.getorder_id(),
                ctrlA,
                orderRequest.getrestaurant_id(),
                ctrlA,
                orderRequest.getrestaurant_lat_lon(),
                ctrlA,
                orderRequest.getdestination_lat_lon(),
                ctrlA,
                orderRequest.getstate()
        );

    }


    /**
     * {
     *     "de_id": 12,
     *     "lat": -56.727263722686104,
     *     "lon": 37.68402408344105,
     *     "state": 75
     * }
     *
     * method to handle fleet requests
     * @param fleetRequest - FleetRequest object as per the dropwizard pojo
     * @return  - return Cn json string
     */
    public String handleFleetRequest(FleetRequest fleetRequest) {
        LOG.info(fleetRequest.toString());

        int deId = (int) fleetRequest.getde_id();
        String lat = fleetRequest.getlat();
        String lon = fleetRequest.getlon();
        int state = fleetRequest.getstate();

        // get redis instance - singleton
        Redis redis = Redis.getInstance();

        /** upsert delivery exec map */
        RMap<Integer, String> deliveryExecsListing = redis.getDeliveryExecs();

        // deliveryExecDetails- format: Cp, Cd, Cn
        String deliveryExecDetails = String.format("(%s,%s):null:null", lat, lon);
        LOG.info("{} -> {}. State: {}", deId, deliveryExecDetails, state);
        boolean inDeList = false;

        // if found, get details and set the flag
        DeMacros macros;
        if (deliveryExecsListing.containsKey(deId)) {
            LOG.info("Contains the de # " + deId);
            deliveryExecDetails = deliveryExecsListing.get(deId);
            inDeList = true;
        }

        macros = getDeMacros(deliveryExecDetails);
        LOG.info("Macros: " + macros.toString());

        if (inDeList) {
            LOG.info("It exists and hence update the lat-lon positions");
            deliveryExecDetails = String.format("(%s,%s):%s:null", lat, lon, macros.Cd);
        }

        // If exists, update the Cp - present lat-lon if not it will add the obtained lat-lon
        deliveryExecsListing.put(deId, deliveryExecDetails);

        // insert if -> not in deListing or Cn is still not set or null
        RMap<Integer, String> processQ = redis.getProcessQ();

        LOG.info("{} -> {}. State: {}", deId, deliveryExecDetails, state);
        if ((!inDeList && state == 0) || (macros.Cn.equalsIgnoreCase("null") && state >= 50)) {
            LOG.info("Ingesting into processQ");
            processQ.put(deId, deliveryExecDetails);
        }

        LOG.info("C-next: " + macros.Cn);
        return String.format("{\"Cn\": \"%s\"}", macros.Cn);
    }


    /**
     * DeMacros object -
     * ToDo: Class members are kept default rather than having setters and getters for then due to time constraint on implementation
     *
     * Description:
     *  DeMacros - for incoming request
     */
    public class DeMacros {
        String Cp, Cd, Cn = null;

        public String toString() {
            return String.format("Cp: %s, Cd: %s, Cn: %s", Cp, Cd, Cn);
        }
    }


    /**
     * get macro items
     * @param input - complete input string
     * @return - DeMacros object
     */
    private DeMacros getDeMacros(String input) {
        String[] splits = input.split(":");
        DeMacros macros = new DeMacros();
        macros.Cp = splits[0];
        macros.Cd = splits[1];
        macros.Cn = splits[2];

        return macros;
    }


    /**
     * Method used for testing purpose. Testing the redis cached tables
     * @param type - input type from a range of de | orders | process
     */
    public static String queryHandlers(String type) {
        LOG.info("Printing data for - {}", type);
        // get redis instance - singleton
        Redis redis = Redis.getInstance();

        StringBuilder sb = new StringBuilder();

        /** upsert delivery exec map */
        if (type.equalsIgnoreCase("de")) {
            RMap<Integer, String> deliveryExecsListing = redis.getDeliveryExecs();
            Set<Integer> deIds = deliveryExecsListing.keySet();
            for (Integer deId : deIds) {
                sb.append(String.format("%d -> %s\n", deId, deliveryExecsListing.get(deId)));
            }
        }

        /** upsert delivery exec map */
        if (type.equalsIgnoreCase("process")) {
            LOG.info("Fetching processQ");
            RMap<Integer, String> processQ = redis.getProcessQ();
            Set<Integer> procIds = processQ.keySet();
            for (Integer procId: procIds) {
                LOG.info("{} -> {}", procId, processQ.get(procId));
                sb.append(String.format("%d -> %s\n", procId, processQ.get(procId) ));
            }
        }


        /** upsert delivery exec map */
        if (type.equalsIgnoreCase("orders") || type.equalsIgnoreCase("order")) {
            RMap<Long, String> ordersQ = redis.getOrderQ();
            Set<Long> orderIds = ordersQ.keySet();
            for (Long orderId: orderIds) {
                LOG.info("{} -> {}", orderId, ordersQ.get(orderId));
                sb.append(String.format("%d -> %s\n", orderId, ordersQ.get(orderId)));
            }
        }

        LOG.info(sb.toString());
        return sb.toString();
    }


}
