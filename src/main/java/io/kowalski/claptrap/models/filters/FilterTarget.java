package io.kowalski.claptrap.models.filters;

import io.kowalski.claptrap.models.ContactType;
import io.kowalski.claptrap.models.jooq.Tables;
import io.kowalski.jqb2jooq.RuleTarget;
import org.jooq.Condition;
import org.jooq.Field;

import static io.kowalski.claptrap.models.jooq.Tables.*;
import static io.kowalski.claptrap.models.jooq.tables.Contact.CONTACT;
import static org.jooq.impl.DSL.field;

public enum FilterTarget implements RuleTarget {

    TO(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.TO.name())),
    CC(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.CC.name())),
    BCC(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.BCC.name())),

    SENDER(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.SENDER.name())),
    FROM(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.FROM.name())),
    REPLY_TO(CONTACT.EMAIL, CONTACT.TYPE.eq(ContactType.REPLY_TO.name())),

    SUBJECT(HEADER.SUBJECT),

    PLAIN_BODY(BODY.PLAIN_TEXT),
    HTML_BODY(BODY.HTML),

    ATTACHMENT(Tables.ATTACHMENT.FILENAME),
    RECEIVED_ON(EMAIL.RECEIVED);

    private final Field field;
    private final Condition[] implicitConditions;

    FilterTarget(Field field, Condition... implicitConditions) {
        this.field = field;
        this.implicitConditions = implicitConditions;
    }

    @Override
    public FilterTarget parse(String value) {
        return FilterTarget.valueOf(value);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Condition[] getImplicitConditions() {
        return implicitConditions;
    }

}
