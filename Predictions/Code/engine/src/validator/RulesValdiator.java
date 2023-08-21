package validator;

import exceptions.InvalidRangeException;
import exceptions.InvalidWhiteSpaceException;
import exceptions.UniqueNameException;
import resources.generated.*;

import java.util.List;
import java.util.stream.Collectors;

public class RulesValdiator {
    private static final String ROOT = "Rules validation";
    private static String SOURCE = ROOT;

    public static void ValidateRules(PRDRules rules) {
        validateUniqueRulesNames(rules);

        for (PRDRule rule : rules.getPRDRule()) {
            ValidateRule(rule);
        }
    }

    private static void validateUniqueRulesNames(PRDRules rules) {
        List<String> rulesNames = rules.getPRDRule()
                .stream()
                .map(PRDRule::getName)
                .collect(Collectors.toList());

        String firstDuplicate = Utils.FindFirstDuplicate(rulesNames);
        if (firstDuplicate != null) // no duplicates
            throw new UniqueNameException(firstDuplicate, SOURCE);
    }

    public static void ValidateRule(PRDRule rule) {
        SOURCE = ROOT + " " + rule.getName();
        if (Utils.isContainsSpace(rule.getName()))
            throw new InvalidWhiteSpaceException(rule.getName(), SOURCE);

        for (PRDAction action : rule.getPRDActions().getPRDAction()) {
            ActionValidator.ValidateAction(action, SOURCE);
        }
        if (rule.getPRDActivation() != null) {
            ValidateActivation(rule.getPRDActivation(), SOURCE);
        }
    }

    public static void ValidateActivation(PRDActivation activation, String source) {
        source += ", activation";
        if (activation.getTicks() != null && activation.getTicks() <= 0) {
            throw new InvalidRangeException("ticks", "1 - infinity", source);
        }
        if (activation.getProbability() != null && (activation.getProbability() < 0 || activation.getProbability() > 1)) {
            throw new InvalidRangeException("probability", "0 - 1", source);
        }
    }

}
