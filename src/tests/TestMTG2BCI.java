package tests;

import app.Application;

/**
 * Test
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class TestMTG2BCI {
    public static void main(String[] args) {
        String mode = "mtg2bci";
        String in = Config.PATH + Config.IMAGE + ".mtg";
        String out = Config.PATH + Config.IMAGE + ".bci";

        Application.run(mode, in, out);
    }
}
