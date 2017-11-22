package io.kowalski.claptrap.models.filters;

import io.kowalski.claptrap.models.filters.enums.RuleOperator;
import io.kowalski.claptrap.models.filters.enums.RuleTarget;
import lombok.Getter;
import lombok.Setter;
import org.jooq.Field;

import java.util.List;

@Getter
@Setter
public class Rule implements FilterPart {

    private RuleTarget target;
    private RuleOperator operator;
    private List<Object> parameters;

    @SuppressWarnings("unchecked")
    public <T> Field<T> getField() {
        return target.getField();
    }

    @SuppressWarnings("unchecked")
    public Object getParameter(int index) {
        return parameters.get(index);
    }

}
