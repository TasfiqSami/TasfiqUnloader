/**
 * Loads and initializes every class given on the command line. Run with -Xverify:all on each
 * supported JDK so a class that cannot load or fails bytecode verification fails the build.
 */
public class LoadCheck {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("No classes given");
        }
        System.out.println("Running on Java " + System.getProperty("java.version"));
        for (String name : args) {
            Class.forName(name, true, LoadCheck.class.getClassLoader());
            System.out.println("OK  " + name);
        }
    }
}
