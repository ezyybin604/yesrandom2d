package src.main.java.com.ezyybin604.yesrandom2d;

public class Debugger {
    public static boolean enabled = false;
    public static Interpreter focus;
    public static void log(String s) {
        if (enabled) {
            System.out.println(s + " " + focus.cursor.toString());
        }
    }
}
