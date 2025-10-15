package com.pighand.aio;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime多格式反序列化器
 * 支持多种日期时间格式的反序列化
 * 
 * @author wangshuli
 * @createDate 2025-01-24
 */
public class MultiLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER_NORMAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private static final DateTimeFormatter FORMATTER_ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText().trim();

        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            if (text.contains("T")) {
                // 假设是 ISO 格式
                if (text.contains("+") || text.contains("Z")) {
                    // 带时区的ISO格式
                    OffsetDateTime odt = OffsetDateTime.parse(text, FORMATTER_ISO);
                    return odt.toLocalDateTime();
                } else {
                    // 本地ISO格式
                    return LocalDateTime.parse(text, FORMATTER_ISO_LOCAL);
                }
            } else {
                // 普通格式 yyyy-MM-dd HH:mm:ss
                return LocalDateTime.parse(text, FORMATTER_NORMAL);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid LocalDateTime format: " + text, e);
        }
    }
}