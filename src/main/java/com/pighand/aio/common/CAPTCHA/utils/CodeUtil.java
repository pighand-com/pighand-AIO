package com.pighand.aio.common.CAPTCHA.utils;

/**
 * 验证码
 */
public class CodeUtil {
    // 数字
    private static final String[] NUMBER = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    // 小写字母，排除l、o
    private static final String[] LOWERCASE_LETTER =
        {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z"};

    // 大写字母，排除I
    private static final String[] UPPERCASE_LETTER =
        {"A", "B", "C", "D", "E", "F", "G", "H", "J", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z"};

    /**
     * 获取随机验证码
     *
     * @param len
     * @return
     */
    public static String[] getCode(int len) {
        String[] codes = new String[len];

        for (int i = 0; i < len; i++) {
            int type = (int)(Math.random() * 3);
            switch (type) {
                case 0:
                    codes[i] = NUMBER[(int)(Math.random() * NUMBER.length)];
                    break;
                case 1:
                    codes[i] = LOWERCASE_LETTER[(int)(Math.random() * LOWERCASE_LETTER.length)];
                    break;
                case 2:
                    codes[i] = UPPERCASE_LETTER[(int)(Math.random() * UPPERCASE_LETTER.length)];
                    break;
            }
        }

        return codes;
    }
}
