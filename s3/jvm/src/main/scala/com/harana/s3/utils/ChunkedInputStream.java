package com.harana.s3.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;


/**
 * Parse an AWS v4 signature chunked stream.  Reference:
 * <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-streaming.html">...</a>
 */
public class ChunkedInputStream extends FilterInputStream {
    private byte[] chunk;
    private int currentIndex;
    private int currentLength;
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
            value = "URF_UNREAD_FIELD",
            justification = "https://github.com/gaul/s3proxy/issues/205")
    @SuppressWarnings("UnusedVariable")
    private String currentSignature;

    public ChunkedInputStream(InputStream is) {
        super(is);
    }

    @Override
    public int read() throws IOException {
        while (currentIndex == currentLength) {
            String line = readLine(in);
            if (line.equals("")) {
                return -1;
            }
            String[] parts = line.split(";", 2);
            currentLength = Integer.parseInt(parts[0], 16);
            currentSignature = parts[1];
            chunk = new byte[currentLength];
            currentIndex = 0;
            ByteStreams.readFully(in, chunk);
            // TODO: check currentSignature
            if (currentLength == 0) {
                return -1;
            }
            readLine(in);
        }
        return chunk[currentIndex++] & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int i;
        for (i = 0; i < len; ++i) {
            int ch = read();
            if (ch == -1) {
                break;
            }
            b[off + i] = (byte) ch;
        }
        if (i == 0) {
            return -1;
        }
        return i;
    }

    /**
     * Read a \r\n terminated line from an InputStream.
     * @return line without the newline or empty String if InputStream is empty
     */
    private static String readLine(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            int ch = is.read();
            if (ch == '\r') {
                ch = is.read();
                if (ch == '\n') {
                    break;
                } else {
                    throw new IOException("unexpected char after \\r: " + ch);
                }
            } else if (ch == -1) {
                if (builder.length() > 0) {
                    throw new IOException("unexpected end of stream");
                }
                break;
            }
            builder.append((char) ch);
        }
        return builder.toString();
    }
}