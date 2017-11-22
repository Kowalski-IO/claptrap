package io.kowalski.claptrap.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ServerConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("smtpPort")
    private int smtpPort;

}
