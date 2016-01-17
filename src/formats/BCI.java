package formats;

import utilities.*;

import java.awt.image.*;
import java.io.*;

/**
 * Class to compress an image to the Seriously High-tech Image Type (BCI) format.
 *
 * @author Jimmy Lindström & Andreas Indal
 */
public class BCI {
    private final static byte[] BCI = "bci_file".getBytes();

    public static void write(BufferedImage image, String filename) throws IOException {
        int W = image.getWidth();
        int H = image.getHeight();

        OutputStream out = new FileOutputStream(filename);
        out.write(BCI);

        write4bytes(W, out);
        write4bytes(H, out);

        byte[] bytes = Compression.run(image);

        for (int i = 0; i < bytes.length; i++) {
            out.write(bytes[i]);
        }

        out.close();
    }

    public static BufferedImage read(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);
        BufferedImage img;

        // Read BCI waterstamp from file
        for (int i = 0; i < BCI.length; i++) {
            if (in.read() != BCI[i]) { throw new IOException(); }
        }

        // Read image width and height from file
        int width  = read4bytes(in);
        int height = read4bytes(in);

        byte[] buffer = new byte[4096];

        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }

        bytes = bos.toByteArray();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = img.getRaster();

        bytes = Decompression.run(bytes);

        for (int y = 0, i = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setPixel(x, y, new int[] {
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                });
            }
        }

        return img;
    }

    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;

        for (int i = 3; i >= 0; i--) {
            b = in.read();
            if (b < 0) {
                throw new EOFException();
            }
            v |= b << i * 8;
        }

        return v;
    }
}
