package com.caffeine.fleet.web.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.logging.LoggingFactory;
import io.dropwizard.metrics.MetricsFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import jersey.repackaged.com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Author: Mukthar
 */
public class Configuration extends io.dropwizard.Configuration {
    @Valid
    @NotNull
    private ServerFactory server = new DefaultServerFactory();

    @Valid
    @Nullable
    private LoggingFactory logging;

    @Valid
    @NotNull
    private MetricsFactory metrics = new MetricsFactory();

    /**
     * Returns the server-specific section of the configuration file.
     *
     * @return server-specific configuration parameters
     */
    @JsonProperty("server")
    public ServerFactory getServerFactory() {
        return server;
    }

    /**
     * Sets the HTTP-specific section of the configuration file.
     */
    @JsonProperty("server")
    public void setServerFactory(ServerFactory factory) {
        this.server = factory;
    }

    /**
     * Returns the logging-specific section of the configuration file.
     *
     * @return logging-specific configuration parameters
     */
    @JsonProperty("logging")
    public synchronized LoggingFactory getLoggingFactory() {
        if (logging == null) {
            // Lazy init to avoid a hard dependency to logback
            logging = new DefaultLoggingFactory();
        }
        return logging;
    }

    /**
     * Sets the logging-specific section of the configuration file.
     */
    @JsonProperty("logging")
    public synchronized void setLoggingFactory(LoggingFactory factory) {
        this.logging = factory;
    }

    @JsonProperty("metrics")
    public MetricsFactory getMetricsFactory() {
        return metrics;
    }

    @JsonProperty("metrics")
    public void setMetricsFactory(MetricsFactory metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("server", server)
                .add("logging", logging)
                .add("metrics", metrics)
                .toString();
    }
}