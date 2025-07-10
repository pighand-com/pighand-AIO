package com.pighand.aio.common.CAPTCHA.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字体加载器
 * 加载resources/font目录下的所有字体
 */
@Component
public class FontLoader {
    private static List<Font> fonts;

    /**
     * 随机获取一个字体
     *
     * @return
     */
    public static Font randomFont() {
        int index = (int)(Math.random() * fonts.size());
        return fonts.get(index);
    }

    @PostConstruct
    public void loader() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:font/*.ttf");

            if (fonts == null) {
                fonts = new ArrayList<>(resources.length);
            }

            for (Resource resource : resources) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, resource.getInputStream());

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);

                fonts.add(font);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
