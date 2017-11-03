package io.kowalski.claptrap.services;

import io.kowalski.claptrap.models.*;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.kowalski.claptrap.models.jooq.Tables.*;

public class StorageService {

    @Inject
    private DSLContext dsl;

    public void storeEmail(Email email) {
        dsl.transaction(tr -> {
            tr.dsl().insertInto(EMAIL)
                    .set(EMAIL.ID, email.getId())
                    .set(EMAIL.RECEIVED, email.getReceived())
                    .set(EMAIL.ENVIRONMENT, email.getEnvironment())
                    .execute();

            tr.dsl().insertInto(HEADER)
                    .set(HEADER.ID, UUID.randomUUID())
                    .set(HEADER.EMAIL_ID, email.getId())
                    .set(HEADER.MESSAGE_ID, email.getHeader().getMessageID())
                    .set(HEADER.DATE, email.getHeader().getDate())
                    .set(HEADER.SUBJECT, email.getHeader().getSubject())
                    .set(HEADER.CONTENT_TYPE, email.getHeader().getContentType())
                    .execute();

            tr.dsl().insertInto(BODY)
                    .set(BODY.ID, UUID.randomUUID())
                    .set(BODY.EMAIL_ID, email.getId())
                    .set(BODY.PLAIN_TEXT, email.getBody().getPlainText())
                    .set(BODY.HTML, email.getBody().getHtml())
                    .execute();

            List<Query> contactInserts = email.getHeader().getAllContacts()
                    .parallelStream()
                    .map(c -> tr.dsl().insertInto(CONTACT)
                            .set(CONTACT.ID, UUID.randomUUID())
                            .set(CONTACT.EMAIL_ID, email.getId())
                            .set(CONTACT.EMAIL, c.getEmail())
                            .set(CONTACT.PERSONAL, c.getName())
                            .set(CONTACT.TYPE, c.getType().name()))
                    .collect(Collectors.toList());

            List<Query> attachmentInserts = email.getBody().getAttachments()
                    .parallelStream()
                    .map(a -> tr.dsl().insertInto(ATTACHMENT)
                            .set(ATTACHMENT.ID, UUID.randomUUID())
                            .set(ATTACHMENT.EMAIL_ID, email.getId())
                            .set(ATTACHMENT.FILENAME, a.getFilename())
                            .set(ATTACHMENT.CONTENT_TYPE, a.getContentType()))
                    .collect(Collectors.toList());

            tr.dsl().batch(contactInserts).execute();
            tr.dsl().batch(attachmentInserts).execute();

        });
    }

    public void deleteEmail(UUID id) {
        dsl.delete(EMAIL)
                .where(EMAIL.ID.eq(id))
                .execute();
    }

    public void deleteAllEmails() {
        dsl.delete(EMAIL).execute();
    }

    public List<Contact> allContacts() {
        return dsl.selectDistinct(CONTACT.EMAIL)
                .select(CONTACT.PERSONAL)
                .from(CONTACT)
                .orderBy(CONTACT.EMAIL.asc())
                .fetchStream()
                .map(r -> {
                    Contact temp = new Contact();
                    temp.setEmail(r.get(CONTACT.EMAIL));
                    temp.setName(r.get(CONTACT.PERSONAL));
                    return temp;
                })
                .collect(Collectors.toList());
    }

    public List<String> allEnvironments() {
        return dsl.selectDistinct(EMAIL.ENVIRONMENT)
                .from(EMAIL)
                .orderBy(EMAIL.ENVIRONMENT.asc())
                .fetch(EMAIL.ENVIRONMENT);
    }

    public String plainBodyForId(UUID id) {
        return dsl.select(BODY.PLAIN_TEXT)
                .from(EMAIL)
                .innerJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .where(EMAIL.ID.eq(id))
                .fetchOne().value1();
    }

    public String htmlBodyForId(UUID id) {
        return dsl.select(BODY.HTML)
                .from(EMAIL)
                .innerJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .where(EMAIL.ID.eq(id))
                .fetchOne().value1();
    }

