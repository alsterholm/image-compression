package tests;

import app.Main;
import graphics.Compressed;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestSHIT2MTG {
    public static void main(String[] args) {
        String mode = "shit2mtg";
        String in = "/Users/andreas/code/java/image-compression/src/resources/cartoon.shit";
        String out = "/Users/andreas/code/java/image-compression/src/resources/cartoon-decompressed.mtg";

        Main.main(new String[] {mode, in, out});
    }
}
