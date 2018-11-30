package org.marcosoft.lib;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class IOUtils {
    /**
     * The default buffer size to use.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static String readContent(java.io.InputStream stream, String encoding) {
		return new Scanner(stream, encoding).useDelimiter("\\Z").next();
	}

	public static byte[] readContent(java.io.InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyLarge(input, output);
        return output.toByteArray();

	}

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (final IOException e) {
		}
	}

}
