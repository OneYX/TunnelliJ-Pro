package net.boruvka.idea.tunnellij.http.request;

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
public class HttpRequestBuilder {

    /**
     * http的请求可以分为三部分
     *
     * 第一行为请求行: 即 方法 + URI + 版本
     * 第二部分到一个空行为止，表示请求头
     * 空行
     * 第三部分为接下来所有的，表示发送的内容,message-body；其长度由请求头中的 Content-Length 决定
     *
     * 几个实例如下
     *
     * @param inputStream
     * @return
     */
    public static HttpRequest build(InputStream inputStream) throws IOException {
        BufferedReader httpReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest();
        decodeRequestLine(httpReader, httpRequest);
        decodeRequestHeader(httpReader, httpRequest);
        decodeRequestBody(httpReader, httpRequest);
        return httpRequest;
    }

    /**
     * 根据标准的http协议，解析请求行
     *
     * @param reader
     * @param request
     */
    private static void decodeRequestLine(BufferedReader reader, HttpRequest request) throws IOException {
        String[] strs = StringUtils.split(reader.readLine(), " ");
        assert strs.length == 3;
        request.setMethod(strs[0]);
        request.setUri(strs[1]);
        request.setVersion(strs[2]);
    }

    /**
     * 根据标准http协议，解析请求头
     *
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestHeader(BufferedReader reader, HttpRequest request) throws IOException {
        Map<String, String> headers = new HashMap<>(16);
        String line = reader.readLine();
        String[] kv;
        while (!"".equals(line)) {
            kv = StringUtils.split(line, ":");
            assert kv.length == 2;
            headers.put(kv[0].trim().toLowerCase(), kv[1].trim());
            line = reader.readLine();
        }

        request.setHeaders(headers);
    }

    /**
     * 根据标注http协议，解析正文
     *
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestBody(BufferedReader reader, HttpRequest request) throws IOException {
        int contentLen = Integer.parseInt(request.getHeaders().getOrDefault("content-length", "-1"));
        if (contentLen > 0) {
            char[] body = new char[contentLen];
            reader.read(body);
            request.setBody(new String(body));
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
        request.setBody(body.toString());
    }
}
