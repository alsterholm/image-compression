package graphics;

import utilities.ReduceColors;

import java.awt.image.*;
import java.io.*;

/**
 * Class to compress an image to the Seriously High-tech Image Type (SHIT) format.
 *
 * @author Jimmy Lindström & Andreas Indal
 */
public class Compressed {
    private final static byte[] SHIT = "shitfile".getBytes();

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
        BufferedImage img;

        // Check magic value.
        for (int i = 0; i < SHIT.length; i++) {
            if (in.read() != SHIT[i]) { throw new IOException(); }
        }

        // Read width and height
        int width  = read4bytes(in);
        int height = read4bytes(in);

        byte[] buffer = new byte[4096];

        byte[] data = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }

            data = bos.toByteArray();

        }

        img = fromSHIT(width, height, data);
        return img;
    }

    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write( v >>> 3*8);
        out.write((v >>> 2*8) & 0xFF);
        out.write((v >>>   8) & 0xFF);
        out.write(v & 0xFF);
    }

    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;
//        b = in.read(); if (b < 0) { throw new EOFException(); }
//        v = b<<3*8;
//        b = in.read(); if (b < 0) { throw new EOFException(); }
//        v |= b<<2*8;
//        b = in.read(); if (b < 0) { throw new EOFException(); }
//        v |= b<<1*8;
//        b = in.read(); if (b < 0) { throw new EOFException(); }
//        v |= b;
//        return v;
        for (int i = 3; i >= 0; i--) {
            b = in.read();
            if (b < 0) {
                throw new EOFException();
            }
            v |= b << i * 8;
        }
        return v;
    }


    private static byte[] toSHIT(BufferedImage image) {
        byte[] b = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        b = ReduceColors.run(b);

        return b;
    }

    private static BufferedImage fromSHIT(int width, int height, byte[] bytes) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = img.getRaster();

        System.out.println("Bytes (compressed) = " + bytes.length);

        bytes = ReduceColors.revert(bytes);

        System.out.println("Bytes (decompressed) = " + bytes.length);
        System.out.println("Pixels (3bytes/p) = " + (bytes.length / 3));

        int i = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setPixel(x, y, new int[] {bytes[i] & 0xFF, bytes[i + 1] & 0xFF, bytes[i + 2] & 0xFF});

                i += 3;
            }
        }

        return img;
    }
}
