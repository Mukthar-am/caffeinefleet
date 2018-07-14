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

    public String handle(FleetRequest fleetRequest) {
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
            deliveryExecDetails = deliveryExecsListing.get(deId);
            inDeList = true;
        }

        macros = getDeMacros(deliveryExecDetails);
        LOG.debug("Macros: " + macros.toString());

        if (inDeList)
            deliveryExecDetails = String.format("(%s,%s):%s:null", lat, lon, macros.Cd);

        // If exists, update the Cp - present lat-lon if not it will add the obtained lat-lon
        deliveryExecsListing.put(deId, deliveryExecDetails);

        // insert if -> not in deListing or Cn is still not set or null
        RMap<Integer, String> processQ = redis.getProcessQ();

        if ( ( !inDeList && state == 0 ) || (macros.Cn == null && state >= 50) )
            processQ.put(deId, deliveryExecDetails);

        return macros.Cn;
    }


    public class DeMacros {
        String Cp, Cd, Cn = null;

        public String toString() {
            return String.format("Cp: %s, Cd: %s, Cn: %s", Cp, Cd,  Cn);
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
