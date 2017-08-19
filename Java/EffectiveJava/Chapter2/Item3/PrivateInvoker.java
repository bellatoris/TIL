import java.lang.reflect.Constructor;

public class PrivateInvoker {
    public static void main(String[] args) throws Exception {
        Constructor<?> con = Private.class.getDeclaredConstructors()[0];
        con.setAccessible(true);
        Private p = (Private) con.newInstance();
    }
}

class Private {
    private Private() {
        System.out.println("Hello!");
    }
}
