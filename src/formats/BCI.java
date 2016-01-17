package formats;

import utilities.*;

import java.awt.image.*;
import java.io.*;

/**
 * Class to compress an image to the Seriously High-tech Image Type (BCI) format.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class BCI {
    private final static byte[] WATERMARK = "bci_file".getBytes();

    /**
     * Write a BCI image to file.
     *
     * @param image Image
     * @param filename Filename
     * @throws IOException
     */
    public static void write(BufferedImage image, String filename) throws IOException {
        int W = image.getWidth();
        int H = image.getHeight();

        OutputStream out = new FileOutputStream(filename);

        // Write the watermark to the file
        out.write(WATERMARK);

        // Write the width and height to the file
        write4bytes(W, out);
        write4bytes(H, out);

        // Run the compression
        byte[] bytes = Compression.run(image);

        for (int i = 0; i < bytes.length; i++) {
            out.write(bytes[i]);
        }

        out.close();
    }

    /**
     * Read a BCI image from file.
     *
     * @param filename Filename
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage read(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);
        BufferedImage img;

        // Read BCI watermark from file
        for (int i = 0; i < WATERMARK.length; i++) {
            if (in.read() != WATERMARK[i]) { throw new IOException(); }
        }

        // Read image width and height from file
        int W  = read4bytes(in);
        int H = read4bytes(in);

        byte[] buffer = new byte[4096];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }

        byte[] bytes = bos.toByteArray();

        // Run the decompression
        bytes = Decompression.run(bytes);

        img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = img.getRaster();

        for (int y = 0, i = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                raster.setPixel(x, y, new int[] {
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                        bytes[i++] & 0xFF,
                });
            }
        }

        return img;
    }

    /**
     * Writes 4 bytes to the specified output stream.
     *
     * @param v Value
     * @param out Output stream
     * @throws IOException
     */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    /**
     * Read 4 bytes from an input stream.
     *
     * @param in Input stream
     * @return Value
     * @throws IOException
     */
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