    public List<Email> retrieveForCriteria(List<String> environment, List<String> to, List<String> cc, List<String> bcc,
                                           List<String> from, List<String> sender, List<String> replyTo,
                                           List<String> subject, List<String> body, PredicateMode mode) {
        if (mode == null) {
            mode = PredicateMode.OR;
        }

        Condition condition = null;

        if (!environment.isEmpty()) {
            condition = EMAIL.ENVIRONMENT.in(environment);
        }

        condition = contactFilter(condition, to, ContactType.TO, mode);
        condition = contactFilter(condition, cc, ContactType.CC, mode);
        condition = contactFilter(condition, bcc, ContactType.BCC, mode);
        condition = contactFilter(condition, from, ContactType.FROM, mode);
        condition = contactFilter(condition, sender, ContactType.SENDER, mode);
        condition = contactFilter(condition, replyTo, ContactType.REPLY_TO, mode);

        return dsl.select()
                .from(EMAIL)
                .innerJoin(HEADER).on(EMAIL.ID.eq(HEADER.EMAIL_ID))
                .innerJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .where(condition)
                .fetchStream()
                .map(r -> {
                    final Email e = new Email();
                    final Header h = new Header();
                    final Body b = new Body();

                    e.setId(r.get(EMAIL.ID));
                    e.setEnvironment(r.get(EMAIL.ENVIRONMENT));
                    e.setReceived(r.get(EMAIL.RECEIVED));
                    e.setHeader(h);
                    e.setBody(b);

                    h.setMessageID(r.get(HEADER.MESSAGE_ID));
                    h.setDate(r.get(HEADER.DATE));
                    h.setSubject(r.get(HEADER.SUBJECT));
                    h.setContentType(r.get(HEADER.CONTENT_TYPE));

                    b.setPlainText(r.get(BODY.PLAIN_TEXT));
                    b.setHtml(r.get(BODY.HTML));

                    dsl.select()
                            .from(CONTACT)
                            .where(CONTACT.EMAIL_ID.eq(e.getId()))
                            .orderBy(CONTACT.EMAIL)
                            .fetchStream()
                            .forEach(contactRecord -> {
                                Contact temp = new Contact();
                                temp.setEmail(contactRecord.get(CONTACT.EMAIL));
                                temp.setName(contactRecord.get(CONTACT.PERSONAL));
                                temp.setType(ContactType.valueOf(contactRecord.get(CONTACT.TYPE)));

                                switch (temp.getType()) {
                                    case TO:
                                        h.getTo().add(temp);
                                        break;
                                    case CC:
                                        h.getCc().add(temp);
                                        break;
                                    case BCC:
                                        h.getBcc().add(temp);
                                        break;
                                    case FROM:
                                        h.setFrom(temp);
                                        break;
                                    case SENDER:
                                        h.setSender(temp);
                                        break;
                                    case REPLY_TO:
                                        h.setReplyTo(temp);
                                        break;
                                }
                            });

                    b.setAttachments(dsl.select()
                            .from(ATTACHMENT)
                            .where(ATTACHMENT.EMAIL_ID.eq(e.getId()))
                            .fetchStream()
                            .map(attachmentRecord -> {
                                Attachment temp = new Attachment();
                                temp.setId(attachmentRecord.get(ATTACHMENT.ID));
                                temp.setContentType(attachmentRecord.get(ATTACHMENT.CONTENT_TYPE));
                                temp.setFilename(attachmentRecord.get(ATTACHMENT.FILENAME));
                                return temp;
                            })
                            .collect(Collectors.toList()));

                    return e;
                })
                .collect(Collectors.toList());
    }

    private Condition contactFilter(Condition condition, List<String> contacts, ContactType type, PredicateMode mode) {
        if (contacts == null || contacts.isEmpty()) {
            return condition;
        }

        Condition temp = EMAIL.ID.in(dsl.select(CONTACT.EMAIL_ID)
                .from(CONTACT)
                .where(CONTACT.TYPE.eq(type.name()))
                .and(CONTACT.EMAIL.in(contacts)));

        switch (mode) {
            case OR:
            default:
                return condition != null ? condition.or(temp) : temp;
            case AND:
                return condition != null ? condition.and(temp) : temp;
        }
    }

}
