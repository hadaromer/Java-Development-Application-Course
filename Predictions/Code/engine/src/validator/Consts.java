package validator;

import java.util.Arrays;
import java.util.List;

public class Consts {
    public static final List<String> TYPES = Arrays.asList("decimal", "float", "boolean","string");
    public static final List<String> RANGE_TYPES = Arrays.asList("decimal", "float");
    public static final List<String> HELPER_FUNCTIONS = Arrays.asList("environment","random","evaluate","percent","ticks");
    public static final List<String> NUMERIC_OPERATORS = Arrays.asList("bt","lt");
}
