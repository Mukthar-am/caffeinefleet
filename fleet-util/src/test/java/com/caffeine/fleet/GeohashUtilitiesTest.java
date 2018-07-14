package com.caffeine.fleet;

import static org.junit.Assert.assertEquals;

import com.caffeine.fleet.geo.GeohashUtilities;
import com.github.davidmoten.geo.LatLong;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class GeohashUtilitiesTest {

    GeohashUtilities geo = new GeohashUtilities();

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testWhiteHouseHashEncodeUsingLatLongObject() {
        assertEquals("dqcjqc", geo.encodeGeohash(38.89710201881826,
                -77.03669792041183));

        System.out.println(geo.encodeGeohash(12.944082,
                77.595824));

        System.out.println(geo.encodeGeohash(12.934924,
                77.611309));

        System.out.println(geo.encodeGeohash(12.892904,
                77.642214));
    }

    @Test
    public void testWhiteHouseHashDecode() {
        LatLong point = geo.decodeGeohash("dqcjqcp");
        assertEquals(String.valueOf(point.getLat()).substring(0, 6), "38.897");
        assertEquals(String.valueOf(point.getLon()).substring(0, 7), "-77.036");
    }

    @Test
    public void testFromGeoHashDotOrg() {
        assertEquals("6gkzwgj", geo.encodeGeohash(-25.382708, -49.265506));
    }


    @Test
    public void testDistanceFromGeoHashDotOrg() {
        System.out.println(geo.getDistance("tdr1ts", "tdr1qg"));
    }

}
