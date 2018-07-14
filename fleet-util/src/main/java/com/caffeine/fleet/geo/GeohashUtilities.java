package com.caffeine.fleet.geo;

import com.github.davidmoten.geo.LatLong;

import static com.github.davidmoten.geo.GeoHash.decodeHash;
import static com.github.davidmoten.geo.GeoHash.encodeHash;

public class GeohashUtilities {

    static final int HASHLENGTH = 6;

    public String encodeGeohash(String latitude, String longitude) {
        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);
        return encodeGeohash(lat, lon);
    }

    public String encodeGeohash(Double latitude, Double longitude) {
        return encodeHash(latitude, longitude, HASHLENGTH);
    }


    public LatLong decodeGeohash(String hash) {
        return decodeHash(hash);
    }

    public double getDistance(String hash1, String hash2) {
        LatLong pt1 = decodeGeohash(hash1);
        LatLong pt2 = decodeGeohash(hash2);

        return getDistance(pt1, pt2);
    }

    public double getDistance(LatLong pt1, LatLong pt2) {
        double dist =  Math.sqrt(Math.pow(pt2.getLat()-pt1.getLat(), 2) +
                Math.pow(pt2.getLon()-pt1.getLon(), 2));

        if(dist < 10) {
            if (dist * 10 < 1) {
                dist = 100 * dist;
            } else {
                dist = 10 * dist;
            }
        }

        return dist;
    }

}
