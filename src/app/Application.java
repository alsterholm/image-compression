package app;

import formats.BCI;
import formats.MTG;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * BCI compression application.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Application {
    /**
     * Run the application.
     *
     * @param mode Application mode
     * @param input Input file name
     * @param output Output file name
     */
    public static void run(String mode, String input, String output) {
        BufferedImage image;
        try {
            switch (mode) {
                case "mtg2bci":
                    image = MTG.read(input);
                    BCI.write(image, output);
                    break;

                case "bci2mtg":
                    image = BCI.read(input);
                    MTG.write(image, output);
                    break;

                case "mtg2png":
                    image = MTG.read(input);
                    ImageIO.write(image, "PNG", new File(output));
                    break;
            }
        } catch (IOException e) {
            Application.help();
        }
    }

    /**
     * Print a helpful message out to the console.
     */
    public static void help() {
        String output = "BCI Image Compression by Jimmy Lindström & Andreas Indal\n" +
                "\n" +
                "Usage: [mode] [input file] [output file]\n" +
                "\n" +
                "Available modes:\n" +
                "    mtg2bci    Compress an MTG-file into a BCI-file.\n" +
                "    bci2mtg    Decompress a BCI-file into an MTG-file.\n" +
                "    mtg2png    Convert an MTG-file to a PNG-file.\n";

        System.out.println(output);
    }
}
