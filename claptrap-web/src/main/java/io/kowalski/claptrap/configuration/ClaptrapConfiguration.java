package io.kowalski.claptrap.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ClaptrapConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("smtpPort")
    private int smtpPort;

    public final int getSmtpPort() {
        return smtpPort;
    }

    public final void setSmtpPort(final int smtpPort) {
        this.smtpPort = smtpPort;
    }

}