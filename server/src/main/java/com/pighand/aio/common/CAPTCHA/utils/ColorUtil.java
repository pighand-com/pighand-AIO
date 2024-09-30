package com.pighand.aio.common.CAPTCHA.utils;

import java.awt.*;
import java.util.Random;

/**
 * 颜色
 */
public class ColorUtil {
    // 颜色容差
    private static final int COLOR_TOLERANCE = 50;

    /**
     * 获取随机颜色
     *
     * @param backgroundColor
     * @return
     */
    public static Color getColor(Color backgroundColor) {
        Random random = new Random();

        int red = getRGBColor(random, backgroundColor == null ? 0 : backgroundColor.getRed());
        int green = getRGBColor(random, backgroundColor == null ? 0 : backgroundColor.getGreen());
        int blue = getRGBColor(random, backgroundColor == null ? 0 : backgroundColor.getBlue());

        return new Color(red, green, blue);
    }

    /**
     * 获取多个随机颜色
     *
     * @param backgroundColor
     * @param num
     * @return
     */
    public static Color[] getColors(Color backgroundColor, int num) {
        Color[] colors = new Color[num];
        for (int i = 0; i < num; i++) {
            colors[i] = getColor(backgroundColor);
        }
        return colors;
    }

    /**
     * 获取随机单颜色
     * 根据颜色容差计算出排除的区间，然后随机非次区间的颜色
     *
     * @param random
     * @param backgroundRGB
     * @return
     */
    private static int getRGBColor(Random random, int backgroundRGB) {
        // 排除区间
        int excludeBegin = backgroundRGB - COLOR_TOLERANCE / 2;
        int excludeEnd = backgroundRGB + COLOR_TOLERANCE / 2;

        // 合并前区间和后区间，取随机数
        int calculateMax = 255 + excludeBegin - excludeEnd + 1;
        int randomColor = random.nextInt(calculateMax) + excludeEnd;

        // 超过255，说明是前区间的值，需要减去255
        if (randomColor > 255) {
            randomColor = randomColor - 255;
        }

        return randomColor;
    }

}
