package com.ezyybin604.yesrandom2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.Scanner;
import java.awt.Point;

enum NoneBool {
    TRUE,
    FALSE,
    NULL
}

public class Interpreter {
    FileLine file;
    List<Integer> cells = new ArrayList<>();
    Point[] diroffsets = {
        new Point(1, 0),
        new Point(1, 1),
        new Point(0, 1),
        new Point(-1,1),
        new Point(-1,0),
        new Point(-1,-1),
        new Point(0,-1),
        new Point(1,-1)
    };
    Point cursor = new Point();
    int regtarget;

    // random variables for random features of the language
    NoneBool lastConditionalResult = NoneBool.NULL;
    Scanner reader = new Scanner(System.in);
    Map<Character, Point> lastRead = new HashMap<>();
    Random rand = new Random();

    public Interpreter(String fn) {
        file = new FileLine(fn);
    }

    public int getCell(int i) {
        if (Main.inRange(0, i, cells.size()-1)) return cells.get(i);
        return 0;
    }

    public void exexcute() {
        char c = file.readChar(cursor.y, cursor.x);
        int eval = c;
        lastRead.put(c, cursor);
        if (cursor.x % 2 == 0 ^ cursor.y % 2 == 0) {
            setCell(regtarget, eval);
        } else {
            regtarget = eval;
        }
        // Move cursor
        int newdir = getCell(68);
        Point dirt = diroffsets[Main.inRange(0, newdir, 7) ? newdir : 0];
        cursor.translate(dirt.x, dirt.y);
        // up+left=0, up+right=2, down+right=4, down+left=6
        boolean changed = false;
        if (cursor.x <= -1) { // left wall
            newdir = 6;
            cursor.translate(1, 0);
            changed = true;
        }
        if (cursor.y <= -1) { // up wall
            newdir = 0;
            cursor.translate(0, 1);
        }
        if (cursor.x >= file.size.x) { // right wall
            newdir = 2;
            cursor.translate(-1, 0);
        }
        if (cursor.y >= file.size.y) { // down wall
            if (!changed) newdir = 4;
            cursor.translate(0, -1);
        }
        setCell('D', newdir);
    }

    public void setCell(int i, int val) {
        while (i >= cells.size()) cells.add(0);
        cells.set(i, val);
        if ((char)i == 'C') {
            boolean cond;
            switch ((char)val) {
                case '@':
                    setCell('O', getCell('A') + getCell('C'));
                    break;
                case ';':
                    setCell('O', getCell('B') - getCell('R'));
                    break;
                case '$':
                    setCell('o', getCell('N') * getCell('Q'));
                    break;
                case '=':
                    cond = getCell('4') == 32;
                    lastConditionalResult = Main.convertBool(cond);
                    Debugger.log("Conditional result " + cond);
                    if (cond) setCell(getCell('T'), getCell(getCell('E')));
                    break;
                case 'V':
                    cond = getCell('7') > 3;
                    lastConditionalResult = Main.convertBool(cond);
                    Debugger.log("Conditional result " + cond);
                    if (cond) setCell(getCell('F'), getCell(getCell('R')));
                    break;
                case 'q':
                    int fromreg = getCell('W');
                    int toreg = getCell('R');
                    if (Debugger.enabled) Debugger.log("Copied " + getCell(fromreg) + " from " + (char)fromreg + fromreg + " to " + (char)toreg + toreg);
                    setCell(toreg, getCell(fromreg));
                    break;
                case '\n':
                    if (lastConditionalResult == NoneBool.TRUE) {
                        char c = reader.findInLine(".").charAt(0);
                        setCell('Q', c);
                    } else if (lastConditionalResult == NoneBool.FALSE) {
                        char c = (char)getCell('r');
                        System.out.print(c);
                    } else {
                        cursor = new Point(rand.nextInt(file.size.x), rand.nextInt(file.size.y));
                    }
                    break;
                case ' ':
                    cursor = lastRead.get((Object)getCell('j'));
                    break;
                case '^': case 'm': case '!': case 'M': case 'v':
                    System.out.print("Not Implimented");
                default:
                    break;
            }
        } else {
            Debugger.log("Regwr " + (char)i + ", v:" + val);
        }
    }
}
