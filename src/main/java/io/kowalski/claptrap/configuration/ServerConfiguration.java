package io.kowalski.claptrap.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class ServerConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("smtpPort")
    private int smtpPort;

}
