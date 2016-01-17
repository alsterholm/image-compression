package utilities;

import tests.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Compression {
    public static byte[] run(BufferedImage image) {
        byte[] bytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        byte[] out = new byte[(256 * 4) + (bytes.length / 3)];
        int index = 0;

        int[] colors = Compression.createColors(image);

        // Write used colors to image
        for (int color : colors) {
            for (int i = 4; i > 0; i--) {
                byte b = (byte)((color >>> ((i - 1) * 8)) & 0xFF);
                out[index++] = b;
            }
        }

        for (int i = 0; i < bytes.length; i += 3) {
            int b = bytes[i + 2] & 0xFF;
            int g = bytes[i + 1] & 0xFF;
            int r = bytes[i]     & 0xFF;

            out[index++] = (byte) getEquivalentColor(b, g, r, colors);
        }

        return out;
    }

    private static int[] createColors(BufferedImage image) {
        int[] colors = new int[256];
        HashMap<Integer, Integer> map = new HashMap<>();
        Raster raster = image.getRaster();

        // Finding all colors that exists in the image.
        int[] p = new int[3];
        for (int x = 0, W = image.getWidth(), color; x < W; x += 5) {
            for (int y = 0, H = image.getHeight(); y < H; y += 5) {
                p = raster.getPixel(x, y, p);

                color = ImageUtils.toInt(p[0], p[1], p[2]);

                int n = map.containsKey(color) ? map.get(color) + 1: 1;
                map.put(color, n);
            }
        }

        // Creating a list containing the keys of the hashmap (that is,
        // the colors that were found in the image).
        LinkedList<Integer> list = new LinkedList<>(map.keySet());

        // Sorting the list by the most frequently occuring colors first.
        list.sort((o1, o2) -> map.get(o2) - map.get(o1));

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
        colors[232] = 0x000000;
        colors[233] = 0x080808;
        colors[255] = 0xFFFFFF;

        int intensity = 8;
        for (int i = 234; i < 255; i++) {
            intensity += 10;
            colors[i] = ImageUtils.toInt(intensity, intensity, intensity);
        }

        return colors;
    }

    private static int getEquivalentColor(int b, int g, int r, int[] colors) {
        int color = -1;
        int c = ImageUtils.toInt(b, g, r);
        double minDistance = Double.MAX_VALUE;

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
