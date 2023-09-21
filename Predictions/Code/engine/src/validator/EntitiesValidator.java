package validator;

import exceptions.InvalidWhiteSpaceException;
import exceptions.NegativePopulationException;
import exceptions.UniqueNameException;
import resources.generated.PRDEntities;
import resources.generated.PRDEntity;
import resources.generated.PRDProperty;

import java.util.List;
import java.util.stream.Collectors;

public class EntitiesValidator {
    private static final String PRD_ENTITY = PRDEntity.class.getSimpleName();

    private static final String PRD_PROPERTY = PRDProperty.class.getSimpleName();
    private static final String ROOT = "Entities validation";
    private static String SOURCE = ROOT;

    public static void ValidateEntities(PRDEntities entities) {
        validateUniqueNamesForEntities(entities);

        for (PRDEntity entity : entities.getPRDEntity()) {
            ValidateEntity(entity);
        }
    }

    private static void validateUniqueNamesForEntities(PRDEntities entities) {
        List<String> entitiesNames = entities.getPRDEntity()
                .stream()
                .map(PRDEntity::getName)
                .collect(Collectors.toList());

        String firstDuplicate = Utils.FindFirstDuplicate(entitiesNames);
        if (firstDuplicate != null) // no duplicates
            throw new UniqueNameException(firstDuplicate, SOURCE);
    }

    private static void validateUniqueNamesForEntityProperties(PRDEntity entity) {
        List<String> propsNames = entity.getPRDProperties().getPRDProperty()
                .stream()
                .map(PRDProperty::getPRDName)
                .collect(Collectors.toList());

        String firstDuplicate = Utils.FindFirstDuplicate(propsNames);
        if (firstDuplicate != null) // no duplicates
            throw new UniqueNameException(firstDuplicate, SOURCE);
    }

    public static void ValidateEntity(PRDEntity entity) {
        SOURCE = ROOT + " " + entity.getName();
        if (Utils.isContainsSpace(entity.getName()))
            throw new InvalidWhiteSpaceException(entity.getName(), SOURCE);
        validateUniqueNamesForEntityProperties(entity);
        for (PRDProperty PrdProperty : entity.getPRDProperties().getPRDProperty()) {
            Property property = new Property(PrdProperty);
            PropertyValidator.PropertyValidator(property, SOURCE);
        }
    }
}
