package com.pighand.aio.common.CAPTCHA.image;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.utils.CodeUtil;
import com.pighand.aio.common.CAPTCHA.utils.ColorUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class ImageAbstract {
    protected static final SecureRandom RANDOM = new SecureRandom();
    protected ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    @Setter
    protected int width = 130; // 验证码显示宽度
    @Setter
    protected int height = 48; // 验证码显示高度
    @Setter
    protected Color bgColor = Color.WHITE; // 验证码背景色
    @Setter
    protected int codeLength = 4; // 验证码随机字符长度
    @Setter
    protected int fontSize = 30; // 验证码字体大小
    protected int codeWidth = width / codeLength; // 验证码显示宽度
    @Getter
    protected String[] codes = CodeUtil.getCode(codeLength);
    protected Color[] colors = ColorUtil.getColors(bgColor, codeLength);
    private boolean isRun = false;

    /**
     * 生成验证码
     */
    private void generate() {
        if (!isRun) {
            isRun = true;
            this.run();
        }
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public String getCodes() {
        return String.join("", codes);
    }

    /**
     * 输出outputStream
     *
     * @return
     */
    public ByteArrayOutputStream outputStream() {
        this.generate();

        return outputStream;
    }

    /**
     * 输出byte[]
     *
     * @return
     */
    public byte[] byteArray() {
        this.generate();

        return outputStream.toByteArray();
    }

    /**
     * 输出base64
     *
     * @return
     */
    public String base64() {
        this.generate();

        return this.base64Prefix() + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 输出codeData
     *
     * @return
     */
    public CodeData getCodeData() {
        this.generate();

        CodeData codeData = new CodeData();
        codeData.setCode(String.join("", codes));
        codeData.setBase64(this.base64());

        return codeData;
    }

    /**
     * 获取字体宽高
     *
     * @param g2d
     * @param code
     * @param index
     * @param fontDimensions
     * @return
     */
    protected Integer[] getFontDimensions(Graphics2D g2d, String code, int index, Integer[][] fontDimensions) {
        Integer fontWidth = fontDimensions[index][0];
        Integer fontHeight = fontDimensions[index][1];

        if (fontWidth == null) {
            FontMetrics fontMetrics = g2d.getFontMetrics();
            fontDimensions[index][0] = fontMetrics.stringWidth(code);
        }

        if (fontHeight == null) {
            FontMetrics fontMetrics = g2d.getFontMetrics();
            fontDimensions[index][1] = fontMetrics.getHeight();
        }

        return fontDimensions[index];
    }

    /**
     * 偏移后x坐标
     *
     * @param index
     * @param offset
     * @return
     */
    protected int offsetX(int index, int fontWidth, double offset) {
        double xOffset = RANDOM.nextDouble(offset * 2 + 1);
        if (xOffset < offset) {
            xOffset = -(xOffset - offset);
        }

        int x = codeWidth * index + codeWidth / 2 - fontWidth / 2 + (int)xOffset;

        return x;
    }

    /**
     * 获取坐标
     *
     * @param fontDimensions
     * @param index
     * @param coordinates
     * @return
     */
    protected Integer[] getCoordinates(Integer[] fontDimensions, int index, Integer[][] coordinates) {
        Integer coordinatesX = coordinates[index][0];
        Integer coordinatesY = coordinates[index][1];

        if (coordinatesX == null) {
            coordinates[index][0] = offsetX(index, fontDimensions[0], (width / codeLength - fontDimensions[0]) / 2);
        }

        if (coordinatesY == null) {
            coordinates[index][1] = offsetY(height - fontDimensions[1]);
        }

        return coordinates[index];

    }

    /**
     * 偏移后坐标
     *
     * @param offset
     * @return
     */
    private int offsetY(int offset) {
        int yOffset = RANDOM.nextInt(offset * 2 + 1);
        if (yOffset < offset) {
            yOffset = -(yOffset - offset);
        }

        int y = height / 2 + yOffset;

        return y;
    }

    /**
     * 画干扰图形
     *
     * @param num
     * @param g2d
     * @param codeColor
     */
    protected void drawDisturbShape(int num, Graphics2D g2d, Color codeColor) {
        for (int i = 0; i < num; i++) {
            g2d.setColor(i == 0 ? codeColor : ColorUtil.getColor(bgColor));

            int shapeWidth = RANDOM.nextInt(height / 2);
            int shapeHeight = RANDOM.nextInt(height / 2);

            int shapeX = RANDOM.nextInt(width - shapeWidth);
            int shapeY = RANDOM.nextInt(height - shapeHeight);

            switch (RANDOM.nextInt(5)) {
                case 0:
                    g2d.drawOval(shapeX, shapeY, shapeWidth, shapeWidth);
                    break;
                case 1:
                    g2d.drawOval(shapeX, shapeY, shapeWidth, shapeHeight);
                    break;
                case 2:
                    g2d.drawRect(shapeX, shapeY, shapeWidth, shapeHeight);
                    break;
                case 3:
                    g2d.drawRoundRect(shapeX, shapeY, shapeWidth, shapeHeight, shapeWidth / 10, shapeHeight / 10);
                    break;
                case 4:
                    g2d.drawLine(shapeX, shapeY, shapeWidth, shapeHeight);
                    break;
                default:
            }
        }
    }

    /**
     * 绘制图片
     *
     * @return
     */
    protected abstract ImageAbstract run();

    /**
     * base64前缀
     *
     * @return
     */
    protected abstract String base64Prefix();

    /**
     * contentType
     *
     * @return
     */
    public abstract String contentType();
}
