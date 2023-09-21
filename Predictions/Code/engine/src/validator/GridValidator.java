package validator;

import exceptions.InvalidGridException;
import resources.generated.PRDWorld;

public class GridValidator {

    private static final String ROOT = "Grid validation";

    public static void ValidateThreadpool(PRDWorld.PRDGrid grid) {
        int rows = grid.getRows();
        int columns = grid.getColumns();
        if (rows < 10 || rows > 100) {
            throw new InvalidGridException("rows", ROOT);
        }
        if (columns < 10 || columns > 100) {
            throw new InvalidGridException("columns", ROOT);
        }
    }
}
