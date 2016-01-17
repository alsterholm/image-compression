package graphics;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.imageio.*;

public class Megatron {
    /** Magic start string to signify Megatron file format. */
    final static byte[] magic = "mEgaMADNZ!".getBytes(StandardCharsets.US_ASCII);

    public final static class InvalidMegatronFileException extends IOException { }

    public static void write(BufferedImage img, String fnam) throws IOException {
        int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgr  = img.getRaster();
        OutputStream out = new FileOutputStream(fnam);
        out.write(magic);
        write4bytes(width, out);
        write4bytes(height, out);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imgr.getPixel(i, j, pxl);
                out.write(pxl[0]);
                out.write(pxl[1]);
                out.write(pxl[2]);
            }
        }
        out.close();
    }

    public static BufferedImage read(String fnam) throws IOException {
        InputStream in = new FileInputStream(fnam);

        // Check magic value.
        for (int i = 0; i < magic.length; i++) {
            if (in.read() != magic[i]) { throw new InvalidMegatronFileException(); }
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

    /** Writes an int as 4 bytes, big endian. */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);
    }

    /** Reads an int as 4 bytes, big endian. */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v = 0;

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

    public static void main(String[] args) throws IOException {
        if (args.length != 3 || !"mtg2png".equals(args[0]) && !"makemtg".equals(args[0])) {
            System.err.println("Expected arguments: mtg2png infile.mtg outfile.png\n"+
                    "                or: makemtg infile.whatever outfile.mtg");
            System.exit(1);
        }
        if ("makemtg".equals(args[0])) {
            BufferedImage img  = ImageIO.read(new File(args[1]));
            Megatron.write(img, args[2]);
        } else {
            BufferedImage img = Megatron.read(args[1]);
            ImageIO.write(img, "PNG", new File(args[2]));
        }
    }
}


