package com.caffeine.fleet.web.representations;

import org.hibernate.validator.constraints.NotBlank;

/**
 */
public class FleetRequest {
    private long de_id;

    @NotBlank
    private String lat;

    @NotBlank
    private String lon;

    private int state;

    public FleetRequest() {
    }

    public FleetRequest(long de_id,
                        String lat,
                        String lon,
                        int state) {
        this.de_id = de_id;
        this.lat = lat;
        this.lon = lon;
        this.state = state;
    }


    /**
     * setter & getters
     */
    public void setde_id(long deid) {
        this.de_id = deid;
    }
    public long getde_id() {
        return this.de_id;
    }

    public void setlat(String lat) {
        this.lat = lat;
    }
    public String getlat() {
        return this.lat;
    }

    public void setlon(String lon) { this.lon = lon; }
    public String getlon() {
        return this.lon;
    }

    public void setstate(int state) {
        this.state = state;
    }
    public int getstate() { return this.state; }


    public String toString() {
        return "[de-id=" + this.de_id + ", " +
                "lat=" + this.lat + ", " +
                "lon=" + this.lon + ", " +
                "state=" + this.state + "]";
    }
}
