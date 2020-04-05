package net.boruvka.idea.tunnellij.http.response;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pingd
 */
public class HttpResponseBuilder {

    /**
     * http的响应可以分为三部分
     *
     * 第一行为请求行: 即 版本 + 状态码
     * 第二部分到一个空行为止，表示请求头
     * 空行
     * 第三部分为接下来所有的，表示发送的内容,message-body；其长度由请求头中的 Content-Length 决定
     *
     * 几个实例如下
     *
     * @param inputStream
     * @return
     */
    public static HttpResponse build(InputStream inputStream) throws IOException {
        BufferedReader httpReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpResponse httpResponse = new HttpResponse();
        decodeRequestLine(httpReader, httpResponse);
        decodeResponseHeader(httpReader, httpResponse);
        decodeRequestBody(httpReader, httpResponse);
        return httpResponse;
    }

    /**
     * 根据标准的http协议，解析请求行
     *
     * @param reader
     * @param response
     */
    private static void decodeRequestLine(BufferedReader reader, HttpResponse response) throws IOException {
        String[] line = StringUtils.split(reader.readLine(), " ");
        assert line.length == 2;
        response.setVersion(line[0]);
        response.setStatus(line[1]);
    }

    /**
     * 根据标准http协议，解析请求头
     *
     * @param reader
     * @param response
     * @throws IOException
     */
    private static void decodeResponseHeader(BufferedReader reader, HttpResponse response) throws IOException {
        Map<String, String> headers = new HashMap<>(16);
        String line = reader.readLine();
        String[] kv;
        while (!"".equals(line)) {
            kv = StringUtils.split(line, ":");
            assert kv.length == 2;
            headers.put(kv[0].trim().toLowerCase(), kv[1].trim());
            line = reader.readLine();
        }

        response.setHeaders(headers);
    }

    /**
     * 根据标注http协议，解析正文
     *
     * @param reader
     * @param response
     * @throws IOException
     */
    private static void decodeRequestBody(BufferedReader reader, HttpResponse response) throws IOException {
        int contentLen = Integer.parseInt(response.getHeaders().getOrDefault("content-length", "-1"));
        if (contentLen > 0) {
            char[] body = new char[contentLen];
            reader.read(body);
            response.setBody(new String(body));
            return;
        }

        // 如get/options请求就没有message
        // 表示没有message，直接返回
        if (contentLen == -1) {
            return;
        }

        // fixme 这种时候，可能是通过 chunked 方式发送数据，待验证这种支持方式是否准确
        StringBuilder body = new StringBuilder();
        int ch;
        while (reader.ready()) {
            ch = reader.read();
            if (ch <= 0) {
                break;
            }
            body.append((char) ch);
        }
        response.setBody(body.toString());
    }
}
