package ru.baccasoft.eatster.service.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.google.android.gcm.server.Sender;
import ru.baccasoft.utils.logging.Logger;

/**
 * Workaround to avoid issue #13 of gcm-server
 *
 * @see https://code.google.com/p/gcm/issues/detail?id=13&q=encoding
 *
 * @author rbarriuso /at/ tribalyte.com
 *
 */
public class Utf8Sender extends Sender {
    private static final Logger LOG = Logger.getLogger(Utf8Sender.class);

    private final String key;

    public Utf8Sender(String key) {
        super(key);
        this.key = key;
    }

    @Override
    protected HttpURLConnection post(String url, String contentType, String body) throws IOException {
        if (url == null || body == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        if (!url.startsWith("https://")) {
            LOG.warn("URL does not use https: " + url);
        }
        LOG.debug("Utf8Sender Sending POST to " + url);
        LOG.debug("POST body: " + body);
        byte[] bytes = body.getBytes(UTF8);
        HttpURLConnection conn = getConnection(url);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Authorization", "key=" + key);
        OutputStream out = conn.getOutputStream();
        try {
            out.write(bytes);
        } finally {
            close(out);
        }
        return conn;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore error
                LOG.debug("IOException closing stream: "+ e);
            }
        }
    }

}
