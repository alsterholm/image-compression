import app.Application;
import formats.BCI;
import formats.MTG;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Entry point for the image compression algorithm.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 *
 * ----------------------------------------------------------------------------
 *
 * Instructions for running on the command line:
 *
 *   - Compile Main.java, e.g.:
 *
 *      $ javac Main.java
 *
 *   - Run the compiled .class-file with the following
 *     arguments: [mode] [input file] [output file], e.g.:
 *
 *      $ java Main mtg2bci resources/green_boat.mtg resources/green_boat.bci
 *
 * ----------------------------------------------------------------------------
 *
 * Instructions for running through your IDE:
 *
 *   - Set the PATH and IMAGE variables in the Config-class of the tests package.
 *
 *   - Run the Test-class inside the tests package.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            Application.help();
        } else {
            String mode = args[0];
            String input = args[1];
            String output = args[2];

            Application.run(mode, input, output);
        }
    }
}
