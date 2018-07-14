package com.caffeine.fleet.web.controller;

import com.caffeine.fleet.handlers.FeedRequestHandler;
import com.caffeine.fleet.web.representations.FleetRequest;
import com.caffeine.fleet.web.templates.HealthCheckTemplate;
import io.dropwizard.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Author: mukthar.m@myntra.com
 * Date: 2017-12-27
 */


@Slf4j
@Path("/fleet")
@Produces(MediaType.APPLICATION_JSON)
public class CaffeineFleetRestController {
    final static Logger LOG = LoggerFactory.getLogger(CaffeineFleetRestController.class);
    private Configuration configs;
    private final Validator validator;

    public CaffeineFleetRestController(Validator validator, Configuration configs) {
        this.validator = validator;
        this.configs = configs;
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response healthCheck(Configuration configs) {
        LOG.info("Wallfeed is healthier as every before to serve you on data picking.");
        LOG.info("===== + = + = + = " + this.configs.toString());
        return Response.ok(HealthCheckTemplate.getHealthTemplate()).build();
    }


    @POST
    public Response requestPicker(FleetRequest fleetRequest) throws URISyntaxException {
        /** validation */
        Set<ConstraintViolation<FleetRequest>> violations = validator.validate(fleetRequest);

        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<FleetRequest> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }

            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        } else {
            FeedRequestHandler feedRequestHandler = new FeedRequestHandler();

            return Response
                    .ok(
                            feedRequestHandler.handle(fleetRequest)
                    ).build();
        }
    }

}