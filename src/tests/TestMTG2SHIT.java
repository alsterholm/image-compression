package tests;

import app.Main;

/**
 * Created by andreas on 2016-01-17.
 */
public class TestMTG2SHIT {
    public static void main(String[] args) {
        String mode = "mtg2shit";
        String in = "C:\\Users\\Jimmy\\git\\image-compression\\src\\resources\\cartoon.mtg";
        String out = "C:\\Users\\Jimmy\\git\\image-compression\\src\\resources\\cartoon.shit";


        Main.main(new String[] { mode, in, out });
    }
}
