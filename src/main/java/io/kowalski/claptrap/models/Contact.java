package io.kowalski.claptrap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact implements Serializable, Comparable<Contact> {

    private static final long serialVersionUID = 7421253893867365076L;

    private String email;
    private String name;

    @JsonIgnore
    private transient ContactType type;

    public Contact(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(Contact c) {
        return email.compareTo(c.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Contact && email.equalsIgnoreCase(((Contact) o).getEmail());
    }

    public static Optional<Contact> of(Address address, ContactType type) {
        if (address instanceof InternetAddress) {
            Contact contact = new Contact();
            contact.setEmail(((InternetAddress) address).getAddress());
            contact.setName(((InternetAddress) address).getPersonal());
            contact.setType(type);

            return Optional.of(contact);
        }
        return Optional.empty();
    }

}
