package validator;

import exceptions.InvalidRangeException;
import exceptions.ThreadpoolSizeException;
import resources.generated.PRDBySecond;
import resources.generated.PRDByTicks;
import resources.generated.PRDTermination;
import resources.generated.PRDWorld;

public class ThreadpoolValidator {

    private static final String ROOT = "Termination validation";

    public static void ValidateThreadpool(PRDWorld world) {
        if (world.getPRDThreadCount() < 1) {
            throw new ThreadpoolSizeException(ROOT);
        }
    }
}
