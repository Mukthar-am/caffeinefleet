package com.caffeine.fleet.web.configurations;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Author: mukthar.m@myntra.com
 * Date: 2018-01-08
 */
public class ConfigPaths extends Configuration {
    @Valid
    @NotNull
    private PathConfiguration pathConfiguration;

    public ConfigPaths() {
    }

    public void setPathConfiguration(PathConfiguration pathConfiguration) {
        this.pathConfiguration = pathConfiguration;
    }

    public PathConfiguration getPathConfiguration() {
        return this.pathConfiguration;
    }
}