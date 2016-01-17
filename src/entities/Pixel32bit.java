package entities;

/**
 * Class used to represent a pixel of
 * an image.
 *
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Pixel32bit {
    private int a, r, g, b, x, y;
    private boolean visited;

    /**
     * Construct a black pixel.
     */
    public Pixel32bit(int x, int y) {
        this(255, 0, 0, 0, x, y);
    }

    /**
     * Construct a new pixel with the supplied
     * color value.
     *
     * @param argb Color
     */
    public Pixel32bit(int argb, int x, int y) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8)  & 0xFF;
        this.b =  argb        & 0xFF;
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a new pixel with the supplied
     * alpha, red, green and blue values.
     *
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public Pixel32bit(int a, int r, int g, int b, int x, int y) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value of the pixel’s alpha channel.
     * @return Alpha
     */
    public int a() {
        return a;
    }

    /**
     * Get the value of the pixel’s red channel.
     * @return Red
     */
    public int r() {
        return r;
    }

    /**
     * Get the value of the pixel’s green channel.
     * @return Green
     */
    public int g() {
        return g;
    }

    /**
     * Get the value of the pixel’s blue channel.
     * @return Blue
     */
    public int b() {
        return b;
    }

    /**
     * Get the pixel’s x position.
     * @return X
     */
    public int x() {
        return x;
    }

    /**
     * Get the pixel’s y position.
     * @return Y
     */
    public int y() {
        return y;
    }

    /**
     * Get the pixel’s intensity.
     * @return Pixel32bit’s intensity
     */
    public double getIntensity() {
        return (double) (this.r + this.g + this.b) / 3;
    }

    /**
     * Get the 32 bit color value of the pixel.
     * @return Pixel32bit’s color
     */
    public int getARGB() {
        int argb = 0;

        argb += (a & 0xFF) << 24;
        argb += (r & 0xFF) << 16;
        argb += (g & 0xFF) << 8;
        argb +=  b & 0xFF;

        return argb;
    }

    /**
     * Set the color of the pixel.
     *
     * @param a Alpha
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public void setARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Set the color of a pixel.
     * @param r Red
     * @param g Green
     * @param b Blue
     */
    public void setRGB(int r, int g, int b) {
        this.setARGB(255, r, g, b);
    }

    /**
     * Set a greyscale color to a pixel.
     * @param c Greyscale value
     */
    public void setRGB(int c) {
        this.setRGB(c, c, c);
    }

    /**
     * Control if the pixel has been visited.
     *
     * @return boolean
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Set the pixel’s visited state.
     *
     * @param visited boolean
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}