package tests;

import app.Main;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestMTG2SHIT {
    public static void main(String[] args) {
        String mode = "mtg2shit";
        String in = Config.PATH + Config.IMAGE + ".mtg";
        String out = Config.PATH + Config.IMAGE + ".shit";


        Main.main(new String[] { mode, in, out });
    }
}
