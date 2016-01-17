package utilities;

import java.awt.image.BufferedImage;

/**
 * Class that handles calculations in color distance. Algorithms used are from
 * various websites.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class ImageUtils {

    private ImageUtils() {}

    /**
     * Calculates the distance (delta E) between two RGB colors by first converting them into LAB color space, then calculates
     * then Euclidean distance. https://en.wikipedia.org/wiki/Color_difference
     *
     * @param c1 First color
     * @param c2 Second color
     * @return Value describing the distance between the two colors
     */
    public static double distanceInLAB(int c1, int c2) {

        double[] lab1 = XYZtoLAB(RGBtoXYZ(c1));
        double[] lab2 = XYZtoLAB(RGBtoXYZ(c2));

        return Math.sqrt(Math.pow(lab1[0] - lab2[0], 2) + Math.pow(lab1[1] - lab2[1], 2) + Math.pow(lab1[2] - lab2[2], 2));
    }

    /**
     * Converts RGB color to XYZ color space. Code from http://www.easyrgb.com/index.php?X=MATH
     *
     * @param c RGB color
     * @return XYZ color
     */
    private static double[] RGBtoXYZ(int c) {
        double r = ((c >> 16) & 0xFF) / (double)255;
        double g = ((c >> 8) & 0xFF) / (double)255;
        double b = (c & 0xFF) / (double)255;

        r = r > 0.04045 ? Math.pow(((r + 0.055) / 1.055), 2.4) : r / 12.92;
        g = g > 0.04045 ? Math.pow(((g + 0.055) / 1.055), 2.4) : g / 12.92;
        b = b > 0.04045 ? Math.pow(((b + 0.055) / 1.055), 2.4) : b / 12.92;

        r *= 100;
        g *= 100;
        b *= 100;

        double[] xyz = new double[3];

        xyz[0] = r * 0.4124 + g * 0.3576 + b * 0.1805;
        xyz[1] = r * 0.2126 + g * 0.7152 + b * 0.0722;
        xyz[2] = r * 0.0193 + g * 0.1192 + b * 0.9505;

        return xyz;
    }

    private static final double refX = 95.047;
    private static final double refY = 100.00;
    private static final double refZ = 108.883;

    /**
     * Converts XYZ color to LAB color space. Code from http://www.easyrgb.com/index.php?X=MATH
     * @param xyz XYZ color
     * @return LAB color
     */
    private static double[] XYZtoLAB(double[] xyz) {
        double x = xyz[0]  / refX;
        double y = xyz[1]  / refY;
        double z = xyz[2]  / refZ;

        x = x > 0.00856 ? Math.pow(x, (double)1/3) : (7.787 * x) + ((double)16 / 116);
        y = y > 0.00856 ? Math.pow(y, (double)1/3) : (7.787 * y) + ((double)16 / 116);
        z = z > 0.00856 ? Math.pow(z, (double)1/3) : (7.787 * z) + ((double)16 / 116);

        double[] lab = new double[3];

        lab[0] = (116 * y) - 16;
        lab[1] = 500 * (x - y);
        lab[2] = 200 * (y - z);

        return lab;
    }

    /**
     * Returns an ARGB-value of the given values. A is set to 255.
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return ARGB color value stored in a single integer
     */
    public static int toInt(int r, int g, int b) {
        int argb = 0;

        argb += 0xFF << 24;
        argb += (r & 0xFF) << 16;
        argb += (g & 0xFF) << 8;
        argb += b & 0xFF;

        return argb;
    }
}
