package utilities;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by andreas on 2016-01-17.
 */
public class CustomPalette {
    public static int[] create(BufferedImage image) {
        Raster raster = image.getRaster();
        int[] palette = new int[256];
        HashMap<Integer, Integer> map = new HashMap<>();
        int color;
        int[] p = new int[3];

        for (int x = 0, W = image.getWidth(); x < W; x++) {
            for (int y = 0, H = image.getHeight(); y < H; y++) {
                p = raster.getPixel(x, y, p);

                color = ImageUtils.toInt(p[0], p[1], p[2]);

                int n = map.containsKey(color) ? map.get(color) + 1: 1;
                map.put(color, n);
            }
        }

        LinkedList<Integer> list = new LinkedList<>();

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            list.add(e.getKey());
        }

        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o2) - map.get(o1);
            }
        });

        int index = 1;
        palette[0] = list.get(0);
        boolean enoughDistance = false;


        for (int i = 0; i < list.size() && index < 232; i++) {
            for (int j = 0; j < index; j++) {
                if (ImageUtils.distanceInLAB(palette[j], list.get(i)) < 3) {
                    enoughDistance = false;
                    break;
                } else {
                    enoughDistance = true;
                }
            }

            if (enoughDistance)
                palette[index++] = list.get(i);
        }

        palette[232] = 0x000000;
        palette[233] = 0x080808;
        palette[255] = 0xFFFFFF;

        int intensity = 8;
        for (int i = 234; i < 255; i++) {
            intensity += 10;
            palette[i] = ImageUtils.toInt(intensity, intensity, intensity);
        }

        return palette;
    }
}
