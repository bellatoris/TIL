public class Boolean {
    private Boolean() {

    }

    static Boolean TRUE = new Boolean();
    static Boolean FALSE = new Boolean();

    public static Boolean valueOf(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    public static void isTrue(Boolean bool) {
        if (bool == Boolean.TRUE) {
            System.out.println("True");
        } else {
            System.out.println("False");
        }
    }
}
