package com.ezyybin604.yesrandom2d;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLine {
    private InputStreamReader core;
    private List<String> data = new ArrayList<>();
    public Point size = new Point();
    public FileLine(String file) {
        File fileTester = new File(file);
        try {
            core = new InputStreamReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!fileTester.canRead() || !fileTester.isFile() || !fileTester.exists()) {
            throw new Error("Cannot read file.");
        }
        try {
            String buf = "";
            while (core.ready()) {
                int c = core.read();
                if (c == '\n') {
                    data.add(buf);
                    size.x = Math.max(buf.length(), size.x);
                    buf = "";
                } else {
                    buf += (char)c;
                }
            }
            size.x = Math.max(buf.length(), size.x);
            data.add(buf);
            size.y = data.size();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public char readChar(int ln, int ch) {
        if (Main.inRange(0, ln, data.size()-1)) {
            String stuff = data.get(ln);
            if (Main.inRange(0, ch, stuff.length()-1)) {
                return stuff.charAt(ch);
            } else {
                return ' ';
            }
        }
        return '\0';
    }
    public void close() {
        try {
            core.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
