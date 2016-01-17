package tests;

import app.Main;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestMTG2PNG {
    public static void main(String[] args) {
        String mode = "mtg2png";
        String in = "C:\\Users\\Jimmy\\git\\image-compression\\src\\resources\\cartoon-decompressed.mtg";
        String out = "C:\\Users\\Jimmy\\git\\image-compression\\src\\resources\\cartoonnew.png";

        Main.main(new String[]{mode, in, out});
    }
}
