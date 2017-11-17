package io.kowalski.claptrap.models.filters;

import io.kowalski.claptrap.models.filters.enums.RuleOperator;
import io.kowalski.claptrap.models.filters.enums.RuleTarget;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class Rule implements FilterPart {

    private RuleTarget target;
    private RuleOperator operator;
    private Collection<Object> parameters;

}
