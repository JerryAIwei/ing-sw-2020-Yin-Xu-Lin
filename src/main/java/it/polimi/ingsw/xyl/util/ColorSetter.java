package it.polimi.ingsw.xyl.util;

/**
 *  Tool to color console output
 *
 *  @author yaw
 */

public enum ColorSetter {
    FG_RED("\u001B[31m"),
    FG_GREEN("\u001B[32m"),
    FG_YELLOW("\u001B[33m"),
    FG_BLUE("\u001B[34m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    ;


    static final String RESET = "\u001B[0m";

    private String escape;

    ColorSetter(String escape) {
        this.escape = escape;
    }

    public String setColor(String s) {
        if(s.endsWith("\n"))
            return escape+s.substring(0,s.length()-1)+RESET+"\n";
        else
            return escape+s+RESET;
    }


}
