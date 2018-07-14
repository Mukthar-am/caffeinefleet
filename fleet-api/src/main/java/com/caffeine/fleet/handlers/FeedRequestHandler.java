package com.caffeine.fleet.handlers;

import com.caffeine.fleet.cache.Redis;
import com.caffeine.fleet.web.representations.FleetRequest;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class FeedRequestHandler {
    private final Logger LOG = LoggerFactory.getLogger(FeedRequestHandler.class);

    public FeedRequestHandler handle(FleetRequest fleetRequest) {
        LOG.info(fleetRequest.toString());

        int deId = (int) fleetRequest.getde_id();
        String lat = fleetRequest.getlat();
        String lon = fleetRequest.getlon();
        int state = fleetRequest.getstate();

        /** ToDo: Remove this hard coding */
        String positioning = String.format("(%s,%s),null,null", lat, lon);
        LOG.info("{} -> {}. State: {}", deId, positioning, state);


        Redis redis = Redis.getInstance();

        /** upsert delivery exec map */
        RMap<Integer, String> deliveryExecs = redis.getDeliveryExecs();

        /** If found, retrive */
        // deliveryExecDetails- format: Cp, Cd, Cn
        String deliveryExecDetails = String.format("(%s,%s):null:null", lat, lon);
        boolean inDeList = false;


        DeMacros macros;
        if (deliveryExecs.containsKey(deId)) {
            deliveryExecDetails = deliveryExecs.get(deId);

            // ToDo: update the Cp
            macros = getDeMacros(deliveryExecDetails);
            LOG.info("Macros: " + macros.toString());

            inDeList = true;
        }


//        // insert either as first time entry or modified entry
//        deliveryExecs.put(deId, deliveryExecDetails);
//
//
//
//
//
//        /** put */
//        if (!inDeList && state > 50) {
//            // insert
//        }
//        deliveryExecs.put(deId, deliveryExecDetails);
//        if (macros.Cn == null && state > 50) {
//
//        }


//
//
////        if ()
////        RMap<Integer, String> processQ = redis.getProcessQ();
//
//
//        deliveryExecs.put(2, positioning);
//        deliveryExecs.put(3, "1,(l1,l2),(l11, l22)");
//
////        processQ.put(1, "1,(l1,l2),null");
////        processQ.put(2, "1,(l1,l2),null");
//
//
//
//        System.out.println("DeList:- " + redis.getDeliveryExecs().get(1));
//        System.out.println("ProcessQ: " + redis.getProcessQ().get(2));

        

        return this;
    }

    public class DeMacros {
        String Cp, Cd, Cn = null;

        public String toString() {
            return Cp + ":" + Cd + ":" + Cn;
        }
    }

    private DeMacros getDeMacros(String input) {
        String[] splits = input.split(":");
        DeMacros macros = new DeMacros();
        macros.Cp = splits[0];
        macros.Cd = splits[1];
        macros.Cn = splits[2];

        return macros;
    }
}
