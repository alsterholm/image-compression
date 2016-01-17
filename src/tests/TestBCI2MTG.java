package tests;

import app.Main;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestBCI2MTG {
    public static void main(String[] args) {
        String mode = "shit2mtg";
        String in = Config.PATH + Config.IMAGE + ".shit";
        String out = Config.PATH + Config.IMAGE + "-decompressed.mtg";

        Main.main(new String[] {mode, in, out});
    }
}
