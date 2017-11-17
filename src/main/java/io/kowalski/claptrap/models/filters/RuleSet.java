package io.kowalski.claptrap.models.filters;

import io.kowalski.claptrap.models.filters.enums.BooleanOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class RuleSet implements FilterPart {

    private BooleanOperator operator;
    private Collection<FilterPart> rules;

}
