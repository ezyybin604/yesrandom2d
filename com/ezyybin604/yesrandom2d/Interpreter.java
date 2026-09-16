package com.ezyybin604.yesrandom2d;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;

enum NoneBool {
    TRUE,
    FALSE,
    NULL
}

public class Interpreter {
    class FileIO {
        public boolean override = false;
        String written = "";
        String filen;
        InputStreamReader sr;
        public FileIO(String fn) {
            filen = fn;
            try {
                sr = new InputStreamReader(new FileInputStream(fn));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        public char read() {
            try {
                return (char)sr.read();
            } catch (IOException e) {
                return '\0';
            }
        }
    
        public void write(char ch) {
            written += ch;
        }
        public void close() {
            try {
                sr.close();
                FileWriter writer = new FileWriter(filen, !override);
                writer.write(written);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
    Map<Character, Point> lastRead = new HashMap<>();
    Map<String, FileIO> files = new HashMap<>();
    String currentFile = "";
    boolean openedFile = false;
    Random rand = new Random();

    public Interpreter(String fn) {
        file = new FileLine(fn);
    }

    public int getCell(int i) {
        if (Main.inRange(0, i, cells.size()-1)) return cells.get(i);
        return 0;
    }

    public String getString(int sr) {
        String out = "";
        int idx = sr;
        int r = getCell(idx);
        while (r > 0) {
            out += (char)r;
            idx++;
            r = getCell(idx);
        }
        return out;
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
        if (Main.inRange(0, newdir, 7)) {
            Point dirt = diroffsets[newdir];
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
    }

    public void setCell(int i, int val) {
        while (i >= cells.size()) cells.add(0);
        cells.set(i, val);
        if ((char)i == 'C') {
            executeCmd((char)val);
        } else {
            Debugger.log("Regwr " + (char)i + ", v:" + val);
        }
    }

    void executeCmd(char cmd) {
        boolean cond;
        switch (cmd) {
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
                switch (lastConditionalResult) {
                    case TRUE -> {
                        char c = '\0';
                        try {
                            c = (char)System.in.read();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setCell('Q', c);
                    }
                    case FALSE -> {
                        char c = (char)getCell('r');
                        System.out.print(c);
                    }
                    case NULL -> cursor = new Point(rand.nextInt(file.size.x), rand.nextInt(file.size.y));
                }
                break;

            case ' ':
                cursor = lastRead.get((char)getCell('j'));
                break;
            case '^':
                String name = getString(getCell('*'));
                openedFile = true;
                currentFile = name;
                files.put(name, new FileIO(name));
                break;
            case 'm':
                if (openedFile) {
                    files.get(currentFile).override = true;
                }
                break;
            case '!':
                if (openedFile) {
                    files.get(currentFile).write((char)getCell('N'));
                }
                break;
            case 'M':
                int res = -2;
                if (openedFile) {
                    try {
                        if (files.get(currentFile).sr.ready()) {
                            res = files.get(currentFile).read();
                        } else {
                            res = -1;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                setCell('*', res);
                break;
            case 'v':
                System.out.print("Not Implimented");
            default:
                break;
        }
    }
}
