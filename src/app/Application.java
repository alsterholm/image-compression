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

                case "shit2bci":
                    image = BCI.read(input);
                    MTG.write(image, output);
                    break;

                case "mtg2png":
                    image = MTG.read(input);
                    ImageIO.write(image, "PNG", new File(output));
                    break;
            }
        } catch (IOException e) {

        }
    }
}
