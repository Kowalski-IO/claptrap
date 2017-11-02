package io.kowalski.claptrap.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email implements Serializable, Comparable<Email> {

    private static final long serialVersionUID = 509542480910399471L;

    private UUID id;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime received;

    private String environment;

    private Header header;
    private Body body;

    public Email(String environment) {
        id = UUID.randomUUID();
        received = LocalDateTime.now();
        this.environment = environment;
    }

    @Override
    public int compareTo(final Email email) {
        return email.received.compareTo(received);
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o instanceof Email) {
            final Email email = (Email) o;
            return id.equals(email.getId());
        } else {
            return false;
        }
    }

}
