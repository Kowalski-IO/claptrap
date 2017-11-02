package io.kowalski.claptrap.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Header implements Serializable {

    private static final long serialVersionUID = 4321489199074896362L;

    private String messageID;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private Contact from;

    private Contact replyTo;
    private Contact sender;

    private Set<Contact> to;
    private Set<Contact> cc;
    private Set<Contact> bcc;

    private String subject;

    private String contentType;

    public Header() {
        to = new HashSet<>();
        cc = new HashSet<>();
        bcc = new HashSet<>();
    }

    @JsonIgnore
    public Collection<Contact> getAllContacts() {
        Set<Contact> allContacts = new HashSet<>();
        allContacts.add(from);
        allContacts.add(replyTo);
        allContacts.add(sender);
        allContacts.addAll(to);
        allContacts.addAll(cc);
        allContacts.addAll(bcc);
        return allContacts;
    }

    public void setSender(Contact sender) {
        sender.setType(ContactType.SENDER);
        this.sender = sender;
    }

}
