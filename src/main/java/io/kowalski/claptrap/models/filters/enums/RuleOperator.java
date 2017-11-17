package io.kowalski.claptrap.models.filters.enums;

import lombok.Getter;

@Getter
public enum RuleOperator {

    EQUAL,
    NOT_EQUAL,

    LESS,
    LESS_OR_EQUAL,

    GREATER,
    GREATER_OR_EQUAL,

    IN,
    NOT_IN,

    BETWEEN,
    NOT_BETWEEN,

    IS_NULL,
    IS_NOT_NULL,

    IS_EMPTY,
    IS_NOT_EMPTY

}
