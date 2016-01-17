package graphics;

import utilities.ReduceColors;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Class to compress an image to the Seriously High-tech Image Type (SHIT) format.
 *
 * @author Jimmy Lindström & Andreas Indal
 */
public class Compressed {
    private final static byte[] SHIT = "shitfile".getBytes(StandardCharsets.US_ASCII);

    private final static class SHITException extends IOException {}

    public static void write(BufferedImage image, String filename) throws IOException {
        int W = image.getWidth();
        int H = image.getHeight();

        OutputStream out = new FileOutputStream(filename);
        out.write(SHIT);
        write4bytes(W, out);
        write4bytes(H, out);

        byte[] b = toSHIT(image);

        for (int i = 0; i < b.length; i++) {
            out.write(b[i]);
        }

        out.close();
    }

    public static BufferedImage read(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);

        // Check SHIT value.
        for (int i = 0; i < SHIT.length; i++) {
            if (in.read() != SHIT[i]) { throw new SHITException(); }
        }

        int width  = read4bytes(in);
        int height = read4bytes(in);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        byte[] pxlBytes = new byte[3];
        int[] pxl = new int[3];

        WritableRaster imgr  = img.getRaster();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (in.read(pxlBytes) != 3) { throw new EOFException(); }
                pxl[0] = pxlBytes[0];
                pxl[1] = pxlBytes[1];
                pxl[2] = pxlBytes[2];
                imgr.setPixel(i, j, pxl);
            }
        }

        in.close();
        return img;
    }

    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v >>> 3*8);
        out.write(v >>> 2*8 & 255);
        out.write(v >>>   8 & 255);
        out.write(v & 255);
    }

    private static int read4bytes(InputStream in) throws IOException {
        int b, v;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v = b<<3*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<2*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<1*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b;
        return v;
    }

    private static byte[] toSHIT(BufferedImage image) {
        byte[] b = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        b = ReduceColors.run(b);

        return b;
    }
}
