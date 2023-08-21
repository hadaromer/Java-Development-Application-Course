package validator;

import exceptions.UniqueNameException;
import resources.generated.PRDEnvProperty;
import resources.generated.PRDEvironment;

import java.util.List;
import java.util.stream.Collectors;

public class EnvironmentValidator {
    private static final String PRD_ENV_PROPERTY = PRDEnvProperty.class.getSimpleName();
    private static final String SOURCE = "Environment validation";
    public static void ValidateEnvironment(PRDEvironment environment) {
        validateUniquePRDNames(environment);

        for (PRDEnvProperty prdEnvProperty : environment.getPRDEnvProperty()) {
            ValidateEnvironmentProperty(prdEnvProperty);
        }
    }

    private static void validateUniquePRDNames(PRDEvironment environment) {
        List<String> envPropertiesnames = environment.getPRDEnvProperty()
                .stream()
                .map(PRDEnvProperty::getPRDName)
                .collect(Collectors.toList());

        String firstDuplicate = Utils.FindFirstDuplicate(envPropertiesnames);
        if (firstDuplicate != null) // no duplicates
            throw new UniqueNameException(firstDuplicate, SOURCE);
    }

    public static void ValidateEnvironmentProperty(PRDEnvProperty environmentProperty) {
        Property property = new Property(environmentProperty);
        PropertyValidator.PropertyValidator(property, SOURCE);
    }

}
