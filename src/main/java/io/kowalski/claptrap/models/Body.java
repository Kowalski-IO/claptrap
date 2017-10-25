package io.kowalski.claptrap.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Body implements Serializable {

    private static final long serialVersionUID = -333452522469089287L;

    private Collection<Attachment> attachments;
    private String html;
    private String plainText;

    public Body() {
        attachments = new ArrayList<>();
    }

}
