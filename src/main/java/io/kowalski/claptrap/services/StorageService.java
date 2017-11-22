package io.kowalski.claptrap.services;

import io.kowalski.claptrap.models.*;
import io.kowalski.claptrap.models.filters.Filter;
import io.kowalski.claptrap.models.filters.FilterPart;
import io.kowalski.claptrap.models.filters.Rule;
import io.kowalski.claptrap.models.filters.RuleSet;
import io.kowalski.claptrap.models.filters.enums.BooleanOperator;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.kowalski.claptrap.models.jooq.Tables.*;

@Slf4j
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
                            .set(ATTACHMENT.ID, a.getId())
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
        try {
            dsl.delete(EMAIL).execute();
            Path rootPath = Paths.get("attachments");
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("Unable to delete all attachments while deleting all emails.", e);
        }
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

    public Attachment attachmentForId(UUID email, UUID attachment) {
        return dsl.select()
                .from(ATTACHMENT)
                .where(ATTACHMENT.ID.eq(attachment))
                .and(ATTACHMENT.EMAIL_ID.eq(email))
                .fetchOne()
                .map(r -> r.into(Attachment.class));
    }

    public File attachmentFileForId(UUID email, UUID attachment) {
        Path path = Paths.get("attachments/".concat(email.toString()).concat("/").concat(attachment.toString()));
        return path.toFile();
    }

    public Collection<Email> retrieveForEnvironment(List<String> environment) {
        Condition condition = null;

        if (!environment.isEmpty()) {
            condition = EMAIL.ENVIRONMENT.in(environment);
        }

        return dsl.select()
                .from(EMAIL)
                .innerJoin(HEADER).on(EMAIL.ID.eq(HEADER.EMAIL_ID))
                .innerJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .where(condition)
                .orderBy(EMAIL.RECEIVED.desc())
                .fetchStream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    public Collection<Email> retrieveForCriteria(List<String> environment, Condition filter) {
        Condition condition = null;

        if (!environment.isEmpty()) {
            condition = EMAIL.ENVIRONMENT.in(environment);
        }

        if (filter != null) {
            condition = condition != null ? condition.and(filter) : filter;
        }

        List<UUID> matches = dsl.selectDistinct(EMAIL.ID)
                .from(EMAIL)
                .leftJoin(HEADER).on(EMAIL.ID.eq(HEADER.EMAIL_ID))
                .leftJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .leftJoin(CONTACT).on(EMAIL.ID.eq(CONTACT.EMAIL_ID))
                .leftJoin(ATTACHMENT).on(EMAIL.ID.eq(ATTACHMENT.EMAIL_ID))
                .where(condition)
                .fetchStream()
                .map(r -> r.get(EMAIL.ID)).collect(Collectors.toList());

        return dsl.select()
                .from(EMAIL)
                .innerJoin(HEADER).on(EMAIL.ID.eq(HEADER.EMAIL_ID))
                .innerJoin(BODY).on(EMAIL.ID.eq(BODY.EMAIL_ID))
                .where(EMAIL.ID.in(matches))
                .orderBy(EMAIL.RECEIVED.desc())
                .fetchStream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private Email parse(Record r) {
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
    }

    public Condition convertFilter(Filter filter) {
        return parseRuleSet(filter.getRuleSet());
    }

    private Condition parseRuleSet(RuleSet ruleSet) {
        Condition condition = null;

        for (FilterPart filterPart : ruleSet.getRules()) {
            Condition parsed;

            if (filterPart instanceof RuleSet) {
                parsed = parseRuleSet((RuleSet) filterPart);
            } else {
                parsed = parseRule((Rule) filterPart);
            }

            condition = appendCondition(condition, parsed, ruleSet.getOperator());
        }

        return condition;
    }

    private Condition parseRule(Rule rule) {
        Condition condition;
        switch (rule.getOperator()) {
            case EQUAL:
                condition = rule.getField().eq(rule.getParameter(0));
                break;
            case NOT_EQUAL:
                condition = rule.getField().ne(rule.getParameter(0));
                break;
            case LESS:
                condition = rule.getField().lt(rule.getParameter(0));
                break;
            case LESS_OR_EQUAL:
                condition = rule.getField().le(rule.getParameter(0));
                break;
            case GREATER:
                condition = rule.getField().gt(rule.getParameter(0));
                break;
            case GREATER_OR_EQUAL:
                condition = rule.getField().ge(rule.getParameter(0));
                break;
            case IN:
                condition = rule.getField().in(rule.getParameters());
                break;
            case NOT_IN:
                condition = rule.getField().notIn(rule.getParameters());
                break;
            case CONTAINS:
                condition = rule.getField().contains(rule.getParameter(0));
                break;
            case DOES_NOT_CONTAIN:
                condition = rule.getField().notContains(rule.getParameter(0));
                break;
            case BETWEEN:
                condition = rule.getField().between(rule.getParameter(0), rule.getParameter(1));
                break;
            case NOT_BETWEEN:
                condition = rule.getField().notBetween(rule.getParameter(0), rule.getParameter(1));
                break;
            case IS_NULL:
                condition = rule.getField().isNull();
                break;
            case IS_NOT_NULL:
                condition = rule.getField().isNotNull();
                break;
            case IS_EMPTY:
                condition = rule.getField().length().eq(0);
                break;
            case IS_NOT_EMPTY:
                condition = rule.getField().length().gt(0);
                break;
            default:
                return null;
        }

        if (rule.getTarget().getImplicitConditions() != null && rule.getTarget().getImplicitConditions().length > 0) {
            for (Condition implicitCondition : rule.getTarget().getImplicitConditions()) {
                condition = condition.and(implicitCondition);
            }
        }

        return condition;
    }

    private Condition appendCondition(Condition initial, Condition addition, BooleanOperator operator) {
        if (initial == null) {
            return addition;
        }
        switch (operator) {
            case OR:
                return initial.or(addition);
            case AND:
                return initial.and(addition);
            case NA:
            default:
                return null;
        }
    }

}
