package io.kowalski.claptrap.services;

import io.kowalski.claptrap.models.filters.Filter;
import io.kowalski.claptrap.models.filters.FilterPart;
import io.kowalski.claptrap.models.filters.Rule;
import io.kowalski.claptrap.models.filters.RuleSet;
import io.kowalski.claptrap.models.filters.enums.BooleanOperator;
import io.kowalski.claptrap.models.filters.enums.RuleOperator;
import io.kowalski.claptrap.models.filters.enums.RuleTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FilterService {

    public static Filter parseJSON(final Map<String, Object> jsonFilter) {

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rules = (List<Map<String, Object>>) jsonFilter.get("rules");

        RuleSet outerRuleSet = new RuleSet();
        outerRuleSet.setOperator(BooleanOperator.valueOf(jsonFilter.get("condition").toString()));
        outerRuleSet.setRules(parseRaw(rules));

        return new Filter(outerRuleSet);
    }

    private static Collection<FilterPart> parseRaw(List<Map<String, Object>> rules) {
        List<FilterPart> filterParts = new ArrayList<>();

        rules.forEach(rule -> filterParts.add(rule.containsKey("condition") ? parseRawRuleSet(rule) : parseRawRule(rule)));

        return filterParts;
    }

    private static RuleSet parseRawRuleSet(final Map<String, Object> rawRuleSet) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rules = (List<Map<String, Object>>) rawRuleSet.get("rules");

        RuleSet ruleSet = new RuleSet();
        ruleSet.setOperator(BooleanOperator.valueOf(rawRuleSet.get("condition").toString()));
        ruleSet.setRules(parseRaw(rules));

        return ruleSet;
    }

    @SuppressWarnings("unchecked")
    private static Rule parseRawRule(final Map<String, Object> rawRule) {
        String rawOperator = rawRule.get("operator").toString().toUpperCase();
        String rawField = rawRule.get("field").toString().toUpperCase();
        Collection<Object> rawValues;

        if (rawRule.get("value") instanceof Collection) {
            rawValues = ((Collection) rawRule.get("value"));
        } else {
            rawValues = new ArrayList<>();
            rawValues.add(rawRule.get("value"));
        }

        Rule rule = new Rule();
        rule.setOperator(RuleOperator.valueOf(rawOperator));
        rule.setTarget(RuleTarget.valueOf(rawField));
        rule.setParameters(rawValues);
        return rule;
    }

}
