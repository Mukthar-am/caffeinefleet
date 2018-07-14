package com.caffeine.fleet;


import com.caffeine.fleet.cache.Redis;
import com.caffeine.fleet.web.representations.OrderRequest;
import org.redisson.api.RMap;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class FleetAPITests {

//    @Test
//    public void OrderEntryTests() {
//
//        RMap<Long, String> ordersQ = Redis.getInstance().getOrderQ();
//        ordersQ.put(orderRequest.getorder_id(), modulated);
//
//        String input = ""
//        Assert.assertEquals(true, true);
//    }
//
//
//    private String modulateOrderData(OrderRequest orderRequest) {
//        //final char ctrlA = '\u0001';
//        final char ctrlA = ':';
//        return String.format("%s%s%s%s%s%s%s%s%s",
//                orderRequest.getorder_id(),
//                ctrlA,
//                orderRequest.getrestaurant_id(),
//                ctrlA,
//                orderRequest.getrestaurant_lat_lon(),
//                ctrlA,
//                orderRequest.getdestination_lat_lon(),
//                ctrlA,
//                orderRequest.getstate()
//        );
//
//    }
}
