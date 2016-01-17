package entities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Class used to represent an image, built
 * up by a two dimensional array of pixels.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Image {
    private Pixel32bit[][] pixels;

    /**
     * Construct an Image object from a filename.
     *
     * @param path Path to image file
     * @throws IOException
     */
    public Image(String path) throws IOException {
        this.pixels = load(ImageIO.read(new File(path)));
    }

    public Image(Pixel32bit[][] pixels) {
        this.pixels = pixels;
    }

    /**
     * Create a black image with the supplied width
     * and height.
     *
     * @param W Image width
     * @param H Image height
     */
    public Image(int W, int H) {
        this.pixels = new Pixel32bit[H][W];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                pixels[y][x] = new Pixel32bit(x, y);
            }
        }
    }

    /**
     * Get the image width.
     *
     * @return Width
     */
    public int getWidth() {
        return pixels[0].length;
    }

    /**
     * Get the image height.
     *
     * @return Height
     */
    public int getHeight() {
        return pixels.length;
    }

    /**
     * Get the pixel at the position x,y.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return Pixel32bit at x,y
     */
    public Pixel32bit getPixel(int x, int y) {
        if (!(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
            return pixels[y][x];
        }
        return null;
    }

    /**
     * Save the image as a file.
     * @param filename Filename
     */
    public void save(String filename) {
        try {
            BufferedImage bufferedImage = this.getBufferedImage();
            ImageIO.write(bufferedImage, "png", new File(filename + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load an array of pixels from BufferedImage object.
     *
     * @param image BufferedImage
     * @return Array of pixels.
     */
    private Pixel32bit[][] load(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int W = image.getWidth();
        int H = image.getHeight();
        boolean hasAlphaChannel = image.getAlphaRaster() != null;

        Pixel32bit[][] img = new Pixel32bit[H][W];

        int pixelLength = hasAlphaChannel ? 4 : 3;

        for (int p = 0, x = 0, y = 0; p < pixels.length; p += pixelLength) {
            int argb = 0;
            argb += hasAlphaChannel ? ((int) pixels[p] & 0xFF) << 24 : -16777216;
            argb += ((int) pixels[p + (hasAlphaChannel ? 3 : 2)] & 0xFF) << 16;
            argb += ((int) pixels[p + (hasAlphaChannel ? 2 : 1)] & 0xFF) << 8;
            argb +=  (int) pixels[p + (hasAlphaChannel ? 1 : 0)] & 0xFF;

            img[y][x] = new Pixel32bit(argb, x, y);
            x++;

            if (x == W) {
                x = 0;
                y++;
            }
        }

        return img;
    }

    /**
     * Create a BufferedImage from the Image object.
     *
     * @return BufferedImage
     */
    private BufferedImage getBufferedImage() {
        int height  = pixels.length;
        int width   = pixels[0].length;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bufferedImage.setRGB(x, y, pixels[y][x].getARGB());
            }
        }
        return bufferedImage;
    }
}