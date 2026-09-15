package src.main.java.com.ezyybin604.yesrandom2d;

public class Main {
    public static boolean inRange(int min, int n, int max) {
        return min <= n && n <= max;
    }
    public static NoneBool convertBool(boolean bool) {
        if (bool) return NoneBool.TRUE;
        return NoneBool.FALSE;
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Provide program file");
            return;
        }
        if (args[0].equals("-debug")) Debugger.enabled = true;
        Interpreter intp = new Interpreter(args[args.length-1]);
        Debugger.focus = intp;
        while (Main.inRange(0, intp.getCell(68), 7)) {
            intp.exexcute();
        }
        Debugger.log("Stopped program");
        return;
    }
}
