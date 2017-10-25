package io.kowalski.claptrap.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attachment implements Serializable {

    private static final long serialVersionUID = -8622304980107603160L;

    private UUID id;
    private String filename;
    private String contentType;

    public Attachment(String filename, String contentType) {
        id = UUID.randomUUID();
        this.filename = filename;
        this.contentType = contentType;
    }

}
