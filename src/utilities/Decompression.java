package utilities;

import java.util.Arrays;

/**Class that handles the decompression of a BCI file back to a MTG file
 * @author Jimmy Lindström (ae7220)
 * @author Andreas Indal (ae2922)
 */
public class Decompression {
    private static final int RESERVED_FOR_COLORS = 1024;

    /**
     * Runs the decompression
     * @param bytes compressed byte-array
     * @return decompressed byte-array
     */
    public static byte[] run(byte[] bytes) {
        byte[] out = new byte[(bytes.length - (RESERVED_FOR_COLORS)) * 3];

        int[] colors = Decompression.createColors(bytes);

        int s = 0;
        //restoring the colors by fetching the right color from
        //the color array
        for (int i = RESERVED_FOR_COLORS; i < bytes.length; i++) {
            int c = colors[bytes[i] & 0xFF];

            out[s++] = (byte) ((c >>> 16) & 0xFF);
            out[s++] = (byte) ((c >>> 8)  & 0xFF);
            out[s++] = (byte)  (c & 0xFF);
        }

        return out;
    }

    /**
     * Recreates the array of used colors that is saved in the beginning of the
     * byte[] from the compressed file
     * @param bytes compressed byte-array
     * @return the color array
     */
    private static int[] createColors(byte[] bytes) {
        bytes = Arrays.copyOfRange(bytes, 0, RESERVED_FOR_COLORS);
        int[] colors = new int[256];

        for (int i = 0, c = 0, b = 0; i < bytes.length; i += 4, c++) {
            int color = 0;

            color |= (bytes[b++] & 0xFF) << 24;
            color |= (bytes[b++] & 0xFF) << 16;
            color |= (bytes[b++] & 0xFF) << 8;
            color |= (bytes[b++] & 0xFF);

            colors[c] = color;
        }

        return colors;
    }
}
