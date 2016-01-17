package app;

import graphics.Compressed;
import graphics.Megatron;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by andreas on 2016-01-17.
 */
public class Main {
    private static void run(String mode, String input, String output) {
        BufferedImage image;
        try {
            switch (mode) {
                case "mtg2shit":
                    image = Megatron.read(input);
                    Compressed.write(image, output);
                    break;

                case "shit2mtg":
                    image = Compressed.read(input);
                    Megatron.write(image, output);
                    break;

                case "shit2png":
                    break;

                case "png2shit":
                    break;

                case "mtg2png":
                    image = Megatron.read(input);
                    ImageIO.write(image, "PNG", new File(output));
                    break;

                case "png2mtg":
                    break;
            }
        } catch (IOException e) {

        }
    }

    private static void help() {

    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--help") || args[0].equals("h")) {
            help();
        } else if (args.length < 3) {
            System.out.println("Invalid arguments.");
            help();
        } else {
            String mode = args[0];
            String input = args[1];
            String output = args[2];

            run(mode, input, output);
        }
    }
}
