package com.pighand.aio.common.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * 缓存request的body
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private final String body;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.body = getBodyString(request);
    }

    public String getBody() {
        return body;
    }

    public String getBodyString(final HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        String bodyString = "";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(contentType) && (contentType.contains("multipart/form-data") || contentType.contains(
            "x-www-form-urlencoded"))) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> next : parameterMap.entrySet()) {
                String[] values = next.getValue();
                String value = null;
                if (values != null) {
                    if (values.length == 1) {
                        value = values[0];
                    } else {
                        value = Arrays.toString(values);
                    }
                }
                sb.append(next.getKey()).append("=").append(value).append("&");
            }
            if (sb.length() > 0) {
                bodyString = sb.substring(0, sb.toString().length() - 1);
            }
            return bodyString;
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }

            return sb.toString();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());

        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
