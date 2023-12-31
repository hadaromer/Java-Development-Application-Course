package validator;

import exceptions.InvalidRangeException;
import exceptions.InvalidTerminationException;
import resources.generated.PRDBySecond;
import resources.generated.PRDByTicks;
import resources.generated.PRDTermination;

public class TerminationValidator {
    private static final String ROOT = "Termination validation";

    public static void ValidateTermination(PRDTermination termination) {
        boolean endByUser = false;
        if (termination.getPRDByUser() != null) {
            endByUser = true;
        }
        if(endByUser && !termination.getPRDBySecondOrPRDByTicks().isEmpty()){
            throw new InvalidTerminationException(ROOT);
        }
        int count;
        String element;
        for (Object option : termination.getPRDBySecondOrPRDByTicks()) {
            if (option.getClass() == PRDByTicks.class) {
                element = "by ticks";
                count = ((PRDByTicks) option).getCount();
            } else {
                element = "by seconds";
                count = ((PRDBySecond) option).getCount();
            }
            if (count <= 0) {
                throw new InvalidRangeException(element, "1 - infinity", ROOT);
            }
        }
    }
}
