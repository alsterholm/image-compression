package utilities;

import java.util.Iterator;

/**
 * Bit Array representation.
 *
 * @author phatfingers
 * @link http://stackoverflow.com/a/15737163/4704396
 */
public class BitArray implements Iterable<Byte> {

    private byte bitX8[] = null;
    private int index = 0;

    public BitArray(int size) {
        bitX8 = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
    }

    public boolean getBit(int pos) {
        return (bitX8[pos / 8] & (1 << (pos % 8))) != 0;
    }

    public void setBit(int pos, boolean b) {
        byte b8 = bitX8[pos / 8];
        byte posBit = (byte) (1 << (pos % 8));
        if (b) {
            b8 |= posBit;
        } else {
            b8 &= (255 - posBit);
        }
        bitX8[pos / 8] = b8;
    }

    public void write(int v) {
        for (int i = 4; i > 0; i--) {
            byte b = (byte)((v >>> ((i - 1) * 8)) & 0xFF);
            bitX8[index++] = b;
        }
    }

    public void write(byte v) {

    }

    @Override
    public Iterator iterator() {
        return new Iterator<Byte>() {
            private int i = 0;
            @Override
            public boolean hasNext() {
                return i != bitX8.length;
            }

            @Override
            public Byte next() {
                return bitX8[i];
            }
        };
    }

    public byte[] toBytes() {
        return bitX8;
    }
}