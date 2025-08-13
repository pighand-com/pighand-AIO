package com.pighand.aio;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MultiDateDeserializer extends JsonDeserializer<Date> {

    private static final DateTimeFormatter FORMATTER_NORMAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText().trim();

        try {
            if (text.contains("T")) {
                // 假设是 ISO 格式
                OffsetDateTime odt = OffsetDateTime.parse(text, FORMATTER_ISO);
                return Date.from(odt.toInstant());
            } else {
                // 普通格式
                LocalDateTime ldt = LocalDateTime.parse(text, FORMATTER_NORMAL);
                return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format: " + text, e);
        }
    }
}
