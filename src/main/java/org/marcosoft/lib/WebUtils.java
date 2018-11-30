package org.marcosoft.lib;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {
	private static final int BUFFER_SIZE = 4096;

	public static boolean exists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            final HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                .openConnection();
			con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
	}

	public static String download(String fileUrl, Progress progress) {
		try {
			System.out.println(fileUrl);
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			downloadFile(fileUrl, out, progress);
			return new String(out.toByteArray());
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void downloadFile(String url, String outputFile, Progress progress) throws IOException {
		System.out.println(url + "\n\t->" + outputFile);
		final java.io.OutputStream out = new FileOutputStream(outputFile);
		WebUtils.downloadFile(url, out, progress);
		IOUtils.closeQuietly(out);
	}
	public static void downloadFile(String fileUrl, OutputStream outputStream, Progress progress) throws IOException {
		InputStream inputStream = null;
		HttpURLConnection urlConnection = null;

		try {
			final URL url = new URL(fileUrl);
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.connect();

			inputStream = url.openStream();

            final byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0L;
			int percentCompleted = 0;
			final long fileSize = urlConnection.getContentLength();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);

				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100L / fileSize);
				progress.setProgress(percentCompleted);
			}

		} finally {
			IOUtils.closeQuietly(inputStream);
			if (urlConnection != null) urlConnection.disconnect();
			progress.finished();
		}
	}

	public static String encode(String url) {
		try {
			return java.net.URLEncoder.encode(url, "UTF-8").replaceAll("\\+", "%20");
		} catch (final java.io.UnsupportedEncodingException e) {
		}
		return url;
	}
}
