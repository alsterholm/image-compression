package utilities;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.*;

/**
 *Class that handles the compression of  a MTG image-file to a BCI file by changing the color depth
 * to 256 colors. It uses an array of 2556 colors created from the image that is supplied. The color
 * array is written to the beginning of the byte-array that is created by this class.
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Compression {
    /**
     *
     * Runs the compression
     * @param image the image file to compress
     * @return byte[] containing compressed image
     */
    public static byte[] run(BufferedImage image) {
        byte[] bytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        byte[] out = new byte[(256 * 4) + (bytes.length / 3)];
        int index = 0;
        //creates the color palette
        int[] colors = Compression.createColors(image);

        // Write used colors to image
        for (int color : colors) {
            for (int i = 4; i > 0; i--) {
                byte b = (byte)((color >>> ((i - 1) * 8)) & 0xFF);
                out[index++] = b;
            }
        }
        //loops through the byte[] bytes getting original color and the replacing
        //it with it's equivalent color from created color palette
        for (int i = 0; i < bytes.length; i += 3) {
            int b = bytes[i + 2] & 0xFF;
            int g = bytes[i + 1] & 0xFF;
            int r = bytes[i]     & 0xFF;

            out[index++] = (byte) getEquivalentColor(b, g, r, colors);
        }

        return out;
    }

    /**
     * Creates an array of the most frequent colors in the picture that differs
     * by a threshold value
     * @param image the image to compress
     * @return color array with integers
     */
    private static int[] createColors(BufferedImage image) {
        int[] colors = new int[256];
        HashMap<Integer, Integer> map = new HashMap<>();
        Raster raster = image.getRaster();

        // Finding all colors that exists in the image and putting
        // them in a temporary Map with color as key, and occurrences as value
        int[] p = new int[3];
        //The loop doesn't loop through all the pixels, it jumps 5 pixels/iteration
        //in both width and height to save some time
        for (int x = 0, W = image.getWidth(), color; x < W; x += 5) {
            for (int y = 0, H = image.getHeight(); y < H; y += 5) {
                p = raster.getPixel(x, y, p);

                color = ImageUtils.toInt(p[0], p[1], p[2]);

                int n = map.containsKey(color) ? map.get(color) + 1: 1;
                map.put(color, n);
            }
        }

        // Creating a temporary list containing the keys of the hashmap (that is,
        // the colors that were found in the image).
        LinkedList<Integer> list = new LinkedList<>(map.keySet());

        // Sorting the list by the most frequently occuring colors first.
        list.sort((o1, o2) -> map.get(o2) - map.get(o1));

        //Adding the colors that are the most frequent and that differs by the threshold
        //value to the final array of colors
        colors[0] = list.get(0);

        for (int i = 0, index = 1; i < list.size() && index < 232; i++) {
            boolean enoughDistance = true;

            for (int j = 0; j < index; j++) {
                // Checking if the colors are separated enough to add, in order
                // to get a good spread.
                if (ImageUtils.distanceInLAB(colors[j], list.get(i)) < 3) {
                    enoughDistance = false;
                    break;
                }
            }

            // If no colors that were to similar was found, including
            // the color.
            if (enoughDistance) {
                colors[index++] = list.get(i);
            }
        }

        // Adding greyscale colors to the list of colors
        for (int i = 232, intensity = 0; i <= 255; i++) {
            colors[i] = ImageUtils.toInt(intensity, intensity, intensity);
            intensity = Math.min(255, intensity + 13);
        }
        return colors;
    }

    /**
     * Returns the color closest to the color passed to method as parameter(int b,g,r)
     * from the color palette
     * @param b int value for blue color
     * @param g int value for green color
     * @param r int value for red color
     * @param colors array containing color palette
     * @return the equivalent color
     */
    private static int getEquivalentColor(int b, int g, int r, int[] colors) {
        int color = -1;
        int c = ImageUtils.toInt(b, g, r);
        double minDistance = Double.MAX_VALUE;

        //looping through the colors array comparing each color to the color
        //from original image and picking the one closest.
        for (int p = 0; p < colors.length; p++) {
            double d = ImageUtils.distanceInLAB(colors[p], c);
            if (d < minDistance) {
                color = p;
                minDistance = d;
            }
        }

        return color;
    }
}
