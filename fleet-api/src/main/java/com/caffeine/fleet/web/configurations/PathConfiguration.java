package com.caffeine.fleet.web.configurations;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Author: mukthar.m@myntra.com
 * Date: 2018-01-08
 */
public class PathConfiguration  {
    @Valid
    @NotNull
    private String host;

    public PathConfiguration() {
    }

    public void sethost(String host) {
        this.host = host;
    }

    public String gethost() {
        return this.host;
    }
}
