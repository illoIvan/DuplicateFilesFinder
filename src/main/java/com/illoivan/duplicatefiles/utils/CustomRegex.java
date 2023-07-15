package com.illoivan.duplicatefiles.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRegex {

    private CustomRegex() {}

    public static String stringEndWith(String... strings) {
        String str = String.format("(%s)$", String.join("|", strings));
        return formatStr(str);
    }
    
    public static String stringStartWith(String... strings) {
        String str = String.format("^(%s)", String.join("|", strings));
        return formatStr(str);
    }
    
    public static String exactString(String string) {
        String str = String.format("^%s$", string);
        return formatStr(str);
    }
    
    public static String stringContains(String[] strings) {
        String str = String.format("(%s)", String.join("|", strings));
        return formatStr(str);
    }
    
    public static String formatStr(String str) {
        String regExSpecialChars = "<([{\\^-=$!|]})?*+.>";
        String regExSpecialCharsRE = regExSpecialChars.replaceAll(".", "\\\\$0");
        Pattern reCharsREP = Pattern.compile( "[" + regExSpecialCharsRE + "]");
        Matcher m = reCharsREP.matcher(str);
        return m.replaceAll( "\\\\$0");
    }
    
}
