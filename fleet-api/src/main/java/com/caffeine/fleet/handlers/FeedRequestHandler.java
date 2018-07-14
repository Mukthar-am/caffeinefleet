package com.caffeine.fleet.handlers;

import com.caffeine.fleet.web.representations.FleetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class FeedRequestHandler {
    private final Logger LOG = LoggerFactory.getLogger(FeedRequestHandler.class);

    public FeedRequestHandler handle(FleetRequest fleetRequest) {
        LOG.info(fleetRequest.toString());
        return this;
    }
}
