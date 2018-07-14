package com.caffeine.fleet.web.representations;

import org.hibernate.validator.constraints.NotBlank;

/**
 * {
 *     "order_id": 1,
 *     "restaurant_id": 1,
 *     "restaurant_lat_lon": "(-123.1231231, 12.123123)",
 *     "destination_lat_lon": "(-123.1231231, 12.123123)",
 *     "state": 0
 * }
 */
public class OrderRequest {
    private long order_id;
    private int restaurant_id;

    @NotBlank
    private String restaurant_lat_lon;

    @NotBlank
    private String destination_lat_lon;

    private int state;

    public OrderRequest() {
    }

    public OrderRequest(long order_id,
                        int restaurant_id,
                        String restaurant_lat_lon,
                        String destination_lat_lon,
                        int state) {
        this.order_id = order_id;
        this.restaurant_id = restaurant_id;
        this.restaurant_lat_lon = restaurant_lat_lon;
        this.destination_lat_lon = destination_lat_lon;
        this.state = state;
    }


    /**
     * setter & getters
     */
    public void setorder_id(long order_id) {
        this.order_id = order_id;
    }
    public long getorder_id() {
        return this.order_id;
    }

    public void setrestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
    public int getrestaurant_id() {
        return this.restaurant_id;
    }

    public void setrestaurant_lat_lon(String restaurant_lat_lon) { this.restaurant_lat_lon = restaurant_lat_lon; }
    public String getrestaurant_lat_lon() {
        return this.restaurant_lat_lon;
    }

    public void setdestination_lat_lon(String destination_lat_lon) { this.destination_lat_lon = destination_lat_lon; }
    public String getdestination_lat_lon() {
        return this.destination_lat_lon;
    }

    public void setstate(int state) {
        this.state = state;
    }
    public int getstate() { return this.state; }

    public String toString() {
        return "[OrderId=" + this.order_id+ ", " +
                "RestaurantId=" + this.restaurant_id + ", " +
                "Restaurant, Lat-Lon=" + this.restaurant_lat_lon + ", " +
                "Destination, Lat-Lon=" + this.destination_lat_lon+ ", " +
                "state=" + this.state + "]";
    }
}
