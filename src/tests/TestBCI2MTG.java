package tests;

import app.Application;

/**
 * Test
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class TestBCI2MTG {
    public static void main(String[] args) {
        String mode = "bci2mtg";
        String in = Config.PATH + Config.IMAGE + ".bci";
        String out = Config.PATH + Config.IMAGE + "-decompressed.mtg";

        Application.run(mode, in, out);
    }
}
