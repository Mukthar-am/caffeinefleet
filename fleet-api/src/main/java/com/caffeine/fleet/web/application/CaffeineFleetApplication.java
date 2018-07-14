package com.caffeine.fleet.web.application;

import com.caffeine.fleet.web.controller.CaffeineFleetRestController;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wall-feed application initializer
 */
public class CaffeineFleetApplication extends Application<Configuration> {
    private static final Logger LOG = LoggerFactory.getLogger(CaffeineFleetApplication.class);

    public static void main(String[] args) throws Exception {
        new CaffeineFleetApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration conf, Environment environment) {
        LOG.info("=== Starting up wallfeed service endpoint ===");
        environment.jersey().register(new CaffeineFleetRestController(environment.getValidator(), conf   ));
    }
}

