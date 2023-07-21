package reflection.api;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.*;

public class Inspector implements Investigator{
    private Class theClass;
    private Object theObject;
    public Inspector() {}

    public void load(Object anInstanceOfSomething) {
        theObject = anInstanceOfSomething; // load the given instance
        theClass = anInstanceOfSomething.getClass(); // load the class of given instance
    }

    public int getTotalNumberOfMethods() {
        return theClass.getDeclaredMethods().length;
    }

    public int getTotalNumberOfConstructors() {
        return theClass.getDeclaredConstructors().length;
    }

    public int getTotalNumberOfFields() {
        return theClass.getDeclaredFields().length;
    }

    public Set<String> getAllImplementedInterfaces() {
        Set<String> interfacesSet = new HashSet<String>();
        Class[] interfaces = theClass.getInterfaces();
        for(Class interfac : interfaces){
            interfacesSet.add(interfac.getSimpleName());
        }
        return interfacesSet;
    }

    public int getCountOfConstantFields() {
        return (int) Arrays.stream(theClass.getDeclaredFields())
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .count();
    }

    public int getCountOfStaticMethods() {
        return (int) Arrays.stream(theClass.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .count();
    }

    public boolean isExtending() {
        // if the super class is Object so the class is not extending
        return theClass.getSuperclass() != Object.class;
    }

    public String getParentClassSimpleName() {
        if(!isExtending())
            return null;
        return theClass.getSuperclass().getSimpleName();
    }

    public boolean isParentClassAbstract() {
        return isExtending() && Modifier.isAbstract(theClass.getSuperclass().getModifiers());
    }

    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        Set<String> fieldsSet = new HashSet<String>();
        Class c = theClass;
        while(c != null) { // loop over all super classes
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                fieldsSet.add(field.getName());
            }
            c = c.getSuperclass();
        }
        return fieldsSet;
    }

    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        try {
            Method func = theClass.getDeclaredMethod(methodName);
            return (int)func.invoke(theObject, args);
        }
        catch (Exception e){
            return 0;
        }
    }

    public Object createInstance(int numberOfArgs, Object... args) {
        try {
            return Arrays.stream(theClass.getDeclaredConstructors())
                    .filter(constructor -> constructor.getParameterCount() == numberOfArgs) // filter by num of args
                    .findFirst() // we are looking for the first
                    .get()
                    .newInstance(args);
        }
        catch (Exception e){
            return null;
        }
    }

    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try {
            Method func = theClass.getDeclaredMethod(name, parametersTypes);
            func.setAccessible(true);
            return func.invoke(theObject, args);
        }
        catch (Exception e){
            return null;
        }

    }

    public String getInheritanceChain(String delimiter) {
        return inheritanceChainHelper(theClass, delimiter);
    }

    // recursive function that returns the inheritance chain of a given class
    private String inheritanceChainHelper(Class c, String delimiter){
        if(c.getSuperclass() == null)
            return c.getSimpleName();
        return inheritanceChainHelper(c.getSuperclass(),delimiter) + delimiter + c.getSimpleName();
    }
}
