package com.pighand.aio.common.CAPTCHA.image;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.pighand.aio.common.CAPTCHA.utils.ColorUtil;
import com.pighand.aio.common.CAPTCHA.utils.FontLoader;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;

/**
 * gif闪烁验证码
 */
public class Flicker extends ImageAbstract {

    /**
     * 绘制gif
     *
     * @return
     */
    public Flicker run() {
        Integer[][] coordinates = new Integer[codeLength][2];
        Integer[][] fontDimensions = new Integer[codeLength][2];

        Font font = FontLoader.randomFont().deriveFont(Font.BOLD, fontSize);

        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        gifEncoder.setSize(width, height);
        gifEncoder.start(outputStream);
        gifEncoder.setDelay(120);
        gifEncoder.setRepeat(0);
        gifEncoder.setQuality(180);

        // 随机生成贝塞尔曲线参数
        int x1 = 5, y1 = RANDOM.nextInt(5, height / 2);
        int x2 = width - 5, y2 = RANDOM.nextInt(height / 2, height - 5);
        int ctrlx = RANDOM.nextInt(width / 4, width / 4 * 3), ctrly = RANDOM.nextInt(5, height - 5);
        if (RANDOM.nextInt(2) == 0) {
            int ty = y1;
            y1 = y2;
            y2 = ty;
        }
        int ctrlx1 = RANDOM.nextInt(width / 4, width / 4 * 3), ctrly1 = RANDOM.nextInt(5, height - 5);
        int[][] besselXY = new int[][] {{x1, y1}, {ctrlx, ctrly}, {ctrlx1, ctrly1}, {x2, y2}};

        for (int i = 0; i < codes.length; i++) {
            BufferedImage frame = this.graphicsImage(codes, colors, coordinates, fontDimensions, i, font, besselXY);
            gifEncoder.addFrame(frame);
        }

        gifEncoder.finish();

        return this;
    }

    /**
     * 绘制单张图片
     *
     * @param codes
     * @param colors
     * @param coordinates
     * @param fontDimensions
     * @param index
     * @param font
     * @param besselXY
     * @return
     */
    private BufferedImage graphicsImage(String[] codes, Color[] colors, Integer[][] coordinates,
        Integer[][] fontDimensions, int index, Font font, int[][] besselXY) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)image.getGraphics();

        // 背景色
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 干扰图
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * RANDOM.nextInt(10)));
        drawDisturbShape(RANDOM.nextInt(codeLength), g2d, colors[index]);

        // 干扰线
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g2d.setColor(ColorUtil.getColor(null));
        CubicCurve2D shape =
            new CubicCurve2D.Double(besselXY[0][0], besselXY[0][1], besselXY[1][0], besselXY[1][1], besselXY[2][0],
                besselXY[2][1], besselXY[3][0], besselXY[3][1]);
        g2d.draw(shape);

        // 当前验证码
        g2d.setFont(font);
        g2d.setColor(colors[index]);

        Integer[] nowFontDimensions = this.getFontDimensions(g2d, codes[index], index, fontDimensions);
        Integer[] nowCoordinates = this.getCoordinates(nowFontDimensions, index, coordinates);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.drawString(codes[index], nowCoordinates[0], nowCoordinates[1]);

        // 下个验证码
        int nextIndex = index == codeLength - 1 ? 0 : index + 1;
        Integer[] nextFontDimensions = this.getFontDimensions(g2d, codes[nextIndex], nextIndex, fontDimensions);
        Integer[] nextCoordinates = this.getCoordinates(nextFontDimensions, nextIndex, coordinates);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.drawString(codes[nextIndex], nextCoordinates[0], nextCoordinates[1]);

        g2d.dispose();

        return image;
    }

    /**
     * base64前缀
     *
     * @return
     */
    @Override
    protected String base64Prefix() {
        return "data:image/gif;base64,";
    }

    @Override
    public String contentType() {
        return "image/gif";
    }
}
