package com.caffeine.fleet.cache;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Redis {
    private Config CONFIG = null;
    private static Redis instance = null;

    private String REDIS_HOST = "127.0.0.1";
    private int REDIS_PORT = 6379;

    private RMap<Integer, String> DELIVERY_EXEC_LIST;
    private RMap<Integer, String> PROCESS_Q;
    private RMap<Long, String> ORDERS_Q;

    /**
     * ToDo: use setter and getters, later
     */
    public RedissonClient REDISSION_CLIENT = null;

    private Redis() {
    }

    public static Redis getInstance() {
        if (instance == null)
            instance = new Redis();

        instance.CONFIG = new Config();
        instance.CONFIG
                .useSingleServer()
                .setAddress("redis://" +
                        instance.REDIS_HOST +
                        ":" +
                        instance.REDIS_PORT);

        instance.REDISSION_CLIENT = Redisson.create(instance.CONFIG);

        instance.DELIVERY_EXEC_LIST = instance.REDISSION_CLIENT.getMap("DeliveryExec");
        instance.PROCESS_Q = instance.REDISSION_CLIENT.getMap("ProcessQ");
        instance.ORDERS_Q = instance.REDISSION_CLIENT.getMap("OrdersQ");

        return instance;
    }

    public RedissonClient getRedissionClient() {
        return this.REDISSION_CLIENT;
    }

    public RMap<Integer, String> getDeliveryExecs() {
        return this.DELIVERY_EXEC_LIST;
    }

    public RMap<Integer, String> getProcessQ() {
        return this.PROCESS_Q;
    }

    public RMap<Long, String> getOrderQ() {
        return this.ORDERS_Q;
    }
}
