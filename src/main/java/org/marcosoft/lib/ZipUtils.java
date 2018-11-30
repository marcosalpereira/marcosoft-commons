package org.marcosoft.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

	public final void unZip(String zipFile, String outputFolder) throws IOException {
		unZip(zipFile, outputFolder, new ProgressConsole());
	}

	protected void unziped(File file) {

	}

	public final void unZip(String zipFile, String outputFolder, Progress progress) throws IOException {
		unzip(this, zipFile, outputFolder, progress);
	}

	public final static void unzip(String zipFile, String outputFolder, Progress progress) throws IOException {
		unzip(new ZipUtils(), zipFile, outputFolder, progress);
	}

	private static void unzip(ZipUtils zipUtils, String zipFile, String outputFolder, Progress progress) throws FileNotFoundException, IOException {
		final byte[] buffer = new byte[1024];

		final ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));

		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			final String fileName = ze.getName();
			final File newFile = new File(outputFolder + File.separator + fileName);
            if (ze.isDirectory()) {
                newFile.mkdirs();
                ze = zis.getNextEntry();
                continue;
            }

			progress.setProgress("Descompactando %s", new Object[] { newFile.getAbsoluteFile() });

			final FileOutputStream fos = new FileOutputStream(newFile);

			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
			zipUtils.unziped(newFile);

			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
	}

}
