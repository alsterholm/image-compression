package tests;

import app.Main;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestMTG2PNG {
    public static void main(String[] args) {
        String mode = "mtg2png";
        String in = Config.PATH + Config.IMAGE + "-decompressed.mtg";
        String out = Config.PATH + Config.IMAGE + "-new.png";

        Main.main(new String[]{mode, in, out});
    }
}
