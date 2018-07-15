package com.caffeine.fleet.tests;


import com.caffeine.fleet.utils.general.JsonUtils;
import com.caffeine.fleet.utils.http.HttpClientHeaders;
import com.caffeine.fleet.utils.http.TouchHttpClient;
import com.caffeine.fleet.web.controller.CaffeineFleetRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class FleetAPITests {
    final static Logger LOG = LoggerFactory.getLogger(FleetAPITests.class);
    private final String DeAPI = "http://localhost:8082/fleet";
    private final String OrdersAPI = "http://localhost:8082/fleet/orders";
    private final String QueryAPI = "http://localhost:8082/fleet/query/";
    private final String HealthCheckAPI = "http://localhost:8082/fleet";
    private HttpClientHeaders httpClientHeaders = new HttpClientHeaders();

    {
        httpClientHeaders.addHeader("Content-Type", "application/json");
    }

    @Test
    public void HealthCheckTests() {
        httpClientHeaders.addHeader("Accept", "text/html"); // override
        LOG.info(httpClientHeaders.toString());

        TouchHttpClient httpClient =
                new TouchHttpClient()
                        .setHeaders(httpClientHeaders)
                        .setURL(HealthCheckAPI);

        try {
            httpClient.sendGetRequest();
            LOG.info(httpClient.toString());
        } catch (IOException e) {
            LOG.error("Exception", e);
        }
    }


    @Test
    public void QueryAPITests() {
        httpClientHeaders.removeHeaders("Content-Type");
        httpClientHeaders.removeHeaders("Accept");
        LOG.info(httpClientHeaders.toString());

        List<String> queryTypes = new ArrayList<>(Arrays.asList("orders", "de", "process"));

        for (String type : queryTypes) {
            String queryAPI = QueryAPI + type;
            System.out.println("Querying:- " + queryAPI);

            TouchHttpClient httpClient =
                    new TouchHttpClient()
                            .setHeaders(httpClientHeaders)
                            .setURL(QueryAPI + type);

            try {
                httpClient.sendGetRequest();
                LOG.info(httpClient.toString());
                LOG.info("\nFullResponse: " + httpClient.getResponseString());

                Thread.sleep(3000);
            } catch (Exception e) {
                LOG.error("Exception", e);
            }
        }
    }


    @Test
    public void PostDeListTest() {
        httpClientHeaders.addHeader("Accept", "application/json"); // override
        String postBody = "{\"de_id\":12,\"lat\":-56.727263722686104,\"lon\":37.68402408344105,\"state\":75}";

        LOG.info("Post body: " + JsonUtils.beautifyJson(postBody));
        LOG.info(httpClientHeaders.toString());

        TouchHttpClient httpClient = new TouchHttpClient();
        httpClient.setPostBody(postBody);
        try {
            httpClient.setHeaders(httpClientHeaders);
            httpClient.setURL(DeAPI);
            httpClient.sendPostRequest();

            System.out.println(httpClient.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void PostOrdersTest() {
        httpClientHeaders.addHeader("Accept", "application/json"); // override
        String postBody = "{\"order_id\":3,\"restaurant_id\":1,\"restaurant_lat_lon\":\"(-123.1231231,12.123123)\",\"destination_lat_lon\":\"(-123.1231231,12.123123)\",\"state\":0}";

        LOG.info("Post body: " + JsonUtils.beautifyJson(postBody));
        LOG.info(httpClientHeaders.toString());

        TouchHttpClient httpClient = new TouchHttpClient();
        httpClient.setPostBody(postBody);
        try {
            httpClient.setHeaders(httpClientHeaders);
            httpClient.setURL(OrdersAPI);
            httpClient.sendPostRequest();

            System.out.println(httpClient.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
