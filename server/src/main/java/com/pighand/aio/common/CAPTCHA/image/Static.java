package com.pighand.aio.common.CAPTCHA.image;

import com.pighand.aio.common.CAPTCHA.utils.ColorUtil;
import com.pighand.aio.common.CAPTCHA.utils.FontLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 静态图片验证码
 * 参考 Flicker，生成单张静态图片（PNG）。
 */
public class Static extends ImageAbstract {

    /**
     * 绘制静态图片
     *
     * @return this
     */
    @Override
    public Static run() {
        Integer[][] coordinates = new Integer[codeLength][2];
        Integer[][] fontDimensions = new Integer[codeLength][2];

        Font font = FontLoader.randomFont().deriveFont(Font.BOLD, fontSize);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)image.getGraphics();

        // 背景色
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 干扰图
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * RANDOM.nextInt(10)));
        drawDisturbShape(RANDOM.nextInt(codeLength), g2d, ColorUtil.getColor(bgColor));

        // 干扰线（随机生成 1~2 条贝塞尔曲线）
        int disturbLineCount = 1 + RANDOM.nextInt(2);
        for (int i = 0; i < disturbLineCount; i++) {
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

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            g2d.setColor(ColorUtil.getColor(null));
            CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx, ctrly, ctrlx1, ctrly1, x2, y2);
            g2d.draw(shape);
        }

        // 绘制验证码字符
        g2d.setFont(font);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        for (int i = 0; i < codes.length; i++) {
            g2d.setColor(colors[i]);

            Integer[] nowFontDimensions = this.getFontDimensions(g2d, codes[i], i, fontDimensions);
            Integer[] nowCoordinates = this.getCoordinates(nowFontDimensions, i, coordinates);

            g2d.drawString(codes[i], nowCoordinates[0], nowCoordinates[1]);
        }

        g2d.dispose();

        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("生成静态验证码失败", e);
        }

        return this;
    }

    /**
     * base64前缀
     */
    @Override
    protected String base64Prefix() {
        return "data:image/png;base64,";
    }

    /**
     * contentType
     */
    @Override
    public String contentType() {
        return "image/png";
    }
}