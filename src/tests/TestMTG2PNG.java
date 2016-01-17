package tests;

import app.Application;

/**
 * Test
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class TestMTG2PNG {
    public static void main(String[] args) {
        String mode = "mtg2png";
        String in = Config.PATH + Config.IMAGE + "-decompressed.mtg";
        String out = Config.PATH + Config.IMAGE + "-new.png";

        Application.run(mode, in, out);
    }
}
