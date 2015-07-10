package pro.hyper_passport.ticketsystem;

import java.util.ArrayList;

/**
 * Created by Mihail on 10.07.2015.
 */
public  class SupportCount {
    private static  int valueThemes;
    private static ArrayList<String> nameThemes  = new ArrayList<String>();

    public static void setValueThemes(int valueThemess) {
        valueThemes = valueThemess;
    }

    public static int getValueThemes() {
        return valueThemes;
    }

    public static void setNameThemes(String string) {
        nameThemes.add(string);
    }

    public static String getNameThemes(int i) {
        return nameThemes.get(i);
    }
}
